# minecraft-velocity-plugin-CommandAliasVelocity
A command alias plugin on velocity
Releases are in CommandAliasVelocity/build/libs/
原作者A2whatever33，原作是MIT协议，下面都是基本上都是照搬的介绍。我改的这个velocity版本的我没有测试很多。


CommandAliasVelocity - 自定义命令别名插件

[MIT][Velocity]
 插件简介

CommandAliasVelocity 是一个轻量级但功能强大的 Minecraft Paper Velocity 服务器插件，它允许管理员通过简单的配置文件自定义命令别名，让玩家可以使用更直观、更方便的命令来执行原有功能。同时插件还内置了便捷的管理员工具命令。


 主要特性
 核心功能  
    自定义命令别名 - 通过配置文件轻松设置命令别名
    完全兼容原版命令 - 不修改任何原版命令，安全稳定
    子命令支持 - 完美支持带有子命令的复杂命令
    大小写不敏感 - 别名不区分大小写，提升用户体验
    实时重载 - 无需重启服务器即可应用配置更改
     便捷工具
    权限管理 - 精细的权限控制，确保服务器安全
     命令列表
     
        #命令            描述            权限                     
        /careload       重载命令别名配置  使用代理端执行  有op权限也不行(因为该指令没有注册入velocity)  
        /careload
        /caliasreload
        /car
  权限节点
      这个呢...嗯...去服务器执行/careload, /caliasreload, /car 才能重载（没注册到velocity的指令里面）
  配置说明
  插件配置文件位于 plugins/commandaliasvelocity/aliases.yml
  aliases:
    
      # 简单命令别名
      # "别名:原命令"
      - "传送:tpa"
      - "t:tpa"
      # 带子命令的别名(这个功能暂时用不了)
      - "stptp:teleport"
      - "stptime:time set"
      
安装方法
        下载CommandAliasVelocity1.0.0.jar 文件
        将文件放入服务器的 plugins 文件夹
        重启服务器或使用插件管理命令重载
        编辑 plugins/commandaliasvelocity/aliases.yml 配置文件
        在服务器命令行使用 /careload等指令 应用更改
使用教程
        基础使用
        安装插件后，编辑 aliases.yml 文件
        添加你想要的命令别名，例如：

    - "传送:tpa"
    - "tpahere:tpahere"
  保存文件并在游戏中执行 /careload, /caliasreload, /car
  现在玩家可以使用 /传送 来执行 /tpa 命令了


高级用法

    子命令支持："stp tp:teleport" 可以让 /stp tp &lt;玩家&gt; 执行 /teleport &lt;玩家&gt;（二创的作者没看懂，保留吧）
    参数传递：所有参数都会自动传递到原命令
    多级别名：支持复杂的多级命令别名设置

 实际应用场景

 特色亮点
适合以下服务器类型：

    技术服务器 - 为复杂命令创建简写
    小游戏服务器 - 为小游戏创建专用命令
    多世界服务器 - 简化传送命令
    新手友好服务器 - 使用中文或其他易懂的命令
    RPG服务器 - 为技能和功能创建特色命令

 特色亮点

    零学习成本 - 配置简单直观，一看就会
    性能优化 - 轻量级设计，不影响服务器性能
    完全兼容 - 与绝大多数插件兼容
    安全可靠 - 不修改核心文件，确保服务器稳定
    持续更新 - 我这个人呢 不知道会不会 如果能联系到我的话可以更新 这是第一回做插件

我修不好我就跑路:( 

大家快快去支持原作者A2whatever33。



让命令更简单，让游戏更快乐！

CommandAliasVelocity
 - 为您的代理服务器量身定制的命令系统
