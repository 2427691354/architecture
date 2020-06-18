package com.lixing;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.lixing.util.TokenUtil;
import org.springframework.core.env.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author cc
 * @date 2020/06/05
 **/
@Component
public class ApiGlobalFilter implements GlobalFilter, Ordered {


    @Autowired
    TokenUtil tokenUtil;

    /**
     * 不进行token校验的请求地址
     */
    @Value("#{'${auth.skip.urls}'.split(',')}")
    public List<String> ignoreUrl;

    @Autowired
    private Environment environment;

    /**
     * 拦截所有的请求头
     * @param exchange
     * @param chain
     * @return
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        String requestUrl = exchange.getRequest().getPath().toString();
        //判断是否为忽略的url请求
        boolean status = CollectionUtil.contains(ignoreUrl, requestUrl);

        if (!status){

            //从请求头中取得token
            String token = exchange.getRequest().getHeaders().getFirst("token");
            //type用于区分不同的端，在做校验token时需要
            String type= exchange.getRequest().getHeaders().getFirst("type");
            ServerHttpResponse response = exchange.getResponse();

            //没有数据
            if (StrUtil.isBlank(token) || StrUtil.isBlank(type)) {
                JSONObject message = new JSONObject();
                message.put("code", "400");
                message.put("message", "鉴权失败，无token或类型");
                byte[] bits = message.toString().getBytes(StandardCharsets.UTF_8);
                DataBuffer buffer = response.bufferFactory().wrap(bits);
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                response.getHeaders().add("Content-Type", "text/json;charset=UTF-8");
                return response.writeWith(Mono.just(buffer));
             //有数据
            }else {
                //校验token
                String userName =tokenUtil.verifyJWT(token ,Integer.parseInt(type));

                if (StrUtil.isEmpty(userName)){
                    JSONObject message = new JSONObject();
                    message.put("message", "token错误");
                    message.put("code", "500");
                    byte[] bits = message.toString().getBytes(StandardCharsets.UTF_8);
                    DataBuffer buffer = response.bufferFactory().wrap(bits);
                    response.setStatusCode(HttpStatus.UNAUTHORIZED);
                    response.getHeaders().add("Content-Type", "text/json;charset=UTF-8");
                    return response.writeWith(Mono.just(buffer));
                }
                //将现在的request，添加当前身份
                ServerHttpRequest mutableReq = exchange.getRequest().mutate().header("Authorization-UserName", userName).build();
                ServerWebExchange mutableExchange = exchange.mutate().request(mutableReq).build();
                return chain.filter(mutableExchange);
            }
        }
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -200;
    }
}

