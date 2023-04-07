package com.juzi.flymsg.service.impl;

import cn.hutool.json.JSONUtil;
import com.juzi.flymsg.model.dto.user.UserRegistryRequest;
import org.junit.jupiter.api.Test;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

/**
 * @author codejuzi
 * @CreateTime 2023/4/2
 */
public class DemoTest {


    @Test
    public void test() {
        String salt = "codejuzi";
        String userPassword = "12345678";
        String s = DigestUtils.md5DigestAsHex((salt + userPassword).getBytes(StandardCharsets.UTF_8));
        String s2 = DigestUtils.md5DigestAsHex((salt + userPassword).getBytes(StandardCharsets.UTF_8));
        System.out.println("s2 = " + s2);
        System.out.println("s = " + s);
    }

    @Test
    public void testJson() {
        String json = "{\n" +
                "  \"userAccount\": \"codejuzi9\",\n" +
                "  \"userPassword\": \"12345678\",\n" +
                "  \"checkedPassword\": \"12345678\"\n" +
                "}";
        UserRegistryRequest userRegistryRequest = JSONUtil.toBean(json, UserRegistryRequest.class);
        System.out.println("userRegistryRequest = " + userRegistryRequest);
    }

    @Test
    public void testListJson() {
        List<String> tagList = Arrays.asList("Java", "Python");
        String jsonStr = JSONUtil.toJsonStr(tagList);
        System.out.println("jsonStr = " + jsonStr);
    }
}
