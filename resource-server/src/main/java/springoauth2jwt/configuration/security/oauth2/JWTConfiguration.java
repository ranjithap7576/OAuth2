package springoauth2jwt.configuration.security.oauth2;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
public class JWTConfiguration {
	
	@Value("${security.signing-key}")
	private String signingKey;

	@Bean
	public JwtTokenStore tokenStore() {
		return new JwtTokenStore(tokenEnhancer());
	}

	@Bean
	public TokenEnhancerChain tokenEnhancerChain() {
		TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
		List tokenEnhancerList = new ArrayList();
		tokenEnhancerList.add(tokenEnhancer());

		tokenEnhancerChain.setTokenEnhancers(tokenEnhancerList);
		return tokenEnhancerChain;
	}

	@Bean
	public JwtAccessTokenConverter tokenEnhancer() {
		JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
		jwtAccessTokenConverter.setAccessTokenConverter(defaultAccessTokenConverter());
		jwtAccessTokenConverter.setSigningKey(signingKey);
		return jwtAccessTokenConverter;
	}

	@Bean
	public DefaultAccessTokenConverter defaultAccessTokenConverter() {
		DefaultAccessTokenConverter converter = new DefaultAccessTokenConverter();
		// default userTokenConverter is DefaultUserAuthenticationConverter
		DefaultUserAuthenticationConverter userConverter = new DefaultUserAuthenticationConverter();
		converter.setUserTokenConverter(userConverter);
		return converter;
	}

	@Bean
	public ResourceServerTokenServices defaultTokenServices() {
		DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
		defaultTokenServices.setTokenStore(tokenStore());
		defaultTokenServices.setTokenEnhancer(tokenEnhancerChain());
		defaultTokenServices.setSupportRefreshToken(true);
		return defaultTokenServices;
	}
}