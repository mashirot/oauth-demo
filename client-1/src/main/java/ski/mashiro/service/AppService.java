package ski.mashiro.service;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.stereotype.Service;

@Service
public class AppService {
    private final OAuth2AuthorizedClientService authorizedClientService;

    public AppService(OAuth2AuthorizedClientService authorizedClientService) {
        this.authorizedClientService = authorizedClientService;
    }

    @PreAuthorize("hasAuthority('SCOPE_profile')")
    public String getJwtToken() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var accessToken = getAccessToken(authentication);
        var refreshToken = getRefreshToken(authentication);
        return String.format("Access Token = %s <br /><br /><br /> Refresh Token = %s",
                accessToken.getTokenValue(), refreshToken.getTokenValue());
    }

    public OAuth2AccessToken getAccessToken(Authentication authentication) {
        var authorizedClient = this.getAuthorizedClient(authentication);
        return authorizedClient.getAccessToken();
    }

    public OAuth2RefreshToken getRefreshToken(Authentication authentication) {
        var authorizedClient = this.getAuthorizedClient(authentication);
        return authorizedClient.getRefreshToken();
    }

    private OAuth2AuthorizedClient getAuthorizedClient(Authentication authentication) {
        if (authentication instanceof OAuth2AuthenticationToken oauthToken) {
            String clientRegistrationId = oauthToken.getAuthorizedClientRegistrationId();
            String principalName = oauthToken.getName();
            return authorizedClientService.loadAuthorizedClient(clientRegistrationId, principalName);
        }
        return null;
    }
}
