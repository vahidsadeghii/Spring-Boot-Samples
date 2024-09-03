package org.example.testcontainer;

import org.example.testcontainer.domain.User;
import org.example.testcontainer.repository.Wallet;
import org.example.testcontainer.repository.WalletRepository;
import org.example.testcontainer.service.WalletService;
import org.example.testcontainer.service.impl.WalletServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.stream.Stream;

@SpringBootTest(classes = WalletServiceImpl.class)
public class WalletTransferTest {
  @MockBean private WalletRepository walletRepository;
  @Autowired private WalletService walletService;

  @Test
  void withMockWalletWithEnoughBalance_thenTransfer_returnTrue() {
    Mockito.when(walletRepository.findById(1L))
        .thenReturn(
            Optional.of(new Wallet(1, new User(), 1000, LocalDateTime.now(), LocalDateTime.now())));
    Mockito.when(walletRepository.findById(2L))
        .thenReturn(
            Optional.of(new Wallet(2, new User(), 1000, LocalDateTime.now(), LocalDateTime.now())));

    boolean result = walletService.transfer(1, 2, 1000);
    Assertions.assertTrue(result);

    Wallet fromWallet = walletRepository.findById(1L).orElseThrow();
    Assertions.assertEquals(0, fromWallet.getBalance());

    Wallet toWallet = walletRepository.findById(2L).orElseThrow();
    Assertions.assertEquals(2000, toWallet.getBalance());

    Mockito.verify(walletRepository, Mockito.times(2)).findById(1L);
    Mockito.verify(walletRepository, Mockito.times(2)).findById(2L);
  }

  @Test
  void withMockWalletNotEnoughBalance_thenTransfer_throwRuntimeError() {
    Mockito.when(walletRepository.findById(1L))
        .thenReturn(
            Optional.of(new Wallet(1, new User(), 0, LocalDateTime.now(), LocalDateTime.now())));
    Mockito.when(walletRepository.findById(2L))
        .thenReturn(
            Optional.of(new Wallet(2, new User(), 1000, LocalDateTime.now(), LocalDateTime.now())));

    Assertions.assertThrows(
        RuntimeException.class,
        () -> {
          walletService.transfer(1, 2, 1000);
        });
  }

  @ParameterizedTest
  @ValueSource(longs = {1000, 2000, 3000})
  void withNumberOfAmount_thenTransfer_returnTrue(long amount) {
    Mockito.when(walletRepository.findById(1L))
        .thenReturn(
            Optional.of(
                new Wallet(1, new User(), 1000_000, LocalDateTime.now(), LocalDateTime.now())));
    Mockito.when(walletRepository.findById(2L))
        .thenReturn(
            Optional.of(new Wallet(2, new User(), 1000, LocalDateTime.now(), LocalDateTime.now())));

    boolean result = walletService.transfer(1, 2, amount);
    Assertions.assertTrue(result);

    Wallet fromWallet = walletRepository.findById(1L).orElseThrow();
    Assertions.assertEquals(1000_000 - amount, fromWallet.getBalance());

    Wallet toWallet = walletRepository.findById(2L).orElseThrow();
    Assertions.assertEquals(1000 + amount, toWallet.getBalance());

    Mockito.verify(walletRepository, Mockito.times(2)).findById(1L);
    Mockito.verify(walletRepository, Mockito.times(2)).findById(2L);
  }

  static Stream<Arguments> transferBalanceTestProvider() {
    return Stream.of(
        Arguments.arguments(1000, 0, 500, 500, 500),
        Arguments.arguments(1000, 0, 1000, 0, 1000),
        Arguments.arguments(1000, 500, 500, 500, 1000));
  }

  @ParameterizedTest
  @MethodSource("transferBalanceTestProvider")
  void withNumberOfAmountAndResults_thenTransfer_returnTrue(
      long fromWalletBalance,
      long toWalletBalance,
      long transferAmount,
      long fromWalletResult,
      long toWalletResult) {
    Mockito.when(walletRepository.findById(1L))
        .thenReturn(
            Optional.of(
                new Wallet(
                    1, new User(), fromWalletBalance, LocalDateTime.now(), LocalDateTime.now())));
    Mockito.when(walletRepository.findById(2L))
        .thenReturn(
            Optional.of(
                new Wallet(
                    2, new User(), toWalletBalance, LocalDateTime.now(), LocalDateTime.now())));

    boolean result = walletService.transfer(1, 2, transferAmount);
    Assertions.assertTrue(result);

    Wallet fromWallet = walletRepository.findById(1L).orElseThrow();
    Assertions.assertEquals(fromWalletResult, fromWallet.getBalance());

    Wallet toWallet = walletRepository.findById(2L).orElseThrow();
    Assertions.assertEquals(toWalletResult, toWallet.getBalance());

    Mockito.verify(walletRepository, Mockito.times(2)).findById(1L);
    Mockito.verify(walletRepository, Mockito.times(2)).findById(2L);
  }
}
