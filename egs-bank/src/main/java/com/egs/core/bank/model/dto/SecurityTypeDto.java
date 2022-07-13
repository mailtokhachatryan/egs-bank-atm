package com.egs.core.bank.model.dto;

import com.egs.core.bank.util.CardSecurityType;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SecurityTypeDto {
    private String cardId;
    private CardSecurityType securityType;
}
