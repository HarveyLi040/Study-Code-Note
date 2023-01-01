package com.lsl.code.controller;

import com.lsl.code.exception.MoneyNotEnoughException;
import com.lsl.code.exception.TransferException;
import com.lsl.code.service.AccountService;
import com.lsl.code.service.impl.AccountServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * @author 22418
 * @version 1.0
 * @description: TODO
 * @date 2023/1/1 20:18
 */
@RestController
@RequestMapping("/bank")
public class AccountController {

    // 为了让这个对象在其他方法中也可以用。声明为实例变量
    private AccountService accountService = new AccountServiceImpl();

    @PostMapping("/transfer")
    public void transfer(@RequestParam("fromActno") String fromActno,
                         @RequestParam("toActno") String toActno,
                         @RequestParam("money") double money,
                         HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            // 调用service的转账方法完成转账。（调业务层）
            accountService.transfer(fromActno, toActno, money);
            // 程序能够走到这里，表示转账一定成功了
            // 调用View完成展示结果
            response.sendRedirect(request.getContextPath() + "/success.html");
        } catch (MoneyNotEnoughException e) {
            response.sendRedirect(request.getContextPath() + "/error1.html");
        } catch (TransferException e) {
            response.sendRedirect(request.getContextPath() + "/error2.html");
        } catch (Exception e) {
            response.sendRedirect(request.getContextPath() + "/error2.html");
        }

    }

}
