package com.cyz.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author cyz
 * @date 2021/7/28 0028 21:03
 */
@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator getMyGateway(RouteLocatorBuilder routeLocatorBuilder){
        //这边获得的routes就类似于配置文件那边的routes
        RouteLocatorBuilder.Builder routes = routeLocatorBuilder.routes();
        //https://news.baidu.com/guonei 访问国内新闻试试
         //这里的id和yml中的id类似 patterns类似于yml中的predicates断言  uri就类似于yml中的uri
        //整体就是访问9527的guonei，就会访问https://news.baidu.com/guonei
        routes.route("com_cyz",r->r.path("/guonei").uri("https://news.baidu.com/guonei")).build();

        return routes.build();
    }
}
