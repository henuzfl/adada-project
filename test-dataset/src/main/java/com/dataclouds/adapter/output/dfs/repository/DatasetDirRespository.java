package com.dataclouds.adapter.output.dfs.repository;

import com.dataclouds.model.DatasetDirEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @Author: zfl
 * @Date: 2021/3/3 13:35
 * @Version: 1.0.0
 */
@Repository
public interface DatasetDirRespository extends JpaRepository<DatasetDirEntity,
        Long> {

    DatasetDirEntity findByParentIsNull();
}
