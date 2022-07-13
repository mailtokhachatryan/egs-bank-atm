package am.egs.core.atm.util;

import lombok.experimental.UtilityClass;
import org.springframework.security.core.context.SecurityContextHolder;

@UtilityClass
public class CurrentCard {

    public static String getCardId() {
        return (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

}

