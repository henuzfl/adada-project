package com.dataclouds.exceptions;

/**
 * @Author: zfl
 * @Date: 2021/3/3 14:59
 * @Version: 1.0.0
 */
public class NameExistsException extends RuntimeException {

    public NameExistsException(String path, String name) {
        super(String.format("路径：%s下已经存在名称：%s！", path, name));
    }
}
