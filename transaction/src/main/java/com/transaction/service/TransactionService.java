package com.transaction.service;

import com.transaction.entity.SysConfig;
import com.transaction.mapper.SysConfigMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * Created by zhangshukang on 2018/12/11.
 */


@Service
public class TransactionService {


    @Autowired

    SysConfigMapper sysConfigMapper;

    @Transactional
    public Object insert(SysConfig entity){

        entity.setValue("admin");
        entity.setVariable("admin801122");
        entity.setSetTime(new Date());
        entity.setSetBy("1");

        sysConfigMapper.insert(entity);
        int a = 1 / 0;
        sysConfigMapper.deleteByPrimaryKey(4);

        return "ok";
    }
}
