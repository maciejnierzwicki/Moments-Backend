package pl.maciejnierzwicki.moments.config.datasource;

import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Configuration
@Data
public class MySQLDataSourceConfiguration {
	
	private String database;
	private String host;
	private Integer port;
	private String username;
	private String password;

}
