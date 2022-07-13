package am.egs.core.atm.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

@RequiredArgsConstructor
public class FeignServiceConfiguration implements RequestInterceptor {

    private final AESManager aesUtil;

    @Value("${security.client-id}")
    private String clientId;
    @Value("${security.client-secret}")
    private String clientSecret;


    @Override
    public void apply(RequestTemplate requestTemplate) {
        var encryptedIdSecret = aesUtil.encrypt(clientId,
                clientSecret);
        requestTemplate.header("MicroserviceAuthorization", "Bearer " + encryptedIdSecret);
        requestTemplate.header("Authorization", "Bearer " + encryptedIdSecret);
    }

}
