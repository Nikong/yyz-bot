package com.yeyue.yyzbot.event;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yeyue.yyzbot.enums.TypeEnum;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.SingleMessage;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Component
public class WhatAnime {

    public void searchAnime(MessageEvent event, Integer type) {
        SingleMessage singleMessage = type.equals(TypeEnum.FRIEND.getValue()) ? event.getMessage().get(2) : event.getMessage().get(3);
        Image image = (Image) singleMessage;
        String url = Image.queryUrl(image);
        System.out.println(url);
        Map imageMap = new HashMap();

        imageMap.put("image", getFile(url));//参数是文件类型
        //都可以封装近map里面一起发送 这种方法有点类似表单提交后台的方式
        HttpResponse response = HttpRequest.post("https://api.trace.moe/search?cutBorders&").form(imageMap).execute();
        String body = response.body();//返回的信息都存在response
        JSONObject jsonObject = JSONObject.parseObject(body);//返回的json


        //上面只找了anime的id，还需要根据id找对应的anime信息
        JSONArray result = JSON.parseObject(JSON.toJSONString(jsonObject.get("result")), JSONArray.class);
        Set animeIdSet = new HashSet();
        result.forEach(e -> {
            JSONObject json = (JSONObject) e;
            animeIdSet.add(json.get("anilist"));
        });
        Map animeMap = new HashMap();
        //query,我也没看是啥，接口传了我也跟着传
        animeMap.put("query", "query ($ids: [Int]) {\n" +
                "            Page(page: 1, perPage: 50) {\n" +
                "              media(id_in: $ids, type: ANIME) {\n" +
                "                id\n" +
                "                title {\n" +
                "                  native\n" +
                "                  romaji\n" +
                "                  english\n" +
                "                }\n" +
                "                type\n" +
                "                format\n" +
                "                status\n" +
                "                startDate {\n" +
                "                  year\n" +
                "                  month\n" +
                "                  day\n" +
                "                }\n" +
                "                endDate {\n" +
                "                  year\n" +
                "                  month\n" +
                "                  day\n" +
                "                }\n" +
                "                season\n" +
                "                episodes\n" +
                "                duration\n" +
                "                source\n" +
                "                coverImage {\n" +
                "                  large\n" +
                "                  medium\n" +
                "                }\n" +
                "                bannerImage\n" +
                "                genres\n" +
                "                synonyms\n" +
                "                studios {\n" +
                "                  edges {\n" +
                "                    isMain\n" +
                "                    node {\n" +
                "                      id\n" +
                "                      name\n" +
                "                      siteUrl\n" +
                "                    }\n" +
                "                  }\n" +
                "                }\n" +
                "                isAdult\n" +
                "                externalLinks {\n" +
                "                  id\n" +
                "                  url\n" +
                "                  site\n" +
                "                }\n" +
                "                siteUrl\n" +
                "              }\n" +
                "            }\n" +
                "          }\n" +
                "          ");
        JSONObject json1 = new JSONObject();
        //animeId
        json1.put("ids", animeIdSet);
        animeMap.put("variables", json1.toJSONString());

        HttpResponse animeResponse = HttpRequest.post("https://trace.moe/anilist/").form(animeMap).execute();
        String animeBody = animeResponse.body();//返回的信息都存在response
        JSONObject data = JSON.parseObject(JSONObject.parseObject(animeBody).get("data").toString(), JSONObject.class);
        JSONObject page = JSON.parseObject(data.get("Page").toString(), JSONObject.class);
        JSONArray media = JSON.parseObject(page.get("media").toString(), JSONArray.class);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("可能是:");
        for (Object o : media) {
            JSONObject jsonObject1 = JSONObject.parseObject(o.toString());
            stringBuilder.append(System.getProperty("line.separator"));
            stringBuilder.append(JSONObject.parseObject(jsonObject1.get("title").toString()).get("chinese"));
        }

        event.getSubject().sendMessage(stringBuilder.toString());
    }


    //获取图片文件
    private File getFile(String url) {
        //对本地文件命名
        String[] urlSplit = url.split("/");
        String fileName = urlSplit[urlSplit.length - 2];
        File file = null;

        URL urlfile;
        try {
            // 创建一个临时路径
            file = File.createTempFile("file", fileName);
            //下载
            urlfile = new URL(url);

            try (InputStream inStream = urlfile.openStream();
                 OutputStream os = new FileOutputStream(file)) {
                int bytesRead;
                byte[] buffer = new byte[8192];
                while ((bytesRead = inStream.read(buffer, 0, 8192)) != -1) {
                    os.write(buffer, 0, bytesRead);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

}
