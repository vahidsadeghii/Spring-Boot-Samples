package com.example.authoritybasesecurity.service.implement;

import com.example.authoritybasesecurity.service.AccountService;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class AccountServiceImpl implements AccountService {

    @Override
    public long getBalance(String username) {
        return new Random().nextInt(100);
    }
}
