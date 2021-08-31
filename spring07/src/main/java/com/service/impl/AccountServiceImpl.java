package com.service.impl;

import com.dao.AccountDao;
import com.domain.Account;
import com.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("accountService")
public class AccountServiceImpl implements AccountService {
    @Autowired
    private AccountDao accountDao;


    @Override
    public void transfer(String sourceName, String targetName, Float money) {
        //1.获取转账人的账户信息
        Account source = accountDao.findAccountByName(sourceName);
        //2.获取收款人的账户信息
        Account target = accountDao.findAccountByName(targetName);
        //3.转账人账户减钱
        source.setMoney(source.getMoney()-money);
        //4.收款人账户加钱
        target.setMoney(target.getMoney()+money);
        //5.更新转账人的账户信息
        accountDao.updateAccount(source);

        //6.更新收款人的账户信息
        accountDao.updateAccount(target);
    }
}
