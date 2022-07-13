package am.egs.core.atm.client;


import am.egs.core.atm.config.FeignServiceConfiguration;
import am.egs.core.atm.dto.LoginDto;
import am.egs.core.atm.dto.SecurityTypeDto;
import am.egs.core.atm.dto.TransferDto;
import am.egs.core.atm.fallback.BankServiceClientFallback;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "bank",
        url = "${feign.bank}",
        configuration = FeignServiceConfiguration.class,
        fallback = BankServiceClientFallback.class
)
public interface BankServiceClient {

    @GetMapping("/bank/validate")
    @CircuitBreaker(name = "ValidateCardBreak", fallbackMethod = "validateCard")
    String validateCard(@RequestParam String cardNumber);

    @PostMapping("/bank/withdraw")
    @CircuitBreaker(name = "WithdrawBreak", fallbackMethod = "withdraw")
    String withdraw(@RequestBody TransferDto transferDto);

    @GetMapping("/bank/get_balance/{cardId}")
    @CircuitBreaker(name = "GetBalanceBreak", fallbackMethod = "getBalance")
    String getBalance(@PathVariable String cardId);

    @PostMapping("/bank/login")
    @CircuitBreaker(name = "LoginBreak", fallbackMethod = "login")
    String login(@RequestBody LoginDto loginDto);

    @PostMapping("/bank/deposit")
    @CircuitBreaker(name = "DepositBreak", fallbackMethod = "deposit")
    String deposit(@RequestBody TransferDto transferDto);

    @PostMapping("/bank/change_auth_type")
    @CircuitBreaker(name = "ChangeAuthTypeBreak", fallbackMethod = "changeAuthType")
    String changeAuthType(@RequestBody SecurityTypeDto securityTypeDto);

}
