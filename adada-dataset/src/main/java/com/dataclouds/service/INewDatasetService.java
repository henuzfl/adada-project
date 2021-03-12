package com.dataclouds.service;


import com.alibaba.fastjson.JSONObject;

import java.io.InputStream;
import java.util.List;

/**
 * @Author: zfl
 * @Date: 2021/3/3 13:48
 * @Version: 1.0.0
 */
public interface INewDatasetService {

    String create();

    void addDir(String id, String path, String dir);

    void addFile(String id, String path, String fileName);

    void delete(String id, String path);

    void rename(String id, String path, String name);

    void move(String id, String originalPath, String targetPath);

    void upload(String id, String path, InputStream inputStream);

    List<JSONObject> list(String id,String path);
}
