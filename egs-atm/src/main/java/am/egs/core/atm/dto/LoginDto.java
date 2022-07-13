package am.egs.core.atm.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginDto {
    private String cardNumber;
    private String password;
}
