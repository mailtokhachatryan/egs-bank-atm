package am.egs.core.atm.fallback;

import am.egs.core.atm.client.BankServiceClient;
import am.egs.core.atm.dto.LoginDto;
import am.egs.core.atm.dto.SecurityTypeDto;
import am.egs.core.atm.dto.TransferDto;

public class BankServiceClientFallback implements BankServiceClient {

    private static final String FALLBACK_RESPONSE = "ATM is not available now!";

    @Override
    public String validateCard(String cardNumber) {
        return FALLBACK_RESPONSE;
    }

    @Override
    public String withdraw(TransferDto transferDto) {
        return FALLBACK_RESPONSE;
    }

    @Override
    public String getBalance(String cardId) {
        return FALLBACK_RESPONSE;
    }


    @Override
    public String login(LoginDto loginDto) {
        return FALLBACK_RESPONSE;
    }

    @Override
    public String deposit(TransferDto transferDto) {
        return FALLBACK_RESPONSE;
    }

    @Override
    public String changeAuthType(SecurityTypeDto type) {
        return FALLBACK_RESPONSE;
    }

}
