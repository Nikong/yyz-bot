package com.yeyue.yyzbot.route;

import com.yeyue.yyzbot.enums.CommandsEnum;
import com.yeyue.yyzbot.enums.TypeEnum;
import com.yeyue.yyzbot.event.BlueArchive;
import com.yeyue.yyzbot.event.ChatGPT;
import com.yeyue.yyzbot.event.Everyday60s;
import com.yeyue.yyzbot.event.Help;
import com.yeyue.yyzbot.event.SauceNAOSearcher;
import com.yeyue.yyzbot.event.Setutime;
import com.yeyue.yyzbot.event.WhatAnime;
import net.mamoe.mirai.event.events.FriendMessageEvent;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class FriendMsgRoute {
    @Resource
    Help help;
    @Resource
    Everyday60s everyday60s;
    @Resource
    Setutime setutime;
    @Resource
    WhatAnime whatAnime;
    @Resource
    SauceNAOSearcher sauceNAOSearcher;
    @Resource
    ChatGPT chatGPT;
    @Resource
    BlueArchive blueArchive;

    public void route(FriendMessageEvent event) {
        String[] str = event.getMessage().serializeToMiraiCode().split("\\s+");
        //命令列表
        if (str[0].equals(CommandsEnum.HELP.getOrder())) {
            help.sendHelpList(event);
            return;
        }

        //每天60秒读懂世界
        if (str[0].equals(CommandsEnum.EVERYDAY60S.getOrder())) {
            everyday60s.getEveryday60s(event);
            return;
        }

        //色图
        if (str[0].equals(CommandsEnum.SETUTIME.getOrder())) {
            if (str.length > 2) {
                if (str[1].equals("正常模式")) {
                    setutime.changeMode(0, 2, event);
                    return;
                }
                if (str[1].equals("lsp模式")) {
                    setutime.changeMode(1, 2, event);
                    return;
                }
                if (str[1].equals("混合模式")) {
                    setutime.changeMode(2, 2, event);
                    return;
                }
            }
            setutime.sendFriendSetu(event);
            return;
        }

        //搜图
        if (str[0].equals(CommandsEnum.WHATANIME.getOrder())) {
            whatAnime.searchAnime(event, TypeEnum.FRIEND.getValue());
            return;
        }
        //搜图
        if (str[0].equals(CommandsEnum.SAUCENAO.getOrder())) {
            sauceNAOSearcher.searchImage(event, TypeEnum.FRIEND.getValue());
            return;
        }

        if (str[0].equals("ba")) {
            blueArchive.ask(event, TypeEnum.FRIEND.getValue());
        }

        //chatGPT
        //chatGPT.chatGPT(event ,TypeEnum.FRIEND.getValue());


    }
}
