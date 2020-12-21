package com.aurthor.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author: Aurthor King
 * @Version: v1.0
 * @Description:
 */
@Configuration
public class OauthFeignConfig implements RequestInterceptor {

    /**
     * 自己设置feign拦截器，在请求的时候设置token传递
     * （这是有前台请求由token 的时候，但是还有一种，例如内部直接调用，或者mq里面去调用，就没有token了）
     * 所以我们要搞一个全局的token，在前台没有token 的时候 带上这个token
     *
     * @param requestTemplate
     */
    @Override
    public void apply(RequestTemplate requestTemplate) {
        //得到请求
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (requestAttributes != null) {
            //说明是前端请求，我们做token的传递
            HttpServletRequest request = requestAttributes.getRequest();
            String authorization = request.getHeader("Authorization");
            if (!StringUtils.isEmpty(authorization)) {
                //如果请求头有token，就带上
                requestTemplate.header("Authorization", request.getHeader("Authorization"));
            } else {
                //如果是支付宝的回调，也是有请求的 但是没有token
                requestTemplate.header("Authorization", "bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJzY29wZSI6WyJ3cml0ZSIsInJlYWQiXSwiZXhwIjozNzQ4NjUzMzQ1LCJqdGkiOiJiNzliMDc4YS1lMGQzLTRjOTAtYTY2Ni01YmRkZjcyOWM1OWQiLCJjbGllbnRfaWQiOiJzeHQifQ.kWHxG0oNCJICpeAHn_WWCAaJL4dzR8FDgt1De_oM31iHMW29JK3g1BBQZIbJJ5H1FCbawBAH3sHzGKY2VeEiKxvud-1f-W9_vNv6k5wq9wpG_R8CRK-w3f3t_B6QkKnmv4GtdVhPInjLS3A9hM5zQeTCURmZEggHXifAIPYfaffqrzJcrpXBAkvhoCywFQUp3XvfjCU86_Q1TGkMkjQp9MZ4xnyCn2gP7H06-sAaphMMoz15nB-23oHLjKz_hgbdLkt3QW63TIkwt26o1OChCh9FjG6P9EYkVXTCXUscPL4hEvXrHUmxKCsCWM6WEkl_Dn2wPVOya5CFfZ3WkS009w");
            }
        } else {
            //如果请求为空，说明是内部自己调用的，我们设置自己的永久token，这个token还需要放在redis里面
            //我们用请求的方式拿到这个永久的token
            requestTemplate.header("Authorization", "bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJzY29wZSI6WyJ3cml0ZSIsInJlYWQiXSwiZXhwIjozNzQ4NjUzMzQ1LCJqdGkiOiJiNzliMDc4YS1lMGQzLTRjOTAtYTY2Ni01YmRkZjcyOWM1OWQiLCJjbGllbnRfaWQiOiJzeHQifQ.kWHxG0oNCJICpeAHn_WWCAaJL4dzR8FDgt1De_oM31iHMW29JK3g1BBQZIbJJ5H1FCbawBAH3sHzGKY2VeEiKxvud-1f-W9_vNv6k5wq9wpG_R8CRK-w3f3t_B6QkKnmv4GtdVhPInjLS3A9hM5zQeTCURmZEggHXifAIPYfaffqrzJcrpXBAkvhoCywFQUp3XvfjCU86_Q1TGkMkjQp9MZ4xnyCn2gP7H06-sAaphMMoz15nB-23oHLjKz_hgbdLkt3QW63TIkwt26o1OChCh9FjG6P9EYkVXTCXUscPL4hEvXrHUmxKCsCWM6WEkl_Dn2wPVOya5CFfZ3WkS009w");
        }
    }
}
