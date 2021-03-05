package com.dataclouds.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Collection;

/**
 * @Author: zfl
 * @Date: 2021/3/3 13:29
 * @Version: 1.0.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "t_dataset_dir")
@ToString(callSuper = true)
public class DatasetDirEntity extends BaseEntity {

    @Column(name = "name", columnDefinition = "varchar(255) not null")
    private String name;

    @ManyToOne(cascade = {CascadeType.REFRESH}, optional = true)
    @JoinColumn(name = "parent", referencedColumnName = "id")
    private DatasetDirEntity parent;

    @OneToMany(cascade = {CascadeType.REFRESH, CascadeType.REMOVE}, fetch =
            FetchType.EAGER, mappedBy = "parent")
    private Collection<DatasetDirEntity> childrenDirs;

    @OneToMany(cascade = {CascadeType.REFRESH, CascadeType.REMOVE}, fetch =
            FetchType.EAGER, mappedBy = "parent")
    private Collection<DatasetFileEntity> childrenFiles;
}
