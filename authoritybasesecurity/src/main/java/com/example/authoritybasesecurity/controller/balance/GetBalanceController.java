package com.example.authoritybasesecurity.controller.balance;

import com.example.authoritybasesecurity.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GetBalanceController {
    private final AccountService accountService;
    private final UserDetails userDetails;

    @Autowired
    public GetBalanceController(AccountService accountService, UserDetails userDetails) {
        this.accountService = accountService;
        this.userDetails = userDetails;
    }

    @PreAuthorize("hasAuthority('GET_BALANCE')")
    @GetMapping("/me/balance")
    public GetBalanceResponse handle() {
        return new GetBalanceResponse(accountService.getBalance(userDetails.getUsername()));
    }
}
