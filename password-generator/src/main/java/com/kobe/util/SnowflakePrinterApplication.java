package com.kobe.util;

import com.kobe.common.properties.SnowflakeProperties;
import com.kobe.common.utils.SnowflakeIdGenerator;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(SnowflakeProperties.class)
public class SnowflakePrinterApplication implements ApplicationRunner {

	private final SnowflakeProperties properties;

	public SnowflakePrinterApplication(SnowflakeProperties properties) {
		this.properties = properties;
	}

	public static void main(String[] args) {
		SpringApplication.run(SnowflakePrinterApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		SnowflakeIdGenerator generator = new SnowflakeIdGenerator(
			properties.getDatacenterId(),
			properties.getMachineId()
		);
		System.out.println("🔑 Snowflake ID Generated: " + generator.nextId());
	}
}
