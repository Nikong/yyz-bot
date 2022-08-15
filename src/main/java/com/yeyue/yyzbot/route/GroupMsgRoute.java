package com.yeyue.yyzbot.route;

import com.yeyue.yyzbot.constants.CommandsEnum;
import com.yeyue.yyzbot.constants.TypeEnum;
import com.yeyue.yyzbot.event.Chat;
import com.yeyue.yyzbot.event.Everyday60s;
import com.yeyue.yyzbot.event.Help;
import com.yeyue.yyzbot.event.SauceNAOSearcher;
import com.yeyue.yyzbot.event.Setutime;
import com.yeyue.yyzbot.event.WhatAnime;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.event.events.MessageEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GroupMsgRoute {
    @Autowired
    Help help;
    @Autowired
    Everyday60s everyday60s;
    @Autowired
    Chat chat;
    @Autowired
    Setutime setutime;
    @Autowired
    WhatAnime whatAnime;
    @Autowired
    SauceNAOSearcher sauceNAOSearcher;

    public void route(GroupMessageEvent event){
        String[] str = event.getMessage().serializeToMiraiCode().split("\\s+");
        //判断at了Bot做的处理
        if(checkAtBot(event)){
            //命令列表
            if(str[1].equals(CommandsEnum.HELP.getOrder())){
                help.sendHelpList(event);
                return;
            }

            //每天60秒读懂世界
            if(str[1].equals(CommandsEnum.EVERYDAY60S.getOrder())){
                everyday60s.getEveryday60s(event);
                return;
            }

            //色图
            if(str[1].equals(CommandsEnum.SETUTIME.getOrder())){
                if(str.length>2){
                    if(str[2].equals("正常模式")){
                        setutime.changeMode(0,2,event);
                        return;
                    }
                    if(str[2].equals("lsp模式")){
                        setutime.changeMode(1,2,event);
                        return;
                    }
                    if(str[2].equals("混合模式")){
                        setutime.changeMode(2,2,event);
                        return;
                    }
                }
                setutime.sendGroupSetu(event);
                return;
            }

            //搜图
            if(str[1].equals(CommandsEnum.WHATANIME.getOrder())){
                whatAnime.searchAnime(event, TypeEnum.GROUP.getValue());
                return;
            }
            //搜图
            if(str[1].equals(CommandsEnum.SAUCENAO.getOrder())){
                sauceNAOSearcher.searchImage(event ,TypeEnum.GROUP.getValue());
                return;
            }

        }

        //文字聊天
        chat.chatTo(event);
    }


    public Boolean checkAtBot(MessageEvent messageEvent) {
        if (messageEvent.getMessage().serializeToMiraiCode().startsWith("[mirai:at:" + messageEvent.getBot().getId() + "]")) {
            //是否at机器人开头
            return true;
        }
        return false;
    }
}
