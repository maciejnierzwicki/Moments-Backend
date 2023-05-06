package pl.maciejnierzwicki.moments.config.datasource;

import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Configuration
@Data
public class H2DataSourceConfiguration {
	
	private String database;
	private String username;
	private String password;

}
