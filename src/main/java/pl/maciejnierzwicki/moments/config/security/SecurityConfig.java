package pl.maciejnierzwicki.moments.config.security;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.cors.reactive.CorsUtils;
import org.springframework.web.filter.CorsFilter;

import lombok.extern.slf4j.Slf4j;
import pl.maciejnierzwicki.moments.config.security.jwt.JwtTokenFilter;

@Configuration
@EnableWebSecurity
@Slf4j
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private JwtTokenFilter jwtTokenFilter;
	
	@Value("${moments.security.trustedClients}")
	private List<String> trustedClients;
	
	@Bean
	public PasswordEncoder encoder() {
		return new BCryptPasswordEncoder(8);
	}
	
	@Bean("authenticationManager")
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception { // TODO: CONFIGURE BEFORE FINISH PROJECT
		http = http.cors().and().csrf().disable();
		http = http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and();
		
		http = http.authorizeRequests().antMatchers("/account/**", "/posts/new", "/posts/**/like", "/users/**/tracking", "/posts/**/comment", "/notifications", "/notifications-mark-single-read").authenticated().and();
		
		
        http = http
                .exceptionHandling()
                .authenticationEntryPoint(
                    (request, response, ex) -> 
                        response.sendError(
                            HttpServletResponse.SC_UNAUTHORIZED,
                            ex.getMessage()
                        )
                )
                .and();
        
        // Add JWT token filter
        http.addFilterBefore(
            jwtTokenFilter,
            UsernamePasswordAuthenticationFilter.class
        );
	}
	
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source =
            new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        trustedClients.forEach(client -> log.debug("Allowed origin: " + client));
        config.setAllowedOrigins(trustedClients);
        config.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        config.addExposedHeader("Authorization");
        config.setAllowedMethods(List.of("GET", "POST", "DELETE", "OPTIONS"));
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

}
