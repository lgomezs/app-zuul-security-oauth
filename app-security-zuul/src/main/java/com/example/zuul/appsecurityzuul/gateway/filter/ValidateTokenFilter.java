package com.example.zuul.appsecurityzuul.gateway.filter;

import com.example.zuul.appsecurityzuul.gateway.client.OAuthClient;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class ValidateTokenFilter extends ZuulFilter {
    private static final String EMPTY_STRING = "";
    private static final String BEARER = "Bearer";
    private static final String AUTHORIZATION = "authorization";
    @Value("${security.checktoken.enabled:false}")
    private boolean enable;
    private final OAuthClient oAuthClient;


    public ValidateTokenFilter(OAuthClient oAuthClient) {
        this.oAuthClient = oAuthClient;
    }


    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {

        if (!enable) {
            log.info("[ValidateTokenFilter] ignore validation token ...");
            return null;
        }

        log.info("[ValidateTokenFilter] validating ...");

        final RequestContext currentContext = RequestContext.getCurrentContext();
        final String authorization = currentContext.getRequest().getHeader(AUTHORIZATION);
        Boolean isValidToken;
        Integer statusCode;

        if (authorization != null && !authorization.isEmpty() && authorization.contains(BEARER)) {
            String token = authorization.replaceAll(BEARER, EMPTY_STRING).trim();
            Map<String, String> form = new HashMap<>();
            form.put("token", token);
            try {
                oAuthClient.checkToken(form);
                isValidToken = true;
                statusCode = null;
                log.info("[ValidateTokenFilter] [checkToken] token OK");
            } catch (FeignException e) {
                log.error("[ValidateTokenFilter] [checkToken] exception", e);
                isValidToken = false;
                if (e.status() == HttpStatus.BAD_REQUEST.value()) {
                    statusCode = HttpStatus.UNAUTHORIZED.value();
                } else {
                    statusCode = e.status();
                }
            }
        } else {
            log.info("[ValidateTokenFilter] not exists token");
            isValidToken = false;
            statusCode = HttpStatus.UNAUTHORIZED.value();
        }

        if (!isValidToken) {
            currentContext.setSendZuulResponse(isValidToken);
            currentContext.setResponseStatusCode(statusCode);
        }
        return null;
    }
}
