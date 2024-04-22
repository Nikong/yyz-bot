# yyzbot  
夜夜子bot是一个基于Mirai-Core的简单易懂的springboot项目———QQ机器人（已整合chatGPT）  


目前qq风控太严，使用手表二维码协议登录，启动项目用对应的qq扫码登录


已整合:  
√ 每天60秒读懂世界  
√ 搜番(whatAnime)  
√ 搜图(sauceNAO)  
√ ChatGPT3.5  
√ ba简单功能(arona提供,杂图为功能表)  
√ 瑟图(api.lolicon.app提供的图库)  

待整合:  
× 推特推送(API需要巨资，搁置)


项目结构:  
yyzbot-  
&emsp;|-conf                (bot的初始化)  
&emsp;|-constants           (放置常量)  
&emsp;&emsp;|-CommandsEnum      (功能目录)  
&emsp;|-event               (接入的功能)  
&emsp;|-route               (路由，根据好友和群发来的消息，分配到对应的功能)  
&emsp;|-utils               (工具类)  

项目流程:
接受QQ消息——群路由或好友路由匹配对应的功能——event里对应的事件


使用:  
1.application.yml里填写自己的QQ号和密码，还有各类的token,apiKey  
2.可以根据群route和好友route指定对应功能，@bot help可以看功能菜单，好友如：60s&emsp;群如：@bot saucenao [图片]
