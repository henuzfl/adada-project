package com.dataclouds.service;

import com.alibaba.fastjson.JSONObject;
import com.dataclouds.adapter.output.dfs.IFileSystemService;
import com.dataclouds.adapter.output.repository.DatasetDirRespository;
import com.dataclouds.adapter.output.repository.DatasetFileRespository;
import com.dataclouds.adapter.output.repository.DatasetTreeDbRespository;
import com.dataclouds.domain.DatasetDirEntity;
import com.dataclouds.domain.DatasetFileEntity;
import com.dataclouds.domain.DatasetTreeEntity;
import com.dataclouds.domain.event.DatasetTreeCreatedEvent;
import com.dataclouds.event.annotation.DomainEvent;
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
    @DomainEvent(events = DatasetTreeCreatedEvent.class)
    public Long create() {
        DatasetTreeEntity tree = new DatasetTreeEntity();
        datasetTreeRespository.save(tree);
        return tree.getId();
    }

    @Override
    public void addDir(Long id, String path, String name) {
        DatasetTreeEntity tree =
                datasetTreeRespository.findById(id)
                        .orElseThrow(() -> new DatasetTreeNotExistsException(id));
        DatasetDirEntity dir = tree.addDir(path, name);
        datasetDirRespository.save(dir);
    }

    @Override
    public void addFile(Long id, String path, String name) {
        DatasetTreeEntity tree =
                datasetTreeRespository.findById(Long.valueOf(id))
                        .orElseThrow(() -> new DatasetTreeNotExistsException(id));
        DatasetFileEntity file = tree.addFile(path, name);
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
        DatasetTreeEntity tree =
                datasetTreeRespository.findById(Long.valueOf(id))
                        .orElseThrow(() -> new DatasetTreeNotExistsException(id));
        DatasetFileEntity file = tree.findFile(path);
        String dfsPath = fileSystemService.upload(file.getName(),
                inputStream);
        tree.uploaded(file, dfsPath);
        datasetFileRespository.save(file);
    }

    @Override
    public List<JSONObject> list(Long id, String path) {
        DatasetTreeEntity tree =
                datasetTreeRespository.findById(Long.valueOf(id))
                        .orElseThrow(() -> new DatasetTreeNotExistsException(id));
        return tree.list(path);
    }
}
