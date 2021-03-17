package com.dataclouds.adapter.output.repository;

import com.dataclouds.domain.DatasetTree;

import java.util.Optional;

/**
 * @Author: zfl
 * @Date: 2021/3/11 11:36
 * @Version: 1.0.0
 */
public interface DatasetTreeRespository {

    Optional<DatasetTree> findById(Long id);

    void save(DatasetTree tree);
}
