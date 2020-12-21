package com.aurthor.constant;

import java.util.Arrays;
import java.util.List;

/**
 * @author: Aurthor King
 * @Version: v1.0
 * @Description: 常量信息
 */
public interface GatewayConstant {

    public static final String OAUTH_PREFIX = "oauth:jwt";

    public static final String AUTHORIZATION = "Authorization";

    public static final List<String> ALLOW_PATH = Arrays.asList("/oauth/token", "/auth-server/oauth/token");
}
