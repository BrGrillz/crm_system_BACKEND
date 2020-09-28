package com.aegis.crmsystem;

import com.aegis.crmsystem.properties.StorageProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class CrmsystemApplication {

	@Value("${spring.jpa.properties.hibernate.jdbc.time_zone}")
	private String timezone;

	@PostConstruct
	void started() {
		TimeZone.setDefault(TimeZone.getTimeZone(timezone));
	}

	public static void main(String[] args) {
		SpringApplication.run(CrmsystemApplication.class, args);
	}

}
