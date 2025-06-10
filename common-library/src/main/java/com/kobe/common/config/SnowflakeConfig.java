package com.kobe.common.config;

import com.kobe.common.properties.SnowflakeProperties;
import com.kobe.common.utils.SnowflakeIdGenerator;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(SnowflakeProperties.class)
public class SnowflakeConfig {

	@Bean
	public SnowflakeIdGenerator snowflakeIdGenerator(SnowflakeProperties properties) {
		// SnowflakeIdGenerator를 생성할 때, properties에서 datacenterId와 machineId를 가져옵니다.
		// yml에서 바인딩된 값을 주입받아 사용.
		return new SnowflakeIdGenerator(properties.getDatacenterId(), properties.getMachineId());
	}
}
