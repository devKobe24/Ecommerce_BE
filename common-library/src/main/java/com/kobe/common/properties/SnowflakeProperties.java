package com.kobe.common.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "snowflake")
public class SnowflakeProperties {
	private long datacenterId;
	private long machineId;
}
