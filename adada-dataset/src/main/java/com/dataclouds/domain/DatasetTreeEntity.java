package com.dataclouds.domain;

import com.alibaba.fastjson.JSONObject;
import com.dataclouds.exceptions.NameExistsException;
import com.dataclouds.exceptions.PathNotExistsException;
import lombok.*;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: zfl
 * @Date: 2021/3/11 14:56
 * @Version: 1.0.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@Builder
@Entity
@Table(name = "t_dataset_tree")
@ToString(callSuper = true)
public class DatasetTreeEntity extends BaseEntity {

    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "root", referencedColumnName = "id")
    private DatasetDirEntity root;

    public DatasetTreeEntity() {
        DatasetDirEntity root = new DatasetDirEntity();
        root.setName("root");
        this.root = root;
    }

    public DatasetDirEntity addDir(String path, String name) {
        DatasetDirEntity parent = findDir(path);
        parent.getChildrenDirs().stream()
                .filter(d -> d.getName().equals(name))
                .findAny()
                .ifPresent(d -> {
                    throw new NameExistsException(path, name);
                });
        DatasetDirEntity dir = new DatasetDirEntity();
        dir.setName(name);
        dir.setParent(parent);
        parent.getChildrenDirs().add(dir);
        return dir;
    }

    public DatasetFileEntity addFile(String path, String name) {
        DatasetDirEntity parent = findDir(path);
        parent.getChildrenFiles().stream()
                .filter(d -> d.getName().equals(name))
                .findAny()
                .ifPresent(d -> {
                    throw new NameExistsException(path, name);
                });
        DatasetFileEntity file = new DatasetFileEntity();
        file.setParent(parent);
        file.setName(name);
        parent.getChildrenFiles().add(file);
        return file;
    }

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
                    tmp.put("dfsPath", file.getDfsPath());
                    return tmp;
                })
                .collect(Collectors.toList()));
        return result;
    }

    public DatasetFileEntity uploaded(DatasetFileEntity file, String dfsPath) {
        file.setDfsPath(dfsPath);
        return file;
    }

    private DatasetDirEntity findDir(String path) {
        DatasetDirEntity result = this.getRoot();
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

    public DatasetFileEntity findFile(String path) {
        DatasetDirEntity parent = this.getRoot();
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
