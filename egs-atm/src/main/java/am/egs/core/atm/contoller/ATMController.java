package am.egs.core.atm.contoller;

import am.egs.core.atm.client.BankServiceClient;
import am.egs.core.atm.dto.LoginDto;
import am.egs.core.atm.dto.SecurityTypeDto;
import am.egs.core.atm.dto.TransferDto;
import am.egs.core.atm.util.CardSecurityType;
import am.egs.core.atm.util.CurrentCard;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/atm")
public class ATMController {

    private final BankServiceClient serviceClient;

    @GetMapping("/validate")
    @Operation(summary = "Validate card.", description = "This method validates card by number.")
    public String validateCard(@RequestParam String cardNumber) {
        cardNumber = cardNumber.trim();
        return serviceClient.validateCard(cardNumber);
    }

    @PostMapping("/login")
    @Operation(security = {@SecurityRequirement(name = "auth-token")}, summary = "Authentication.", description = "This method authenticates card.")
    public String login(@RequestBody LoginDto loginDto) {
        return serviceClient.login(loginDto);
    }

    @PostMapping("/withdraw")
    @Operation(security = {@SecurityRequirement(name = "auth-token")}, summary = "Withdraw.", description = "This method takes money from card.")
    public String withdraw(@RequestBody Integer amount) {
        TransferDto dto = new TransferDto();
        dto.setCardId(CurrentCard.getCardId());
        dto.setAmount(amount);
        return serviceClient.withdraw(dto);
    }

    @GetMapping("/balance")
    public String getBalance() {
        return serviceClient.getBalance(CurrentCard.getCardId());
    }

    @PostMapping("/deposit")
    @Operation(security = {@SecurityRequirement(name = "auth-token")}, summary = "Deposit.", description = "This method adds money to card.")
    public String deposit(@RequestBody Integer amount) {
        TransferDto dto = new TransferDto();
        dto.setCardId(CurrentCard.getCardId());
        dto.setAmount(amount);
        return serviceClient.deposit(dto);
    }

    @PostMapping("/auth_type")
    @Operation(security = {@SecurityRequirement(name = "auth-token")}, summary = "Change authentication type.", description = "This method for change authentication type.")
    public ResponseEntity<String> changeAuthType(@RequestParam CardSecurityType type) {
        SecurityTypeDto dto = new SecurityTypeDto();
        dto.setSecurityType(type);
        dto.setCardId(CurrentCard.getCardId());
        return ResponseEntity.status(200).body(serviceClient.changeAuthType(dto));
    }
}
