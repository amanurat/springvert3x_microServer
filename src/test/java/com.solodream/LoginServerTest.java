package com.solodream;

import com.alibaba.fastjson.JSON;
import com.solodream.spring.vertx.common.HttpUtils;
import com.solodream.spring.vertx.req.BaseReq;
import com.solodream.spring.vertx.req.client.TokenRequestParam;
import com.solodream.spring.vertx.req.client.UserLoginRequestParam;
import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by young on 15/12/31.
 */
public class LoginServerTest {
    @Test
    public void testLogin() throws Exception {
        List<Header> headers = new ArrayList<Header>();
        headers.add(new BasicHeader("Content-Type", "application/json"));

        BaseReq<UserLoginRequestParam> request = new BaseReq<UserLoginRequestParam>();
        request.setToken("");

        UserLoginRequestParam param = new UserLoginRequestParam();
        param.setDeviceId("123456788");
        param.setUsername("TestAccount24");
        param.setPassword("123456steven");
        param.setSmsCode("12345");

        request.setParam(param);
        String parameter = JSON.toJSONString(request);


        System.out.println(parameter);

        System.out.println(HttpUtils.post("http://127.0.0.1:18080/login", headers, parameter));
    }

    @Test
    public void testGetAccessToken()throws  Exception{
        List<Header> headers = new ArrayList<Header>();
        headers.add(new BasicHeader("Content-Type", "application/json"));

        BaseReq<TokenRequestParam> request = new BaseReq<TokenRequestParam>();
        request.setToken("");

        TokenRequestParam param = new TokenRequestParam();
        param.setRefreshToken("c7c57afdb3d397d6fa8a9189096445a2");

        request.setParam(param);
        String parameter = JSON.toJSONString(request);


        System.out.println(parameter);

        System.out.println(HttpUtils.post("http://127.0.0.1:18080/getAccessToken/", headers, parameter));
    }
}
