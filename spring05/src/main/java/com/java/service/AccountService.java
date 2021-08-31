package com.java.service;

import com.java.domain.Account;

import java.util.List;

public interface AccountService {

    /**
     * 查询所有账户
     * @return
     */
    public List<Account> findAll();
}
