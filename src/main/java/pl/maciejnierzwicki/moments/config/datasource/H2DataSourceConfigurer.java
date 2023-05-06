package pl.maciejnierzwicki.moments.config.datasource;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;

public class H2DataSourceConfigurer implements IDataSourceConfigurer {

	@Autowired
	private H2DataSourceConfiguration config;
	
	@Override
	@Bean
	public DataSource dataSource() {
		DataSourceBuilder<?> builder = DataSourceBuilder.create();
		builder.username(config.getUsername());
		builder.password(config.getPassword());
		builder.driverClassName("org.h2.Driver");
		builder.url("jdbc:h2:file:./" + config.getDatabase() + ";DB_CLOSE_ON_EXIT=FALSE;DB_CLOSE_DELAY=-1");
		return builder.build();
	}
}
