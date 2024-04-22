package com.yeyue.yyzbot;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yeyue.yyzbot.utils.HttpUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
class YyzBotApplicationTests {

    @Test
    void contextLoads() {
        Map map=new HashMap();
        File file=new File("C:\\Users\\YUXI\\Desktop\\111.jpg");
        map.put("image",file);//参数是文件类型
        //都可以封装近map里面一起发送 这种方法有点类似表单提交后台的方式
        HttpResponse response= HttpRequest.post("https://api.trace.moe/search?cutBorders&").form(map).execute();
        String body=response.body();//返回的信息都存在response
        JSONObject jsonObject=JSONObject.parseObject(body);
        //第三方接口返回的是一个布尔 所以取到对应的key转换为对应的类型
        System.out.println(body);
        Boolean datas= JSON.parseObject(JSON.toJSONString(jsonObject.get("data")),Boolean.class);
        if(!datas){
            System.out.println("error!!!!!!!!!!!!!!!!");
        }
    }

}
