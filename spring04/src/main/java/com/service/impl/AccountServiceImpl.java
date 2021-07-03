package com.service.impl;

import com.dao.AccountDao;
import com.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    @Qualifier(value = "accountDao1")
    private AccountDao accountDao;
}
