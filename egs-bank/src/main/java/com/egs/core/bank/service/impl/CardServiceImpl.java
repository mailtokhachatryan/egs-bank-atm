package com.egs.core.bank.service.impl;

import com.egs.core.bank.exception.GenericException;
import com.egs.core.bank.model.dto.LoginDto;
import com.egs.core.bank.model.dto.SecurityTypeDto;
import com.egs.core.bank.model.dto.TransferDto;
import com.egs.core.bank.repository.CardRepository;
import com.egs.core.bank.service.CardService;
import com.egs.core.bank.util.CardSecurityType;
import com.egs.core.bank.util.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {

    private final CardRepository cardRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    @Transactional
    public String login(LoginDto dto) {
        var cardByCardNumber = cardRepository.findCardByCardNumber(dto.getCardNumber());
        if (cardByCardNumber.getWrongAttempts() < 3 && Boolean.TRUE.equals(!cardByCardNumber.getLocked())) {
            if (cardByCardNumber.getCardSecurityType().equals(CardSecurityType.PIN)) {
                if (dto.getPassword().equals(cardByCardNumber.getPin())) {
                    cardByCardNumber.setWrongAttempts(0);
                    cardRepository.save(cardByCardNumber);
                    return jwtTokenProvider.generateToken(cardByCardNumber.getId().toString());
                } else {
                    cardByCardNumber.setWrongAttempts(cardByCardNumber.getWrongAttempts() + 1);
                    cardRepository.save(cardByCardNumber);
                    throw new EntityNotFoundException("Invalid pin or number");
                }
            }
            throw new UnsupportedOperationException("Unsupported security type");
        }
        cardByCardNumber.setLocked(true);
        cardRepository.save(cardByCardNumber);
        return "Your card is blocked";
    }

    @Override
    public String changeAuthType(SecurityTypeDto securityTypeDto) {
        return null;
    }

    @Override
    public String validateCard(String cardNumber) {
        return cardRepository.findCardByCardNumber(cardNumber).getCardSecurityType().toString();
    }

    @Override
    @Transactional
    public String withdraw(TransferDto transferDto) {
        var amount = transferDto.getAmount();
        var card = cardRepository.findById(UUID.fromString(transferDto.getCardId())).orElseThrow(
                () -> new GenericException("Card not found {}", HttpStatus.NOT_FOUND)
        );

        if (card.getBalance() >= amount) {
            card.setBalance(card.getBalance() - amount);
            cardRepository.save(card);
            return String.format("Withdraw of amount %d is done", amount);
        }
        return "You dont have enough money";
    }

    @Override
    @Transactional
    public String deposit(TransferDto transferDto) {
        var amount = transferDto.getAmount();
        var card = cardRepository.findById(UUID.fromString(transferDto.getCardId())).
                orElseThrow(
                        () -> new GenericException("Card not found {}", HttpStatus.NOT_FOUND)
                );
        card.setBalance(card.getBalance() + transferDto.getAmount());
        cardRepository.save(card);
        return String.format("Deposit of amount %d is done", amount);
    }

    @Override
    public String getBalance(String cardId) {
        var balance = String.format("Your balance is %d", cardRepository.findById(UUID.fromString(cardId)).orElseThrow(
                () -> new GenericException("Card not found {}", HttpStatus.NOT_FOUND)
        ).getBalance());
        log.info("Balance {}", balance);
        return balance;
    }

}
