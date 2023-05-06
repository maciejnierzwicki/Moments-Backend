package pl.maciejnierzwicki.moments.config.datasource;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;

public class MySQLDataSourceConfigurer implements IDataSourceConfigurer {

	@Autowired
	private MySQLDataSourceConfiguration config;
	
	@Override
	@Bean
	public DataSource dataSource() {
		DataSourceBuilder<?> builder = DataSourceBuilder.create();
		builder.username(config.getUsername());
		builder.password(config.getPassword());
		builder.driverClassName("com.mysql.jdbc.Driver");
		builder.url("jdbc:mysql://" + config.getHost() + ":" + config.getPort() + "/" + config.getDatabase());
		return builder.build();
	}
}
