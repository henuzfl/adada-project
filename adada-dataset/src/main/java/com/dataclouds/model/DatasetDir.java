package com.dataclouds.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: zfl
 * @Date: 2021/3/5 15:30
 * @Version: 1.0.0
 */
@Data
@NoArgsConstructor
public class DatasetDir {

    private String name;
    private List<DatasetDir> childrenDirs;
    private List<DatasetFile> childrenFiles;

    public DatasetDir(String name) {
        this.name = name;
        this.childrenDirs = new ArrayList<DatasetDir>();
        this.childrenFiles = new ArrayList<DatasetFile>();
    }
}
