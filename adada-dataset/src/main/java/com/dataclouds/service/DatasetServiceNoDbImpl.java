package com.dataclouds.service;

import com.alibaba.fastjson.JSONObject;
import com.dataclouds.adapter.output.dfs.IFileSystemService;
import com.dataclouds.adapter.output.repository.DatasetTreeRespository;
import com.dataclouds.exceptions.DatasetTreeNotExistsException;
import com.dataclouds.model.DatasetFile;
import com.dataclouds.model.DatasetTree;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;

/**
 * @Author: zfl
 * @Date: 2021/3/5 15:46
 * @Version: 1.0.0
 */
@Service("datasetServiceNoDb")
public class DatasetServiceNoDbImpl implements INewDatasetService {

    @Autowired
    private IFileSystemService fileSystemService;

    @Autowired
    private DatasetTreeRespository datasetTreeRespository;

    @Override
    public String create() {
        DatasetTree tree = new DatasetTree();
        datasetTreeRespository.save(tree);
        return tree.getId();
    }

    @Override
    public void addDir(String id, String path, String dir) {
        DatasetTree tree = datasetTreeRespository.findById(id)
                .orElseThrow(() -> new DatasetTreeNotExistsException(id));
        tree.addDir(path, dir);
        datasetTreeRespository.save(tree);
    }

    @Override
    public void addFile(String id, String path, String fileName) {
        DatasetTree tree = datasetTreeRespository.findById(id)
                .orElseThrow(() -> new DatasetTreeNotExistsException(id));
        tree.addFile(path, fileName);
        datasetTreeRespository.save(tree);
    }

    @Override
    public void delete(String id, String path) {

    }

    @Override
    public void rename(String id, String path, String name) {

    }

    @Override
    public void move(String id, String originalPath, String targetPath) {

    }

    @Override
    public void upload(String id, String path, InputStream inputStream) {
        DatasetTree tree = datasetTreeRespository.findById(id)
                .orElseThrow(() -> new DatasetTreeNotExistsException(id));
        DatasetFile file = tree.findFile(path);
        String dfsPath = fileSystemService.upload(file.getName(), inputStream);
        tree.uploaded(file, dfsPath);
        datasetTreeRespository.save(tree);
    }

    @Override
    public List<JSONObject> list(String id, String path) {
        DatasetTree tree = datasetTreeRespository.findById(id)
                .orElseThrow(() -> new DatasetTreeNotExistsException(id));
        return tree.list(path);
    }
}
