package com.egs.core.bank.controller;

import com.egs.core.bank.model.dto.LoginDto;
import com.egs.core.bank.model.dto.SecurityTypeDto;
import com.egs.core.bank.model.dto.TransferDto;
import com.egs.core.bank.service.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/bank")
@RequiredArgsConstructor
public class BankController {

    private final CardService cardService;

    @PostMapping("/login")
    public String login(@RequestBody LoginDto loginDto) {
        return cardService.login(loginDto);
    }

    @GetMapping("/validate")
    public String validateCard(@RequestParam String cardNumber) {
        return cardService.validateCard(cardNumber);
    }

    @PostMapping("/withdraw")
    public String withdraw(@RequestBody TransferDto transferDto) {
        return cardService.withdraw(transferDto);
    }

    @GetMapping("/get_balance/{cardId}")
    public String getBalance(@PathVariable String cardId) {
        return cardService.getBalance(cardId);
    }

    @PostMapping("/deposit")
    public String deposit(@RequestBody TransferDto transferDto) {
        return cardService.deposit(transferDto);
    }

    @PostMapping("/change_auth_type")
    String changeAuthType(@RequestBody SecurityTypeDto securityTypeDto) {
        return cardService.changeAuthType(securityTypeDto);
    }

}
