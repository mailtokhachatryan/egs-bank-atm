package am.egs.core.atm.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransferDto {
    private String cardId;
    private Integer amount;
}
