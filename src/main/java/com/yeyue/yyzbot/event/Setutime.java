package com.yeyue.yyzbot.event;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yeyue.yyzbot.constants.Constants;
import com.yeyue.yyzbot.utils.HttpUtils;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.ForwardMessageBuilder;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.PlainText;
import net.mamoe.mirai.utils.ExternalResource;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Component
public class Setutime {

    private static final String loliconUrl = "https://api.lolicon.app/setu/v2";
    private int friendMode = 0;
    private int groupMode = 0;
    private static final Map<String, String> timesMap = new HashMap<>();

    // 修改群/私聊对应的模式
    public void changeMode(int mode, int type, MessageEvent event) {
        //type=1为修改好友的色图mode
        if (type == 1) {
            this.friendMode = mode;
        } else {
            this.groupMode = mode;
        }

        if (mode == 0) {
            event.getSubject().sendMessage("启用正常模式");
        }
        if (mode == 1) {
            event.getSubject().sendMessage("反省吧，色批");
        }
        if (mode == 2) {
            event.getSubject().sendMessage("启动混合模式");
        }
    }

    //私聊处理
    public void sendFriendSetu(MessageEvent event) {
        String[] command = event.getMessage().serializeToMiraiCode().split("\\s+");
        StringBuilder getImgurl = new StringBuilder(loliconUrl + "?r18=" + friendMode);
        if (command.length > 1) {
            String[] tag = command[1].split("AND");
            for (String s : tag) {
                getImgurl.append("&tag=").append(s.replace("OR", "|"));
            }
        }
        sendSetu(event, getImgurl.toString());
    }

    //群聊处理
    public void sendGroupSetu(MessageEvent event) {
        //群组发色图每人每天次数限制
        reduceTimes(event);

        String[] command = event.getMessage().serializeToMiraiCode().split("\\s+");
        StringBuilder getImgurl = new StringBuilder(loliconUrl + "?r18=" + groupMode);
        if (command.length > 2) {
            String[] tag = command[2].split("AND");
            for (String s : tag) {
                getImgurl.append("&tag=").append(s.replace("OR", "|"));
            }
        }
        sendSetu(event, getImgurl.toString());
    }

    //发图
    private void sendSetu(MessageEvent event, String getImgurl) {
        System.out.println("请求url" + getImgurl);
        JSONObject loliData = JSONObject.parseObject(HttpUtils.doGet(getImgurl));
        JSONArray data = loliData.getJSONArray("data");
        if (data != null && data.size() > 0) {
            JSONObject first = data.getJSONObject(0);
            JSONObject imgUrlData = first.getJSONObject("urls");
            String imgUrl = imgUrlData.getString("original");
            try {
                InputStream in = HttpUtils.getInputStreamByUrl(imgUrl);
                ExternalResource externalResource = ExternalResource.create(Objects.requireNonNull(in));
                Image image = event.getSubject().uploadImage(externalResource);
                event.getSubject().sendMessage(image);
//                ForwardMessageBuilder helpMesBuilder = new ForwardMessageBuilder(event.getSubject());
//                helpMesBuilder.add(1043097469L, "うてな", image);
//                event.getSubject().sendMessage(new MessageChainBuilder().append(helpMesBuilder.build()).build());

                externalResource.close();
                in.close();

            } catch (Exception e) {
                event.getSubject().sendMessage(e.getMessage());
            }
        } else {
            event.getSubject().sendMessage("没有找到色图惹");
        }
    }

    //扣除色图次数
    private boolean reduceTimes(MessageEvent messageEvent) {
        String dateStr = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        Long qq = messageEvent.getSender().getId();
        String map = timesMap.get(qq + "");
        if (map != null && !map.equals("")) {
            String[] str = map.split(":");
            int times = Integer.parseInt(str[1]);
            if (times > Constants.MAX_SETUTIMES) {
                return false;
            }
            if (dateStr.equals(str[0])) {
                timesMap.put(qq + "", str[0] + ":" + (times + 1));
            } else {
                timesMap.put(qq + "", dateStr + ":1");
            }
        } else {
            timesMap.put(qq + "", dateStr + ":1");
        }
        return true;
    }
}
