package springoauth2jwt.configuration.security.oauth2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;

@Configuration
@EnableResourceServer
public class ResourceServerConfigurer extends ResourceServerConfigurerAdapter {
	@Autowired
	JWTConfiguration jwtConfiguration;

	@Autowired
	private ResourceServerTokenServices tokenServices;

	@Value("${security.jwt.resource-ids}")
	private String resourceIds;

	private static String[] WHITE_LISTED_URIS = {
			// -- swagger ui
			"/swagger-resources/**", "/swagger-ui.html", "/v2/api-docs", "/webjars/**" };

	@Override
	public void configure(ResourceServerSecurityConfigurer config) {
		config.tokenServices(jwtConfiguration.defaultTokenServices())
				// config.tokenServices(tokenServices)
				.resourceId(resourceIds);
		System.out.println("******************************************************");
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {
		// http.authorizeRequests()
		// .antMatchers("/v2/api-docs", "/configuration/ui", "/swagger-resources",
		// "/configuration/security", "/swagger-ui.html", "/webjars/**").permitAll()
		// .anyRequest().authenticated();

		http.csrf().disable().authorizeRequests().antMatchers(WHITE_LISTED_URIS).permitAll().and().authorizeRequests()
				.anyRequest().authenticated();
	}

}