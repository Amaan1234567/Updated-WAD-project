package com.orders_service;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class UserContextInterceptor implements HandlerInterceptor {

    private static final ThreadLocal<String> userUuid = new ThreadLocal<>();
    private static final ThreadLocal<String> userRole = new ThreadLocal<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String uuid = request.getHeader("X-User-Id");
        String role = request.getHeader("X-User-Role");
        userUuid.set(uuid);
        userRole.set(role);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
            Object handler, Exception ex) {
        userUuid.remove();
        userRole.remove();
    }

    public static String getUserUuid() {
        return userUuid.get();
    }

    public static String getUserRole() {
        return userRole.get();
    }
}
