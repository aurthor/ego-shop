package com.aurthor.utils;

/**
 * @author: Aurthor King
 * @Version: v1.0
 * @Description: FastDFS工具类
 */
public class FastDFSUtil {
    /**
     * 解析fastDFS的路径参数
     *
     * @return
     */
    public static String parseGroup(String fullPath) {
        String[] paths = fullPath.split("/");
        if (paths.length <= 1) {
            throw new RuntimeException("参数错误，无法解析");
        }
        return paths[0];
    }

}
