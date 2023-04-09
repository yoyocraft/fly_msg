package com.juzi.flymsg.aop;

import cn.hutool.core.lang.UUID;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 请求响应日志 - AOP实现
 *
 * @author codejuzi
 * @CreateTime 2023/4/9
 */
@Slf4j
@Aspect
@Component
public class LogInterceptor {

    /**
     * 执行拦截，针对controller包下所有的方法
     *
     * @param joinPoint 切点
     * @return 拦截方法结果
     */
    @Around(value = "execution(* com.juzi.flymsg.controller.*.*(..))")
    public Object doInterceptor(ProceedingJoinPoint joinPoint) throws Throwable {
        // 开始计时
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        // 获取请求路径 (uri) 和 ip
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        String uri = request.getRequestURI();
        String ip = request.getRemoteHost();
        // 生成唯一请求ID
        String requestId = UUID.randomUUID().toString();
        // 获取请求参数
        Object[] args = joinPoint.getArgs();
        String reqParams = "[" + StringUtils.join(args, ",") + "]";
        // 输出请求日志
        log.info("request start, id: {}, path: {}, ip: {}, args: {}", requestId, uri, ip, reqParams);
        // 执行原方法
        Object result = joinPoint.proceed(args);
        // 停止计时
        stopWatch.stop();
        long totalTimeMillis = stopWatch.getTotalTimeMillis();
        // 输出响应日志
        log.info("request end, id: {}. cost {}ms", requestId, totalTimeMillis);
        // 返回
        return result;
    }
}
