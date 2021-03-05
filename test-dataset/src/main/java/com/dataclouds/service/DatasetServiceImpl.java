package com.dataclouds.service;

import com.alibaba.fastjson.JSONObject;
import com.dataclouds.adapter.output.dfs.IFileSystemService;
import com.dataclouds.model.DatasetDirEntity;
import com.dataclouds.model.DatasetFileEntity;
import com.dataclouds.exceptions.NameExistsException;
import com.dataclouds.exceptions.PathNotExistsException;
import com.dataclouds.adapter.output.dfs.repository.DatasetDirRespository;
import com.dataclouds.adapter.output.dfs.repository.DatasetFileRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: zfl
 * @Date: 2021/3/3 13:53
 * @Version: 1.0.0
 */
@Service
public class DatasetServiceImpl implements IDatasetService {

    @Autowired
    private DatasetDirRespository datasetDirRespository;

    @Autowired
    private DatasetFileRespository datasetFileRespository;

    @Autowired
    private IFileSystemService fileSystemService;

    @PostConstruct
    public void init(){

    }

    @Override
    public void addDir(String path, String dir) {
        DatasetDirEntity parent = findDir(path);
        parent.getChildrenDirs().stream()
                .filter(d -> d.getName().equals(dir))
                .findAny()
                .ifPresent(d -> {
                    throw new NameExistsException(path, dir);
                });
        DatasetDirEntity dirEntity = new DatasetDirEntity();
        dirEntity.setName(dir);
        dirEntity.setParent(parent);
        datasetDirRespository.save(dirEntity);
    }

    @Override
    public void addFile(String path, String fileName) {
        DatasetDirEntity parent = findDir(path);
        parent.getChildrenFiles().stream()
                .filter(d -> d.getName().equals(fileName))
                .findAny()
                .ifPresent(d -> {
                    throw new NameExistsException(path, fileName);
                });
        DatasetFileEntity fileEntity = new DatasetFileEntity();
        fileEntity.setParent(parent);
        fileEntity.setName(fileName);
        datasetFileRespository.save(fileEntity);
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
        DatasetFileEntity datasetFile = findFile(path);
        String fsPath = fileSystemService.upload(datasetFile.getName(),
                inputStream);
        datasetFile.setFsPath(fsPath);
        datasetFileRespository.save(datasetFile);
    }

    @Override
    public List<JSONObject> list(String path) {
        DatasetDirEntity parent = findDir(path);
        List<JSONObject> result = new ArrayList<JSONObject>();
        result.addAll(parent.getChildrenDirs().stream()
                .map(dir -> {
                    JSONObject tmp = new JSONObject();
                    tmp.put("name", dir.getName());
                    tmp.put("type", "dir");
                    return tmp;
                })
                .collect(Collectors.toList()));
        result.addAll(parent.getChildrenFiles().stream()
                .map(file -> {
                    JSONObject tmp = new JSONObject();
                    tmp.put("name", file.getName());
                    tmp.put("type", "file");
                    return tmp;
                })
                .collect(Collectors.toList()));
        return result;
    }

    private DatasetDirEntity findDir(String path) {
        DatasetDirEntity result = datasetDirRespository.findByParentIsNull();
        for (String p : path.split("/")) {
            if (!StringUtils.isEmpty(p.trim())) {
                result = result.getChildrenDirs().stream()
                        .filter(d -> d.getName().equals(p.trim()))
                        .findAny()
                        .orElseThrow(() -> new PathNotExistsException(path));
            }
        }
        return result;
    }

    private DatasetFileEntity findFile(String path) {
        DatasetDirEntity parent = datasetDirRespository.findByParentIsNull();
        String[] filePathArr = path.split("/");
        String fileName = filePathArr[filePathArr.length - 1];
        for (int i = 0; i < filePathArr.length - 1; i++) {
            String dirName = filePathArr[i];
            if (!StringUtils.isEmpty(dirName)) {
                parent = parent.getChildrenDirs().stream()
                        .filter(d -> d.getName().equals(dirName))
                        .findAny()
                        .orElseThrow(() -> new PathNotExistsException(path));
            }
        }
        return parent.getChildrenFiles().stream()
                .filter(f -> f.getName().equals(fileName))
                .findFirst()
                .orElseThrow(() -> new PathNotExistsException(path));
    }
}
