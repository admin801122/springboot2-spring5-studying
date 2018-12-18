package com.transaction.service;

import com.transaction.entity.Comics;
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


        SysConfig sysConfig = new SysConfig();
        sysConfig.setValue("admin");
        sysConfig.setVariable("admin801122");
        sysConfig.setSetTime(new Date());
        sysConfig.setSetBy("1");

        sysConfigMapper.insert(sysConfig);
        int a = 1 / 0;
        sysConfigMapper.deleteByPrimaryKey(4);

        return "ok";
    }
}
