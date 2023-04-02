package com.juzi.flymsg.service.impl;

import org.junit.jupiter.api.Test;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;

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
}
