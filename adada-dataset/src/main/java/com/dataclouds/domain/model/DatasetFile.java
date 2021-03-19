package com.dataclouds.domain.model;

import com.alibaba.fastjson.JSONObject;
import lombok.*;

import javax.persistence.*;

/**
 * @Author: zfl
 * @Date: 2021/3/3 13:30
 * @Version: 1.0.0
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "t_dataset_file")
@ToString(callSuper = true)
public class DatasetFile extends BaseEntity {

    @Column(name = "name", columnDefinition = "varchar(255) not null")
    private String name;

    @Column(name = "dfs_path", columnDefinition = "varchar(1023) null")
    private String dfsPath;

    @ManyToOne(cascade = {CascadeType.REFRESH}, optional = true)
    @JoinColumn(name = "parent", referencedColumnName = "id")
    private DatasetDir parent;

    public JSONObject encode() {
        JSONObject result = new JSONObject();
        result.put("id", this.getId());
        result.put("createTime", this.getCreateTime());
        result.put("updateTime", this.getUpdateTime());
        result.put("name", this.getName());
        result.put("type", "file");
        result.put("parent", this.getParent().getId());
        if (null != this.dfsPath) {
            result.put("dfsPath", this.getDfsPath());
        }
        return result;
    }

    public static DatasetFile decode(JSONObject fileJson,
                                     DatasetDir parent) {
        DatasetFile file = new DatasetFile();
        file.setId(fileJson.getLongValue("id"));
        file.setCreateTime(fileJson.getDate("createTime"));
        file.setUpdateTime(fileJson.getDate("updateTime"));
        file.setName(fileJson.getString("name"));
        if (fileJson.containsKey("dfsPath")) {
            file.setDfsPath(fileJson.getString("dfsPath"));
        }
        file.setParent(parent);
        return file;
    }
}
