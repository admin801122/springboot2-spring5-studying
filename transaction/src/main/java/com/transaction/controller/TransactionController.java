package com.transaction.controller;

import com.transaction.entity.SysConfig;
import com.transaction.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by zhangshukang on 2018/12/11.
 */

@RestController
public class TransactionController {

    @Autowired
    TransactionService transactionService;


    @GetMapping("/ok")
    public Object ok(){
        return transactionService.insert(new SysConfig());
    }
}
