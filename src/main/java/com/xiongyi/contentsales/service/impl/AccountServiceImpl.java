package com.xiongyi.contentsales.service.impl;

import com.xiongyi.contentsales.dao.AccountDao;
import com.xiongyi.contentsales.entity.Account;
import com.xiongyi.contentsales.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by xiongyi on 2018/12/31.
 */
@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountDao accountDao;

    @Override
    public Account getAccountByContentId(int contentId) {
        return accountDao.getAccountByContentId(contentId);
    }

    @Override
    public int insert(Account account) {
        return accountDao.insert(account);
    }

    @Override
    public List<Account> getAccounts() {
        return accountDao.getAccounts();
    }
}
