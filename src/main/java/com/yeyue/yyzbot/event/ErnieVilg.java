package com.yeyue.yyzbot.event;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yeyue.yyzbot.constants.ErnieVilgEnum;
import com.yeyue.yyzbot.utils.HttpUtils;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.utils.ExternalResource;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Component
public class ErnieVilg {

    public void getDraw(MessageEvent event, Integer type){
        String[] command = event.getMessage().serializeToMiraiCode().split("\\s+");
        String style = type == 1? command[1] : command[2];
        String text = type == 1? command[2] : command[3];

        Map<String,Object> darwMap = new HashMap<>();
        darwMap.put("style", ErnieVilgEnum.getEnumByOrder(style).getValue());
        darwMap.put("text",text);
        darwMap.put("access_token",getToken());

        HttpResponse drawResponse = HttpRequest
                .post("https://wenxin.baidu.com/moduleApi/portal/api/rest/1.0/ernievilg/v1/txt2img")
                .form(darwMap).execute();
        String drawBody=drawResponse.body();//返回的信息都存在response
        JSONObject drawJson = JSONObject.parseObject(drawBody);

        if(drawJson.getInteger("code")==0) {

            event.getSubject().sendMessage("AI作画中...");
            try {
                Thread.sleep(30000l);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Map<String, Object> imgMap = new HashMap<>();
            imgMap.put("taskId", drawJson.getJSONObject("data").getInteger("taskId"));
            while (true) {
                imgMap.put("access_token", getToken());
                HttpResponse imgResponse = HttpRequest
                        .post("https://wenxin.baidu.com/moduleApi/portal/api/rest/1.0/ernievilg/v1/getImg")
                        .form(imgMap).execute();
                String imgBody = imgResponse.body();

                System.out.println(imgBody);

                JSONObject imgJson = JSONObject.parseObject(imgBody);
                JSONObject imgData = imgJson.getJSONObject("data");
                if (imgData.getInteger("status") == 1) {
                    JSONArray imgArray = imgData.getJSONArray("imgUrls");
                    if (imgArray.size() > 0) {
                        JSONObject img = imgArray.getJSONObject(0);
                        String url = img.getString("image");
                        try {
                            InputStream in = HttpUtils.getInputStreamByUrl(url);
                            ExternalResource externalResource = ExternalResource.create(in);
                            Image image = event.getSubject().uploadImage(externalResource);
                            event.getSubject().sendMessage(image);

                            externalResource.close();
                            in.close();

                        } catch (Exception e) {
                            event.getSubject().sendMessage(e.getMessage());
                        }
                    }
                    break;
                }

                try {
                    Thread.sleep(10000l);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }else {
            event.getSubject().sendMessage(drawJson.getString("msg"));
        }


    }


    private static String getToken(){
        HttpResponse tokenResponse = HttpRequest
                .post("https://wenxin.baidu.com/moduleApi/portal/api/oauth/token?grant_type=client_credentials&client_id=你的AK&client_secret=你的SK")
                .header("Content-Type","application/x-www-form-urlencoded").execute();
        String tokenBody=tokenResponse.body();//返回的信息都存在response
        JSONObject tokenJson = JSONObject.parseObject(tokenBody);
        return tokenJson.getString("data");
    }

}
