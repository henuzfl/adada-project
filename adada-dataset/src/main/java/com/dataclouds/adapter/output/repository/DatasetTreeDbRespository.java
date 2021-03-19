package com.dataclouds.adapter.output.repository;

import com.dataclouds.domain.model.DatasetTree;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Author: zfl
 * @Date: 2021/3/11 15:16
 * @Version: 1.0.0
 */
public interface DatasetTreeDbRespository extends JpaRepository<DatasetTree, Long> {
}
