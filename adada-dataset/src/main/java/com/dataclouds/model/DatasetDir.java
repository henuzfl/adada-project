package com.dataclouds.model;

import lombok.Data;
import lombok.NoArgsConstructor;

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

}
