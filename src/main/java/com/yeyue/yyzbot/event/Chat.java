package com.yeyue.yyzbot.event;

import net.mamoe.mirai.event.events.MessageEvent;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class Chat {

    public void chatTo(MessageEvent event){
        String str = event.getMessage().serializeToMiraiCode();

        if(str.contains("阿龙是什么")){
            List<String> list = new ArrayList<>();
            list.add("阿龙是傻逼");
            list.add("阿龙是笨比");
            list.add("阿龙是大聪明");
            list.add("阿龙是可爱妹妹");
            list.add("阿龙是天才妹妹");
            int random = (int) (Math.random()*list.size());
            event.getSubject().sendMessage(list.get(random));
        }else {
            //100分之1的概率复读
            int random = (int) (1+Math.random()*100);
            if(random==1) {
                event.getSubject().sendMessage(event.getMessage());
            }
        }
    }
}
