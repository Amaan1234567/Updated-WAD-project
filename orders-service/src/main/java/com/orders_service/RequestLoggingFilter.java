package com.orders_service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class RequestLoggingFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(RequestLoggingFilter.class);

    @Value("${logging.request-body-enabled:false}")
    private boolean logRequestBody;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        log.info("=== {} {} ===", request.getMethod(), request.getRequestURI());

        // Headers
        Collections.list(request.getHeaderNames())
                .forEach(name -> log.info("Header: {} = {}", name, request.getHeader(name)));

        // Query parameters
        request.getParameterMap().forEach((key, values) -> log.info("Param: {} = {}", key, String.join(",", values)));

        // Body — only if enabled AND method has a body
        if (logRequestBody && request.getMethod().matches("POST|PUT|PATCH")) {
            CachedBodyRequestWrapper wrappedRequest = new CachedBodyRequestWrapper(request);
            String body = new String(wrappedRequest.getCachedBody(), StandardCharsets.UTF_8);
            log.info("Body: {}", body);
            filterChain.doFilter(wrappedRequest, response);
        } else {
            filterChain.doFilter(request, response);
        }
    }
}
