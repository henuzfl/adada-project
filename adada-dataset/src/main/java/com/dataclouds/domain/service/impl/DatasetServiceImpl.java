package com.dataclouds.domain.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.dataclouds.adapter.output.dfs.IFileSystemService;
import com.dataclouds.adapter.output.repository.DatasetDirRespository;
import com.dataclouds.adapter.output.repository.DatasetFileRespository;
import com.dataclouds.adapter.output.repository.DatasetTreeDbRespository;
import com.dataclouds.domain.model.DatasetDir;
import com.dataclouds.domain.model.DatasetFile;
import com.dataclouds.domain.model.DatasetTree;
import com.dataclouds.domain.service.IDatasetService;
import com.dataclouds.event.publisher.IDomainEventPublisher;
import com.dataclouds.exceptions.DatasetTreeNotExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;

/**
 * @Author: zfl
 * @Date: 2021/3/3 13:53
 * @Version: 1.0.0
 */
@Service("datasetService")
public class DatasetServiceImpl implements IDatasetService {

    @Autowired
    private DatasetDirRespository datasetDirRespository;

    @Autowired
    private DatasetFileRespository datasetFileRespository;

    @Autowired
    private DatasetTreeDbRespository datasetTreeRespository;

    @Autowired
    private IFileSystemService fileSystemService;

    @Autowired
    private IDomainEventPublisher domainEventPublisher;

    @Override
    public Long create() {
        DatasetTree tree = new DatasetTree();
        datasetTreeRespository.save(tree);
        return tree.getId();
    }

    @Override
    public void addDir(Long id, String path, String name) {
        DatasetTree tree =
                datasetTreeRespository.findById(id)
                        .orElseThrow(() -> new DatasetTreeNotExistsException(id));
        DatasetDir dir = tree.addDir(path, name);
        datasetDirRespository.save(dir);
    }

    @Override
    public void addFile(Long id, String path, String name) {
        DatasetTree tree =
                datasetTreeRespository.findById(Long.valueOf(id))
                        .orElseThrow(() -> new DatasetTreeNotExistsException(id));
        DatasetFile file = tree.addFile(path, name);
        datasetFileRespository.save(file);
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
        DatasetTree tree =
                datasetTreeRespository.findById(Long.valueOf(id))
                        .orElseThrow(() -> new DatasetTreeNotExistsException(id));
        DatasetFile file = tree.findFile(path);
        String dfsPath = fileSystemService.upload(file.getName(),
                inputStream);
        tree.uploaded(file, dfsPath);
        datasetFileRespository.save(file);
    }

    @Override
    public List<JSONObject> list(Long id, String path) {
        DatasetTree tree =
                datasetTreeRespository.findById(Long.valueOf(id))
                        .orElseThrow(() -> new DatasetTreeNotExistsException(id));
        return tree.list(path);
    }
}
