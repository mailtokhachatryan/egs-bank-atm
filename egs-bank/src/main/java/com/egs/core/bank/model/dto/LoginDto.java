package com.egs.core.bank.model.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginDto {
    private String cardNumber;
    private String password;
}
