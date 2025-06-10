package com.kobe.common.utils;

/**
 * JWT Token에 사용되는 고유한 Token ID(jti)를 생성하는 유틸 클래스.
 * 내부적으로 Snowflake 알고리즘 기반의 ID 생성기를 사용합니다.
 */
public class TokenIdGenerator {

	// 싱글톤 Snowflake 인스턴스 (datacenterId = 1, machineId = 1 설정은 환경에 따라 조정)
	private static final SnowflakeIdGenerator idGenerator = new SnowflakeIdGenerator(1, 1);

	/**
	 * JWT 토큰에 사용할 고유한 jti를 생성합니다.
	 *
	 * @return 문자열 형태의 고유 ID
	 */
	public static String generate() {
		return String.valueOf(idGenerator.nextId());
	}

	// private constructor to prevent instantiation
	private TokenIdGenerator() {}
}
