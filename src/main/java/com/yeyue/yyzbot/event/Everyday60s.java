package com.yeyue.yyzbot.event;

import com.yeyue.yyzbot.utils.HttpUtils;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.utils.ExternalResource;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.Objects;

@Component
public class Everyday60s {

    public void getEveryday60s(MessageEvent event) {
        try {
            InputStream in = HttpUtils.getInputStreamByUrl("https://api.qqsuu.cn/api/dm-60s");
            ExternalResource externalResource = ExternalResource.create(Objects.requireNonNull(in));
            Image image = event.getSubject().uploadImage(externalResource);
            event.getSubject().sendMessage(image);

            externalResource.close();
            in.close();
        } catch (Exception e) {
            event.getSubject().sendMessage(e.getMessage());
        }
    }
}
