package am.egs.core.atm.dto;

import am.egs.core.atm.util.CardSecurityType;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SecurityTypeDto {
    private String cardId;
    private CardSecurityType securityType;
}
