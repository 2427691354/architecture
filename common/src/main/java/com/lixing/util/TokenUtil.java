package com.lixing.util;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author cc
 * @date 2020/06/05
 **/
@Component
@Data
public class TokenUtil {

    @Value("${token.expire.time}")
    private long tokenExpireTime;

    @Value("${refresh.token.expire.time}")
    private long refreshTokenExpireTime;

    private Map<String , String> map = new HashMap<>(2);

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private Environment environment;


    /**
     * 生成token和refreshToken
     * @param userName
     * @param type
     * @return
     */
    public Map<String, String> getToken(String userName, Integer type){
        //生成refreshToken
        String refreshToken = UUID.randomUUID().toString().replaceAll("-","");
        String prefix = getPre(type);

        //生成token
        String token = this.buildJWT(userName, type);
        String key = SecureUtil.md5(prefix + userName);

        //数据放入redis,使用hash结构保存使用中的token以及用户标识
        stringRedisTemplate.opsForHash().put(key,"token", token);
        stringRedisTemplate.opsForHash().put(key,"refreshToken", refreshToken);

        //设置token的过期时间
        stringRedisTemplate.expire(key,
                refreshTokenExpireTime, TimeUnit.MILLISECONDS);
        map.put("token", token);
        map.put("refreshToken", refreshToken);
        return map;
    }

    /**
     * 刷新token
     * @param userName
     * @param type
     * @param refreshToken
     * @return
     */
    public Map<String, String>  refreshToken(String userName, Integer type, String refreshToken){
        String prefix = getPre(type);
        String key = SecureUtil.md5(prefix + userName);
        String oldRefresh = (String) stringRedisTemplate.opsForHash().get(key, "refreshToken");
        if (StrUtil.isBlank(oldRefresh)){
            map.put("token", "refreshToken过期");
        }else {
            if (!oldRefresh.equals(refreshToken)){
                map.put("token", "refreshToken错误");
            }else {
                String token = buildJWT(userName, type);
                stringRedisTemplate.opsForHash().put(key,"token", token);
                stringRedisTemplate.opsForHash().put(key,"refreshToken", refreshToken);
                stringRedisTemplate.expire(key,
                        refreshTokenExpireTime, TimeUnit.MILLISECONDS);
                map.put("token", token);
                map.put("refreshToken", refreshToken);
            }
        }
        return map;
    }

    /**
     * 删除key
     * @param userName
     * @param type
     */
    public boolean removeToken(String userName, Integer type){
        String prefix = getPre(type);
        String key = SecureUtil.md5(prefix + userName);
        return stringRedisTemplate.delete(key);
    }


    /**
     * 生成jwt: JWT通常由头部(Header)，负载(Payload)，签名(Signature)三个部分组成，中间以.号分隔，其格式为Header.Payload.Signature
     * Header：声明令牌的类型和使用的算法
     * Payload：也称为JWT Claims，包含用户的一些信息
     * Signature：签名
     *      签名格式： HMACSHA256(base64UrlEncode(header) + "." + base64UrlEncode(payload), secret)
     * @param userName 手机号
     * @param type 前缀
     * @return
     */
    public String buildJWT(String userName, Integer type){
        // 生成JWT的时间
        Date now = new Date();
        //  算法
        Algorithm algo = Algorithm.HMAC256(getPre(type));
        String token = JWT.create()
                //签发人
                .withIssuer("userName")
                //签发时间
                .withIssuedAt(now)
                //过期时间
                .withExpiresAt(new Date(now.getTime() + tokenExpireTime))
                //自定义的存放的数据
                .withClaim("userName", userName)
                //签名
                .sign(algo);
        return token;
    }

    /**
     * JWT验证
     * @param token
     * @return userPhone
     */
    public String verifyJWT(String token, Integer type){
        String userName;
        try {
            Algorithm algorithm = Algorithm.HMAC256(getPre(type));
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("userName")
                    .build();
            //  检验token是否正确语句
            DecodedJWT jwt = verifier.verify(token);
            userName = jwt.getClaim("userName").asString();
        } catch (JWTVerificationException e){
            e.printStackTrace();
            return "";
        }
        return userName;
    }

    public String getPre(Integer type){
        return environment.getProperty("jwt.secret."+type);
    }
}

