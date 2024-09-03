package org.example.testcontainer.service.impl;

import org.example.testcontainer.repository.Wallet;
import org.example.testcontainer.repository.WalletRepository;
import org.example.testcontainer.service.WalletService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WalletServiceImpl implements WalletService {
  private final WalletRepository walletRepository;

  public WalletServiceImpl(WalletRepository walletRepository) {
    this.walletRepository = walletRepository;
  }

  @Transactional
  @Override
  public boolean transfer(long fromWalletId, long toWalletId, long amount) {
    Wallet fromWallet = walletRepository.findById(fromWalletId).orElseThrow();
    Wallet toWallet = walletRepository.findById(toWalletId).orElseThrow();

    if (fromWallet.getBalance() < amount) {
      throw new RuntimeException();
    }

    fromWallet.setBalance(fromWallet.getBalance() - amount);
    toWallet.setBalance(toWallet.getBalance() + amount);

    return true;
  }
}
