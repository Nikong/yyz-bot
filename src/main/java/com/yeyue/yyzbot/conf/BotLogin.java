package com.yeyue.yyzbot.conf;

import com.yeyue.yyzbot.route.FriendMsgRoute;
import com.yeyue.yyzbot.route.GroupMsgRoute;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.BotFactory;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.events.FriendMessageEvent;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.utils.BotConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Configuration
public class BotLogin {
    @Value("${bot.qq}")
    private  Long qq;

    @Value("${bot.passwd}")
    private String passwd;

    @Autowired
    FriendMsgRoute friendMsgRoute;
    @Autowired
    GroupMsgRoute groupMsgRoute;

    /**
     *
     * bot登录.
     */
    @PostConstruct
    public void login() {
        //bot信息
        Bot bot = BotFactory.INSTANCE.newBot(qq, passwd, new BotConfiguration() {{
            fileBasedDeviceInfo(); // 存储device.json
            setProtocol(MiraiProtocol.ANDROID_PAD); // 切换协议
        }});
        bot.login();
        //bot分配消息处理
        bot.getEventChannel().subscribeAlways(GroupMessageEvent.class, this::handGroupMessage);
        bot.getEventChannel().subscribeAlways(FriendMessageEvent.class, this::handFriendMessage);
    }


    @EventHandler
    public void handGroupMessage(GroupMessageEvent event) {
        groupMsgRoute.route(event);
    }


    @EventHandler
    public void handFriendMessage(FriendMessageEvent event) {
        friendMsgRoute.route(event);
    }
}
