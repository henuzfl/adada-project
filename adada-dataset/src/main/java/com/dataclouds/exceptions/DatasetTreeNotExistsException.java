package com.dataclouds.exceptions;

/**
 * @Author: zfl
 * @Date: 2021/3/3 14:59
 * @Version: 1.0.0
 */
public class DatasetTreeNotExistsException extends RuntimeException {

    public DatasetTreeNotExistsException(Long id) {
        super(String.format("数据集树id：%s不存在！", id));
    }
}
