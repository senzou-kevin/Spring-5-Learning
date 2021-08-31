package com.java.dao.impl;

import com.java.dao.AccountDao;
import com.java.domain.Account;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("accountDao")
public class AccountDaoImpl implements AccountDao {
    @Autowired
    private QueryRunner runner;

    /**
     * 查询所有账户
     * @return
     */
    @Override
    public List<Account> findAll() {
        String sql="select * from account1";
        try {
            return runner.query(sql,new BeanListHandler<Account>(Account.class));
        } catch (Exception e) {
           throw new RuntimeException();
        }
    }
}
