package com.lsl.code.service.impl;

import com.lsl.code.dao.AccountDao;
import com.lsl.code.dao.impl.AccountDaoImpl;
import com.lsl.code.exception.MoneyNotEnoughException;
import com.lsl.code.exception.TransferException;
import com.lsl.code.pojo.Account;
import com.lsl.code.service.AccountService;

public class AccountServiceImpl implements AccountService {

    private AccountDao accountDao = new AccountDaoImpl();

    @Override
    public void transfer(String fromActno, String toActno, double money) throws MoneyNotEnoughException, TransferException {
        // 查询转出账户的余额
        Account fromAct = accountDao.selectByActno(fromActno);
        if (fromAct.getBalance() < money) {
            throw new MoneyNotEnoughException("对不起，您的余额不⾜。");
        }
        try {
            // 程序如果执⾏到这⾥说明余额充⾜
            // 修改账户余额
            Account toAct = accountDao.selectByActno(toActno);
            fromAct.setBalance(fromAct.getBalance() - money);
            toAct.setBalance(toAct.getBalance() + money);
            // 更新数据库
            accountDao.update(fromAct);
            accountDao.update(toAct);
        } catch (Exception e) {
            throw new TransferException("转账失败，未知原因！");
        }
    }
}
