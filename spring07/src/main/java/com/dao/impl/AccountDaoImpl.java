package com.dao.impl;

import com.dao.AccountDao;
import com.domain.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository("accountDao")
public class AccountDaoImpl implements AccountDao {
    @Autowired
    private JdbcTemplate template;

    /**
     * 根据用户名字查询账户
     * @param name
     * @return
     */
    @Override
    public Account findAccountByName(String name) {
        String sql="select * from account1 where name=?";
        return template.queryForObject(sql,new BeanPropertyRowMapper<Account>(Account.class),name);
    }

    /**
     * 更新账户
     * @param account
     */
    @Override
    public void updateAccount(Account account) {
        String sql="update account1 set money=? where name=?";
        template.update(sql,account.getMoney(),account.getName());
    }
}
