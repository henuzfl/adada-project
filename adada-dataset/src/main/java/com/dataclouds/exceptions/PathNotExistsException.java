package com.dataclouds.exceptions;

/**
 * @Author: zfl
 * @Date: 2021/3/3 14:55
 * @Version: 1.0.0
 */
public class PathNotExistsException extends RuntimeException {

    public PathNotExistsException(String path) {
        super(String.format("路径：%s不存在！", path));
    }
}
