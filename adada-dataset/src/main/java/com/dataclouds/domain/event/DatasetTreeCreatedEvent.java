package com.dataclouds.domain.event;

import com.dataclouds.domain.DatasetTreeEntity;
import com.dataclouds.event.common.BaseDomainEvent;

/**
 * @Author: zfl
 * @Date: 2021/3/16 10:28
 * @Version: 1.0.0
 */
public class DatasetTreeCreatedEvent extends BaseDomainEvent {

    public DatasetTreeCreatedEvent(DatasetTreeEntity tree){
        super(tree);
    }
}
