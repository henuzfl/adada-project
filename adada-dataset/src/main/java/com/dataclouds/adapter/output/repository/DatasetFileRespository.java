package com.dataclouds.adapter.output.repository;

import com.dataclouds.domain.model.DatasetFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @Author: zfl
 * @Date: 2021/3/3 13:35
 * @Version: 1.0.0
 */
@Repository
public interface DatasetFileRespository extends JpaRepository<DatasetFile,
        Long> {
}
