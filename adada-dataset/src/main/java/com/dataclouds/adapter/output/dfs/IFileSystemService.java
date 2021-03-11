package com.dataclouds.adapter.output.dfs;

import java.io.InputStream;

/**
 * @Author: zfl
 * @Date: 2021/3/4 11:50
 * @Version: 1.0.0
 */
public interface IFileSystemService {

    String upload(String fileName, InputStream inputStream);

    String getUrl(String fsPath);
}
