package com.yeyue.yyzbot.event;

import com.yeyue.yyzbot.constants.CommandsEnum;
import com.yeyue.yyzbot.constants.ErnieVilgEnum;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import org.springframework.stereotype.Component;

//菜单事件
@Component
public class Help {

    public void sendHelpList(MessageEvent event){
        MessageChainBuilder messages = new MessageChainBuilder();
        messages.append("命令列表:");
        for (CommandsEnum s : CommandsEnum.values()) {
            messages.append(System.getProperty("line.separator"));
            messages.append(s.getComment()+"------->"+s.getFrom());
        }
        event.getSubject().sendMessage(messages.build());
    }

    public void sendErnieVilgHelpList(MessageEvent event){
        MessageChainBuilder messages = new MessageChainBuilder();
        messages.append("命令列表:");
        for (ErnieVilgEnum s : ErnieVilgEnum.values()) {
            messages.append(System.getProperty("line.separator"));
            messages.append(s.getNum()+"------->"+s.getValue());
        }
        event.getSubject().sendMessage(messages.build());
    }
}
