package com.dataclouds.domain.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.dataclouds.adapter.output.dfs.IFileSystemService;
import com.dataclouds.adapter.output.repository.DatasetTreeRespository;
import com.dataclouds.domain.model.DatasetDir;
import com.dataclouds.domain.model.DatasetFile;
import com.dataclouds.domain.model.DatasetTree;
import com.dataclouds.domain.service.IDatasetService;
import com.dataclouds.exceptions.DatasetTreeNotExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Random;

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
        tree.setId(new Random().nextLong());
        tree.setCreateTime(new Date());
        tree.setUpdateTime(new Date());
        datasetTreeRespository.save(tree);
        return tree.getId();
    }

    @Override
    public void addDir(Long id, String path, String name) {
        DatasetTree tree = datasetTreeRespository.findById(id)
                .orElseThrow(() -> new DatasetTreeNotExistsException(id));
        DatasetDir dir = tree.addDir(path, name);
        dir.setId(new Random().nextLong());
        dir.setCreateTime(new Date());
        dir.setUpdateTime(new Date());
        datasetTreeRespository.save(tree);
    }

    @Override
    public void addFile(Long id, String path, String name) {
        DatasetTree tree = datasetTreeRespository.findById(id)
                .orElseThrow(() -> new DatasetTreeNotExistsException(id));
        DatasetFile file = tree.addFile(path, name);
        file.setId(new Random().nextLong());
        file.setCreateTime(new Date());
        file.setUpdateTime(new Date());
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
        file.setUpdateTime(new Date());
        datasetTreeRespository.save(tree);
    }

    @Override
    public List<JSONObject> list(Long id, String path) {
        DatasetTree tree = datasetTreeRespository.findById(id)
                .orElseThrow(() -> new DatasetTreeNotExistsException(id));
        return tree.list(path);
    }
}
