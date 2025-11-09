package org.example.testcontainer.service;

public interface WalletService {
    boolean transfer(long fromWalletId, long toWalletId, long amount);
}
