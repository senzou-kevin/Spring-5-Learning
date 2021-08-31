package com.dao;

import com.domain.Account;

public interface AccountDao {

    /**
     * 根据用户名字查询账户
     * @param name
     * @return
     */
    public Account findAccountByName(String name);

    /**
     * 更新账户
     * @param account
     */
    public void updateAccount(Account account);
}
