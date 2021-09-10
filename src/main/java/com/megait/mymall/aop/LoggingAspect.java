package com.megait.mymall.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Map;

@Component
@Slf4j
@Aspect
public class LoggingAspect {

    // 서비스패키지의 모든 클래스/메서드들 실행시 로그 작성할랭

    @Around("within(com.megait.mymall.service..*)")
    public Object loggin(ProceedingJoinPoint pjp) throws Throwable {
        String params = getRequestParams(); // 리퀘스트 객체를 받아서 함..아래에 우리가 만든 메서드임

        Long start = System.currentTimeMillis();
        Signature requestSignature = pjp.getSignature();
        log.info("request : {} ({}) = {}", requestSignature.getDeclaringTypeName(), requestSignature.getName(), params);

        Object result = pjp.proceed();

        Long end = System.currentTimeMillis();

        Signature responseSignature = pjp.getSignature();
        log.info("response : {} ({}) = {} ({}ms)",
                responseSignature.getDeclaringTypeName(), requestSignature.getName(), result, end - start);

        return result;

    }

    private String getRequestParams() {
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();

        if(attributes == null){
            return null;
        }

        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();

        Map<String, String[]> parameterMap = request.getParameterMap();
        final StringBuilder stringBuilder = new StringBuilder();
        parameterMap.forEach((k, v) -> {
            stringBuilder.append("파라미터 : ").append(k).append(", 값 : ").append(Arrays.toString(v)).append("\n");
        });
        return stringBuilder.toString();

    }

}
