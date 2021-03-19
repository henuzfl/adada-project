package com.dataclouds.domain.service;


import com.alibaba.fastjson.JSONObject;

import java.io.InputStream;
import java.util.List;

/**
 * @Author: zfl
 * @Date: 2021/3/3 13:48
 * @Version: 1.0.0
 */
public interface IDatasetService {

    Long create();

    void addDir(Long id, String path, String name);

    void addFile(Long id, String path, String name);

    void delete(Long id, String path);

    void rename(Long id, String path, String name);

    void move(Long id, String originalPath, String targetPath);

    void upload(Long id, String path, InputStream inputStream);

    List<JSONObject> list(Long id, String path);
}
