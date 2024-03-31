package cn.ecnu.eblog.gateway.config;


import cn.ecnu.eblog.common.constant.JwtClaimsConstant;
import cn.ecnu.eblog.common.context.BaseContext;
import cn.ecnu.eblog.common.utils.JwtUtil;
import cn.ecnu.eblog.gateway.properties.JwtProperties;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Objects;


@Component
@Order(-1)  // 越小优先级越高
@Slf4j
public class Filter implements GlobalFilter {
    @Autowired
    private JwtProperties jwtProperties;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("接收到请求");
        // 如果是登录或者注册，直接放行
        String path = exchange.getRequest().getURI().getPath();
        if ("/user/login".equals(path) || "/user/signup".equals(path)){
            return chain.filter(exchange);
        }
        // 否则进行jwt令牌验证
        try{
            String token = exchange.getRequest().getHeaders().getFirst(jwtProperties.getUserTokenName());
            log.info("jwt校验:{}", token);
            Claims claims = JwtUtil.parseJWT(jwtProperties.getUserSecretKey(), token);
            Long userId = Long.valueOf(claims.get(JwtClaimsConstant.USER_ID).toString());
            if(Boolean.FALSE.equals(redisTemplate.hasKey("token:" + userId)) || !Objects.equals(redisTemplate.opsForValue().get("token:" + userId), token)){
                throw new Exception();
            }
            log.info("当前用户id：{}", userId);
            // 存入当前线程请求头中

            ServerHttpRequest modifiedRequest = exchange.getRequest()
                    .mutate()
                    .header("userId", String.valueOf(userId))
                    .build();

            ServerWebExchange modifiedExchange = exchange.mutate()
                    .request(modifiedRequest)
                    .build();

            //3、通过，放行
            return chain.filter(modifiedExchange);
        } catch (Exception ex) {
            //4、不通过，响应401状态码
            exchange.getResponse().setStatusCode(HttpStatus.valueOf(401));
            return exchange.getResponse().setComplete();
        }
    }
}
