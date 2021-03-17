package com.dataclouds.service;

import com.alibaba.fastjson.JSONObject;
import com.dataclouds.adapter.output.dfs.IFileSystemService;
import com.dataclouds.adapter.output.repository.DatasetTreeRespository;
import com.dataclouds.exceptions.DatasetTreeNotExistsException;
import com.dataclouds.domain.DatasetFile;
import com.dataclouds.domain.DatasetTree;
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
public class DatasetServiceNoDbImpl implements IDatasetService {

    @Autowired
    private IFileSystemService fileSystemService;

    @Autowired
    private DatasetTreeRespository datasetTreeRespository;

    @Override
    public Long create() {
        DatasetTree tree = new DatasetTree();
        datasetTreeRespository.save(tree);
        return tree.getId();
    }

    @Override
    public void addDir(Long id, String path, String name) {
        DatasetTree tree = datasetTreeRespository.findById(id)
                .orElseThrow(() -> new DatasetTreeNotExistsException(id));
        tree.addDir(path, name);
        datasetTreeRespository.save(tree);
    }

    @Override
    public void addFile(Long id, String path, String name) {
        DatasetTree tree = datasetTreeRespository.findById(id)
                .orElseThrow(() -> new DatasetTreeNotExistsException(id));
        tree.addFile(path, name);
        datasetTreeRespository.save(tree);
    }

    @Override
    public void delete(Long id, String path) {

    }

    @Override
    public void rename(Long id, String path, String name) {

    }

    @Override
    public void move(Long id, String originalPath, String targetPath) {

    }

    @Override
    public void upload(Long id, String path, InputStream inputStream) {
        DatasetTree tree = datasetTreeRespository.findById(id)
                .orElseThrow(() -> new DatasetTreeNotExistsException(id));
        DatasetFile file = tree.findFile(path);
        String dfsPath = fileSystemService.upload(file.getName(), inputStream);
        tree.uploaded(file, dfsPath);
        datasetTreeRespository.save(tree);
    }

    @Override
    public List<JSONObject> list(Long id, String path) {
        DatasetTree tree = datasetTreeRespository.findById(id)
                .orElseThrow(() -> new DatasetTreeNotExistsException(id));
        return tree.list(path);
    }
}
