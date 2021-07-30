package com.cyz.config;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author cyz
 * @date 2021/7/30 0030 12:45
 */
@Component
public class MyLogGatewayFilter implements GlobalFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String uname = exchange.getRequest().getQueryParams().getFirst("uname");
        if (uname==null){ //判断是否有uname参数
            exchange.getResponse().setStatusCode(HttpStatus.NOT_ACCEPTABLE);
            System.out.println("没有携带uname");
            return exchange.getResponse().setComplete();
        }
        System.out.println("携带uname");
        return chain.filter(exchange);
    }
//下面这个是顺序，0代表优先级，范围int  -2147483648  -- 2147483647;
    @Override
    public int getOrder() {
        return 0;
    }
}
