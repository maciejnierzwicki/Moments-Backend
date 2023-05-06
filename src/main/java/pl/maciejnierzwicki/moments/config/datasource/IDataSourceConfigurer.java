package pl.maciejnierzwicki.moments.config.datasource;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public interface IDataSourceConfigurer {
	
	public DataSource dataSource();

}
