package com.meoowtoy.commandaliasvelocity;

import com.google.inject.Inject;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Plugin(
        id = "commandaliasvelocity",
        name = "CommandAliasVelocity",
        version = "1.0.0",
        authors = {"meoowtoy"},
        description = "A lightweight command alias plugin for Velocity"
)
public class CommandAliasVelocityPlugin {

    private final ProxyServer server;
    private final Logger logger;
    private final Path dataDirectory;
    private final List<CommandMeta> registeredCommands = new ArrayList<>();
    private List<String> aliases;

    @Inject
    public CommandAliasVelocityPlugin(ProxyServer server, Logger logger, @DataDirectory Path dataDirectory) {
        this.server = server;
        this.logger = logger;
        this.dataDirectory = dataDirectory;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        // 确保数据目录存在
        if (!Files.exists(dataDirectory)) {
            try {
                Files.createDirectories(dataDirectory);
            } catch (IOException e) {
                logger.error("无法创建数据目录", e);
                return;
            }
        }

        // 加载配置
        loadConfig();

        // 注册重载命令
        registerReloadCommand();

        logger.info("CommandAliasVelocity 插件已加载！");
    }

    private void loadConfig() {
        Path configFile = dataDirectory.resolve("aliases.yml");

        // 如果配置文件不存在，创建默认配置
        if (!Files.exists(configFile)) {
            try {
                Files.writeString(configFile,
                        "# CommandAliasVelocity 配置文件\n" +
                                "# 格式: \"别名:原命令\"\n" +
                                "aliases:\n" +
                                "  - \"lobby:server lobby\"\n" +
                                "  - \"hub:server hub\"\n" +
                                "  - \"survival:server survival\"\n" +
                                "  - \"creative:server creative\"\n" +
                                "  - \"tpa:tpa\"\n" +
                                "  - \"tpahere:tpahere\"\n" +
                                "  - \"spawn:server spawn\"\n" +
                                "  - \"shop:server shop\"\n"
                );
                logger.info("已创建默认配置文件: {}", configFile);
            } catch (IOException e) {
                logger.error("无法创建配置文件", e);
                return;
            }
        }

        // 读取配置
        try {
            List<String> lines = Files.readAllLines(configFile);
            aliases = new ArrayList<>();

            for (String line : lines) {
                line = line.trim();
                // 跳过注释和空行
                if (line.startsWith("#") || line.isEmpty() || !line.startsWith("-")) {
                    continue;
                }

                // 解析别名配置
                if (line.startsWith("- \"") && line.endsWith("\"")) {
                    String aliasConfig = line.substring(3, line.length() - 1);
                    aliases.add(aliasConfig);
                }
            }

            logger.info("已加载 {} 个别名", aliases.size());

            // 注册所有别名命令
            registerAliasCommands();

        } catch (IOException e) {
            logger.error("无法读取配置文件", e);
        }
    }

    private void registerAliasCommands() {
        CommandManager commandManager = server.getCommandManager();

        // 先取消所有已注册的别名命令
        for (CommandMeta meta : registeredCommands) {
            commandManager.unregister(meta);
        }
        registeredCommands.clear();

        // 注册新的别名命令
        for (String aliasConfig : aliases) {
            String[] parts = aliasConfig.split(":", 2);
            if (parts.length != 2) {
                logger.warn("无效的别名配置: {}", aliasConfig);
                continue;
            }

            String alias = parts[0].trim();
            String originalCommand = parts[1].trim();

            // 创建命令元数据
            CommandMeta meta = commandManager.metaBuilder(alias)
                    .plugin(this)
                    .build();

            // 创建命令执行器
            SimpleCommand command = new SimpleCommand() {
                @Override
                public void execute(Invocation invocation) {
                    // 获取原始命令和参数
                    String fullCommand = originalCommand;
                    String[] args = invocation.arguments();
                    if (args.length > 0) {
                        fullCommand += " " + String.join(" ", args);
                    }

                    // 执行原始命令
                    commandManager.executeAsync(invocation.source(), fullCommand);
                }

                @Override
                public boolean hasPermission(Invocation invocation) {
                    return true; // 所有人都可以使用别名命令
                }
            };

            // 注册命令
            commandManager.register(meta, command);
            registeredCommands.add(meta);

            logger.info("注册命令别名: /{} -> /{}", alias, originalCommand);
        }
    }

    private void registerReloadCommand() {
        CommandManager commandManager = server.getCommandManager();

        CommandMeta meta = commandManager.metaBuilder("careload")
                .aliases("caliasreload", "car")
                .plugin(this)
                .build();

        SimpleCommand reloadCommand = new SimpleCommand() {
            @Override
            public void execute(Invocation invocation) {
                if (!invocation.source().hasPermission("commandaliasvelocity.reload")) {
                    invocation.source().sendMessage(
                            net.kyori.adventure.text.Component.text(
                                    "§c你没有权限使用此命令！",
                                    net.kyori.adventure.text.format.NamedTextColor.RED
                            )
                    );
                    return;
                }

                loadConfig();
                invocation.source().sendMessage(
                        net.kyori.adventure.text.Component.text(
                                "§aCommandAliasVelocity 配置已重载！",
                                net.kyori.adventure.text.format.NamedTextColor.GREEN
                        )
                );

                logger.info("{} 重载了 CommandAliasVelocity 配置", invocation.source());
            }

            @Override
            public boolean hasPermission(Invocation invocation) {
                return invocation.source().hasPermission("commandaliasvelocity.reload");
            }
        };

        commandManager.register(meta, reloadCommand);
    }
}
