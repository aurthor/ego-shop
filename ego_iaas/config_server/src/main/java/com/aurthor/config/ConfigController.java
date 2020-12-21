package com.aurthor.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @author: Aurthor King
 * @Version: v1.0
 * @Description: 动态刷新配置
 */
@RestController
public class ConfigController {

    /**
     * 端口号，实现负载均衡
     */
    @Value("${server.port}")
    private Integer port;

    @Autowired
    private RestTemplate restTemplate;

    /**
     * 钩子函数会触发我们这个接口，发请求刷新所有的配置文件
     *
     * @return
     */
    @PostMapping("bus-refresh")
    public ResponseEntity<String> refresh() {
        //组装url
        String url = "http://localhost:" + port + "/actuator/bus-refresh";
        //创建请求头
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("content-type", "application/json");
        HttpEntity<String> stringHttpEntity = new HttpEntity<>(httpHeaders);
        //发post请求
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, stringHttpEntity, String.class);
        if (responseEntity.getStatusCode().equals(HttpStatus.NO_CONTENT)) {
            return ResponseEntity.ok("刷新配置文件成功");
        }
        return ResponseEntity.badRequest().body("刷新失败");
    }
}
