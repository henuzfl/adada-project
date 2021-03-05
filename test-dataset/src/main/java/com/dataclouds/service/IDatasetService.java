package com.dataclouds.service;

import cn.hutool.json.JSONObject;

import java.io.InputStream;
import java.util.List;

/**
 * @Author: zfl
 * @Date: 2021/3/3 13:48
 * @Version: 1.0.0
 */
public interface IDatasetService {

    void addDir(String path, String dir);

    void addFile(String path, String fileName);

    void delete(String path);

    void rename(String path, String name);

    void move(String originalPath, String targetPath);

    void upload(String path, InputStream inputStream);

    List<JSONObject> list(String path);
}
