package com.dataclouds.domain;

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
public class DatasetFileEntity extends BaseEntity {

    @Column(name = "name", columnDefinition = "varchar(255) not null")
    private String name;

    @Column(name = "dfs_path", columnDefinition = "varchar(1023) null")
    private String dfsPath;

    @ManyToOne(cascade = {CascadeType.REFRESH}, optional = true)
    @JoinColumn(name = "parent", referencedColumnName = "id")
    private DatasetDirEntity parent;
}
