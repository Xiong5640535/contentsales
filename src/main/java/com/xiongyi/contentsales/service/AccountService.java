package com.xiongyi.contentsales.service;

import com.xiongyi.contentsales.entity.Account;

import java.util.List;

/**
 * Created by xiongyi on 2018/12/31.
 */
public interface AccountService {

    Account getAccountByContentId(int contentId);

    int insert(Account account);

    List<Account> getAccounts();
}
