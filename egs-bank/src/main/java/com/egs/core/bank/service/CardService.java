package com.egs.core.bank.service;


import com.egs.core.bank.model.dto.LoginDto;
import com.egs.core.bank.model.dto.SecurityTypeDto;
import com.egs.core.bank.model.dto.TransferDto;

public interface CardService {
    String login(LoginDto dto);

    String changeAuthType(SecurityTypeDto securityTypeDto);

    String validateCard(String cardNumber);

    String withdraw(TransferDto transferDto);

    String deposit(TransferDto transferDto);

    String getBalance(String cardId);
}
