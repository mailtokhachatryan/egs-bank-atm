package com.egs.core.bank.repository;

import com.egs.core.bank.model.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CardRepository extends JpaRepository<Card, UUID> {

    Card findCardByCardNumber(String cardNumber);


}
