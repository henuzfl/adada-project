package com.dataclouds.model;

import lombok.*;

import javax.persistence.*;

/**
 * @Author: zfl
 * @Date: 2021/3/11 14:56
 * @Version: 1.0.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "t_dataset_tree")
@ToString(callSuper = true)
public class DatasetTreeEntity extends BaseEntity {

    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "root", referencedColumnName = "id")
    private DatasetDirEntity root;
}
