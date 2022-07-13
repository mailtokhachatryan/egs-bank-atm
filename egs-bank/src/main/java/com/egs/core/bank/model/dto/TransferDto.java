package com.egs.core.bank.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransferDto {
    private String cardId;
    private Integer amount;
}
