package com.yeyue.yyzbot.event;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yeyue.yyzbot.enums.TypeEnum;
import com.yeyue.yyzbot.utils.HttpUtils;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.utils.ExternalResource;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.Objects;

@Component
public class BlueArchive {
    private final static String imgUrl = "https://arona.cdn.diyigemt.com/image";

    public void ask(MessageEvent event, Integer type) {
        String[] command = event.getMessage().serializeToMiraiCode().split("\\s+");
        String str = type.equals(TypeEnum.FRIEND.getValue()) ? command[1] : command[2];
        send(event, str);
    }

    //建议做缓存
    public void send(MessageEvent event, String str) {
        try {
            HttpResponse response = HttpRequest.get("https://arona.diyigemt.com/api/v2/image").form("name", str).execute();
            String body = response.body();//返回的信息都存在response
            JSONObject result = JSONObject.parseObject(body);//返回的json
            if (result.get("data") == null) {
                event.getSubject().sendMessage("找不到结果!");
            } else {
                JSONArray data = result.getJSONArray("data");
                MessageChainBuilder messages = new MessageChainBuilder();
                for (Object o : data) {
                    JSONObject jsonObject = JSONObject.parseObject(o.toString());
                    String content = jsonObject.getString("content");
                    if ("file".equals(jsonObject.get("type"))) {
                        InputStream in = HttpUtils.getInputStreamByUrl(imgUrl + content);
                        ExternalResource externalResource = ExternalResource.create(Objects.requireNonNull(in));
                        Image image = event.getSubject().uploadImage(externalResource);
                        messages.append(image);
                        messages.append(System.getProperty("line.separator"));
                    }
                    if ("plain".equals(jsonObject.get("type"))) {
                        messages.append(content);
                        messages.append(System.getProperty("line.separator"));
                    }
                }
                event.getSubject().sendMessage(messages.build());
            }
        } catch (Exception e) {
            System.out.println(e);
            event.getSubject().sendMessage("出错啦");
        }
    }

}
