package ru.antonsibgatulin.bankingservice.schedule;

import lombok.AllArgsConstructor;
import org.apache.commons.logging.Log;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.antonsibgatulin.bankingservice.entity.wallet.Wallet;
import ru.antonsibgatulin.bankingservice.repository.WalletRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.logging.Logger;

@AllArgsConstructor
@Service
public class WalletSchedule {
    private final WalletRepository walletRepository;
    private static final Logger log = Logger.getLogger(WalletSchedule.class.getName());

    @Scheduled(fixedRate = 60000)
    public synchronized void runBalanceIncrease() {
        log.info("Starting balance increase...");
        List<Wallet> wallets = walletRepository.findAll();
        for (Wallet wallet : wallets) {
            BigDecimal startDeposit = wallet.getStartDeposit();
            BigDecimal balance = wallet.getBalance();
            BigDecimal newBalance = balance.multiply(new BigDecimal("1.05")).setScale(2, RoundingMode.HALF_UP);
            if (newBalance.compareTo(startDeposit.multiply(new BigDecimal("2.07"))) > 0) {
                newBalance = startDeposit.multiply(new BigDecimal("2.07")).setScale(2, RoundingMode.HALF_UP);
            }

            wallet.setBalance(newBalance);
            walletRepository.save(wallet);
        }
        log.info("Balance increase completed.");
    }
}
