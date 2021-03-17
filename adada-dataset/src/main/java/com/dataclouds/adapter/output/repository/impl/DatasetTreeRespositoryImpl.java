package com.dataclouds.adapter.output.repository.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dataclouds.adapter.output.repository.DatasetTreeRespository;
import com.dataclouds.domain.DatasetTree;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.Optional;

/**
 * @Author: zfl
 * @Date: 2021/3/11 11:38
 * @Version: 1.0.0
 */
@Repository
public class DatasetTreeRespositoryImpl implements DatasetTreeRespository {


    @Override
    public Optional<DatasetTree> findById(Long id) {
        try {
            File file = new File(getPathById(id));
            try (FileInputStream fis = new FileInputStream(file)) {
                String content = IOUtils.toString(fis, "UTF-8");
                if (StringUtils.isNotBlank(content)) {
                    DatasetTree tree =
                            new DatasetTree(id, JSON.parseObject(content));
                    return Optional.of(tree);
                }
            }
        } catch (IOException e) {
            Optional.empty();
        }
        return Optional.empty();
    }

    @Override
    public void save(DatasetTree tree) {
        try {
            JSONObject json = tree.encode();
            File file = new File(getPathById(tree.getId()));
            if (!file.exists()) {
                file.createNewFile();
            }
            try (OutputStream os = new FileOutputStream(file)) {
                IOUtils.write(json.toJSONString(), os, "utf-8");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getPathById(Long id) {
        return id + ".json";
    }
}
