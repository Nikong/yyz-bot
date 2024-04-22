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
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.ForwardMessageBuilder;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.PlainText;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class GroupMsgRoute {
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


    public void route(GroupMessageEvent event) {
        String[] str = event.getMessage().serializeToMiraiCode().split("\\s+");
        //判断at了Bot做的处理
        if (checkAtBot(event)) {
            //命令列表
            if (str[1].equals(CommandsEnum.HELP.getOrder())) {
                help.sendHelpList(event);
                return;
            }

            //每天60秒读懂世界
            if (str[1].equals(CommandsEnum.EVERYDAY60S.getOrder())) {
                everyday60s.getEveryday60s(event);
                return;
            }

            //色图
            if (str[1].equals(CommandsEnum.SETUTIME.getOrder())) {
                if (str.length > 2) {
                    if (str[2].equals("正常模式")) {
                        setutime.changeMode(0, 2, event);
                        return;
                    }
                    if (str[2].equals("lsp模式")) {
                        setutime.changeMode(1, 2, event);
                        return;
                    }
                    if (str[2].equals("混合模式")) {
                        setutime.changeMode(2, 2, event);
                        return;
                    }
                }
                setutime.sendGroupSetu(event);
                return;
            }

            //搜图
            if (str[1].equals(CommandsEnum.WHATANIME.getOrder())) {
                whatAnime.searchAnime(event, TypeEnum.GROUP.getValue());
                return;
            }
            //搜图
            if (str[1].equals(CommandsEnum.SAUCENAO.getOrder())) {
                sauceNAOSearcher.searchImage(event, TypeEnum.GROUP.getValue());
                return;
            }

            //ba
            if (str[1].equals("ba")) {
                blueArchive.ask(event, TypeEnum.GROUP.getValue());
            }

            //chatGPT
//            chatGPT.chatGPT(event ,TypeEnum.GROUP.getValue());


        }
    }


    public Boolean checkAtBot(MessageEvent messageEvent) {
        if (messageEvent.getMessage().serializeToMiraiCode().startsWith("[mirai:at:" + messageEvent.getBot().getId() + "]")) {
            //是否at机器人开头
            return true;
        }
        return false;
    }

}
