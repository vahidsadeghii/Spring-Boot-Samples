package com.example.authoritybasesecurity.controller.balance;

public class GetBalanceResponse {
    private long balance;

    public GetBalanceResponse(long balance) {
        this.balance = balance;
    }

    public long getBalance() {
        return balance;
    }
}
