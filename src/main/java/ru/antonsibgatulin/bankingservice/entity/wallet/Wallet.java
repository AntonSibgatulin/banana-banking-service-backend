package ru.antonsibgatulin.bankingservice.entity.wallet;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.antonsibgatulin.bankingservice.entity.user.User;

import java.math.BigDecimal;

@NoArgsConstructor
@Data
@Entity
@Table(name = "wallets")
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private BigDecimal startDeposit;
    private BigDecimal balance;

    @ManyToOne
    private User user;


    public Wallet(BigDecimal startDeposit, User user) {
        this.startDeposit = startDeposit;
        this.user = user;
        this.balance = new BigDecimal(startDeposit.toString());
    }

    public Wallet(User user) {
        this.user = user;
    }
}
