package com.dataclouds.domain.model;

import com.alibaba.fastjson.JSONObject;
import com.dataclouds.exceptions.NameExistsException;
import com.dataclouds.exceptions.PathNotExistsException;
import lombok.*;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
public class DatasetTree extends BaseEntity {

    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "root", referencedColumnName = "id")
    private DatasetDir root;

    public DatasetTree() {
        DatasetDir root = new DatasetDir();
        root.setName("root");
        this.root = root;
    }

    public DatasetDir addDir(String path, String name) {
        DatasetDir parent = findDir(path);
        parent.getChildrenDirs().stream()
                .filter(d -> d.getName().equals(name))
                .findAny()
                .ifPresent(d -> {
                    throw new NameExistsException(path, name);
                });
        DatasetDir dir = new DatasetDir();
        dir.setName(name);
        dir.setParent(parent);
        parent.getChildrenDirs().add(dir);
        return dir;
    }

    public DatasetFile addFile(String path, String name) {
        DatasetDir parent = findDir(path);
        parent.getChildrenFiles().stream()
                .filter(d -> d.getName().equals(name))
                .findAny()
                .ifPresent(d -> {
                    throw new NameExistsException(path, name);
                });
        DatasetFile file = new DatasetFile();
        file.setParent(parent);
        file.setName(name);
        parent.getChildrenFiles().add(file);
        return file;
    }

    public List<JSONObject> list(String path) {
        DatasetDir parent = findDir(path);
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

    public DatasetFile uploaded(DatasetFile file, String dfsPath) {
        file.setDfsPath(dfsPath);
        return file;
    }

    private DatasetDir findDir(String path) {
        DatasetDir result = this.getRoot();
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

    public DatasetFile findFile(String path) {
        DatasetDir parent = this.getRoot();
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

    public JSONObject encode() {
        JSONObject result = new JSONObject();
        result.put("id", this.getId());
        result.put("createTime", this.getCreateTime());
        result.put("updateTime", this.getUpdateTime());
        result.put("root", this.root.encode());
        return result;
    }

    public static DatasetTree decode(JSONObject treeJson) {
        DatasetTree tree = new DatasetTree();
        tree.setId(treeJson.getLongValue("id"));
        tree.setCreateTime(treeJson.getDate("createTime"));
        tree.setUpdateTime(treeJson.getDate("updateTime"));
        tree.setRoot(DatasetDir.decode(treeJson.getJSONObject("root"),
                Optional.empty()));
        return tree;
    }
}
