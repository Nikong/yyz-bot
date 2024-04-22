package com.yeyue.yyzbot.event;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yeyue.yyzbot.enums.TypeEnum;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.SingleMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class SauceNAOSearcher {
    @Value("${userAccount.sauceNAO.apiKey}")
    private String apiKey;

    public void searchImage(MessageEvent event, Integer type) {
        SingleMessage singleMessage = type.equals(TypeEnum.FRIEND.getValue()) ? event.getMessage().get(2) : event.getMessage().get(3);
        Image image = (Image) singleMessage;
        String url = Image.queryUrl(image);
        event.getSubject().sendMessage(search(url, 5000).build());
    }

    public MessageChainBuilder search(String url, int timeout) {
        MessageChainBuilder messages = new MessageChainBuilder();
        try {
            StringBuilder sb = new StringBuilder("https://saucenao.com/search.php?");
            sb.append("api_key=" + apiKey + "&db=999&output_type=2&numres=1&db=999&url=");
            sb.append(URLEncoder.encode(url, StandardCharsets.UTF_8));
            URL u = new URL(sb.toString());
            HttpURLConnection c = (HttpURLConnection) u.openConnection();
            c.setRequestMethod("GET");
            c.setUseCaches(false);
            c.setAllowUserInteraction(false);
            c.setConnectTimeout(timeout);
            c.setReadTimeout(timeout);
            c.connect();

            int status = c.getResponseCode();

            switch (status) {
                case 200:
                case 201:
                    BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream()));
                    sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line).append("\n");
                    }
                    br.close();
                    c.disconnect();

                    JSONArray results = JSONObject.parseObject(sb.toString()).getJSONArray("results");
                    JSONObject data = new JSONObject();
                    JSONObject header = new JSONObject();
                    if (results.size() > 0) {
                        JSONObject first = results.getJSONObject(0);
                        data = first.getJSONObject("data");
                        header = first.getJSONObject("header");
                    }

                    messages.append("结果为：");
                    messages.append(System.getProperty("line.separator"));
                    messages.append("相似度" + "------->").append(String.valueOf(header.get("similarity"))).append("%");
                    data.forEach((key, value) -> {
                        messages.append(System.getProperty("line.separator"));
                        if (key.equals("ext_urls")) {
                            value = value.toString().replace("\\", "");
                        }
                        messages.append(key).append("------->").append(unicodeDecode(value.toString()));
                    });

                    return messages;
            }
            return messages.append("失败惹！");
        } catch (Exception e) {
            return messages.append(e.getMessage());
        }
    }


    public String unicodeDecode(String string) {
        Pattern pattern = Pattern.compile("(\\\\u(\\p{XDigit}{4}))");
        Matcher matcher = pattern.matcher(string);
        char ch;
        while (matcher.find()) {
            ch = (char) Integer.parseInt(matcher.group(2), 16);
            string = string.replace(matcher.group(1), ch + "");
        }
        return string;
    }
}
