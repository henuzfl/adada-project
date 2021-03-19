package com.dataclouds.domain.model;

import com.alibaba.fastjson.JSONObject;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Author: zfl
 * @Date: 2021/3/3 13:29
 * @Version: 1.0.0
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "t_dataset_dir")
@ToString(callSuper = true)
public class DatasetDir extends BaseEntity {

    @Column(name = "name", columnDefinition = "varchar(255) not null")
    private String name;

    @ManyToOne(cascade = {CascadeType.REFRESH}, optional = true)
    @JoinColumn(name = "parent", referencedColumnName = "id")
    private DatasetDir parent;

    @OneToMany(cascade = {CascadeType.REFRESH, CascadeType.REMOVE}, fetch =
            FetchType.EAGER, mappedBy = "parent")
    private Set<DatasetDir> childrenDirs = new HashSet<>();

    @OneToMany(cascade = {CascadeType.REFRESH, CascadeType.REMOVE}, fetch =
            FetchType.EAGER, mappedBy = "parent")
    private Set<DatasetFile> childrenFiles = new HashSet<>();

    @OneToOne(mappedBy = "root")
    private DatasetTree root;

    public JSONObject encode() {
        return encodeDir(this);
    }

    private JSONObject encodeDir(DatasetDir dir) {
        JSONObject result = new JSONObject();
        result.put("id", dir.getId());
        result.put("createTime", dir.getCreateTime());
        result.put("updateTime", dir.getUpdateTime());
        result.put("name", dir.getName());
        result.put("type", "dir");
        if (null != parent){
            result.put("parent", dir.getParent().getId());
        }
        result.put("childrenFiles", dir.getChildrenFiles().stream()
                .map(DatasetFile::encode)
                .collect(Collectors.toList()));
        result.put("childrenDirs", dir.getChildrenDirs().stream()
                .map(this::encodeDir)
                .collect(Collectors.toList()));
        return result;
    }

    public static DatasetDir decode(JSONObject dirJson,
                                    Optional<DatasetDir> parent) {
        DatasetDir dir = new DatasetDir();
        dir.setId(dirJson.getLongValue("id"));
        dir.setCreateTime(dirJson.getDate("createTime"));
        dir.setUpdateTime(dirJson.getDate("updateTime"));
        dir.setName(dirJson.getString("name"));
        if (parent.isPresent()) {
            dir.setParent(parent.get());
        }
        if (dirJson.containsKey("childrenFiles")) {
            dir.setChildrenFiles(dirJson.getJSONArray("childrenFiles")
                    .toJavaList(JSONObject.class).stream()
                    .map(tmpJson -> DatasetFile.decode(tmpJson, dir))
                    .collect(Collectors.toSet()));
        } else {
            dir.setChildrenDirs(new HashSet<>());
        }

        if (dirJson.containsKey("childrenDirs")) {
            dir.setChildrenDirs(dirJson.getJSONArray("childrenDirs")
                    .toJavaList(JSONObject.class).stream()
                    .map(tmpJson -> decode(tmpJson, Optional.ofNullable(dir)))
                    .collect(Collectors.toSet()));
        } else {
            dir.setChildrenDirs(new HashSet<>());
        }
        return dir;
    }
}
