package com.dataclouds.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dataclouds.adapter.output.dfs.IFileSystemService;
import com.dataclouds.model.DatasetFile;
import com.dataclouds.model.DatasetTree;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.*;
import java.util.List;

/**
 * @Author: zfl
 * @Date: 2021/3/5 15:46
 * @Version: 1.0.0
 */
@Service("datasetServiceNoDb")
public class DatasetServiceNoDbImpl implements IDatasetService {

    @Autowired
    private IFileSystemService fileSystemService;

    private DatasetTree tree;

    private File file;

    @PostConstruct
    public void init() throws IOException {
        file = new ClassPathResource("tree.json").getFile();
        try (FileInputStream fis = new FileInputStream(file)) {
            String content = IOUtils.toString(fis, "UTF-8");
            if (StringUtils.isNotBlank(content)) {
                tree = new DatasetTree(JSON.parseObject(content));
            }
        }
    }

    @Override
    public void addDir(String path, String dir) {
        tree.addDir(path, dir);
        persistence();
    }

    @Override
    public void addFile(String path, String fileName) {
        tree.addFile(path, fileName);
        persistence();
    }

    @Override
    public void delete(String path) {

    }

    @Override
    public void rename(String path, String name) {

    }

    @Override
    public void move(String originalPath, String targetPath) {

    }

    @Override
    public void upload(String path, InputStream inputStream) {
        DatasetFile file = tree.findFile(path);
        String dfsPath = fileSystemService.upload(file.getName(), inputStream);
        tree.uploaded(file, dfsPath);
        persistence();
    }

    @Override
    public List<JSONObject> list(String path) {
        return tree.list(path);
    }

    private void persistence() {
        JSONObject json = this.tree.encode();
        try (OutputStream os = new FileOutputStream(file)) {
            IOUtils.write(json.toJSONString(), os, "utf-8");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
