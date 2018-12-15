package com.transaction.service;

import com.transaction.entity.Comics;
import com.transaction.mapper.ComicsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by zhangshukang on 2018/12/11.
 */


@Service
public class TransactionService {


    @Autowired
    ComicsMapper comicsMapper;



    @Transactional
    public Object insert(Comics comics){

        comics.setId("5");
        comics.setName("transaction");
        comics.setCategoryId("222");
        comics.setRecentChapter("333");
        comics.setDesciption("admin");
        comics.setIsEnd("1");

        comicsMapper.insert(comics);
        int a = 1 / 0;
        comicsMapper.deleteByPrimaryKey(4);

        return "ok";
    }
}
