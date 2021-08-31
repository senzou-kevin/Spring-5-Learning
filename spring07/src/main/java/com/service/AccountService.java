package com.service;

import com.domain.Account;

public interface AccountService {

    /**
     * 转账
     * @param sourceName
     * @param targetName
     * @param money
     */
    public void transfer(String sourceName,String targetName,Float money);

}
