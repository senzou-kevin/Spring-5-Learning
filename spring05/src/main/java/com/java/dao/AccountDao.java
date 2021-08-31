package com.java.dao;

import com.java.domain.Account;

import java.util.List;

public interface AccountDao {

    /**
     * 查询所有账户
     * @return
     */
    public List<Account> findAll();
}
