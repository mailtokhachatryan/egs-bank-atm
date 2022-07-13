package am.egs.core.atm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ATMApplication {

    public static void main(String[] args) {
        SpringApplication.run(ATMApplication.class, args);
    }

}
