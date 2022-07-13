package com.egs.core.bank.model.entity;

import com.egs.core.bank.util.CardSecurityType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Getter


@Setter
@Table(name = "card")
public class Card extends BaseEntity<UUID> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "cart_number", nullable = false)
    private String cardNumber;

    @Column(name = "balance", nullable = false)
    private Integer balance;

    @Column(name = "pin", nullable = false)
    private String pin;

    @Column(name = "password")
    private String password;

    @Column(name = "fingerprint")
    private Integer fingerprint;

    @Column(name = "locked", nullable = false)
    private Boolean locked = false;

    @Column(name = "wrong_attempts", nullable = false)
    private Integer wrongAttempts = 0;

    @Enumerated(EnumType.STRING)
    private CardSecurityType cardSecurityType;

    @Enumerated(EnumType.STRING)
    private CardType cardType;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    private enum CardType {
        VISA,
        MASTER,
        AMEX,
        ARCA,
    }


}