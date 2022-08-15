package com.yeyue.yyzbot.constants;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Constants {
    public static final int MAX_SETUTIMES = 10;

    public static void main(String[] args) {
        Map map=new HashMap();
        File file=new File("C:\\Users\\YUXI\\Desktop\\111.jpg");
        map.put("image",file);//参数是文件类型
        //都可以封装近map里面一起发送 这种方法有点类似表单提交后台的方式
        HttpResponse response= HttpRequest.post("https://api.trace.moe/search?cutBorders&").form(map).execute();
        String body=response.body();//返回的信息都存在response
        JSONObject jsonObject=JSONObject.parseObject(body);
        //第三方接口返回的是一个布尔 所以取到对应的key转换为对应的类型
        System.out.println(body);
        JSONArray result= JSON.parseObject(JSON.toJSONString(jsonObject.get("result")),JSONArray.class);
        Set set = new HashSet();
        result.forEach(e ->{
            JSONObject json = (JSONObject) e;
            set.add(json.get("anilist"));
        });

        Map map2 =new HashMap();
        JSONObject json1 = new JSONObject();
        json1.put("ids",set);
        map2.put("variables",json1.toJSONString());
        HttpResponse response2 = HttpRequest.post("https://trace.moe/anilist/").form(map2).execute();
        String body2=response2.body();//返回的信息都存在response
        JSONObject data= JSON.parseObject(JSONObject.parseObject(body2).get("data").toString(),JSONObject.class);
        JSONObject page= JSON.parseObject(data.get("Page").toString(),JSONObject.class);
        JSONArray media= JSON.parseObject(page.get("media").toString(),JSONArray.class);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("可能是:");
        for (Object o:media) {
            JSONObject jsonObject1 = JSONObject.parseObject(o.toString());
            stringBuilder.append(System.getProperty("line.separator"));
            stringBuilder.append(JSONObject.parseObject(jsonObject1.get("title").toString()).get("chinese"));
        }
        System.out.println("最后--------------------->"+stringBuilder);
    }
}
