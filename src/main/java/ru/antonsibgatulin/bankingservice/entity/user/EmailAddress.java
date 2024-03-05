package ru.antonsibgatulin.bankingservice.entity.user;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "email_addresses")
public class EmailAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(length = 255, nullable = false, unique = true)
    private String address;


    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    private User user;

    public EmailAddress(String address, User user) {
        this.address = address;
        this.user = user;
    }

}