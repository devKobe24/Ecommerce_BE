package com.kobe.common.utils;

public class SnowflakeIdGenerator {

	private final long datacenterId;
	private final long machineId;

	private final static long EPOCH = 1700000000000L;

	private final static long DATACENTER_BITS = 5L;
	private final static long MACHINE_BITS = 5L;
	private final static long SEQUENCE_BITS = 12L;

	private final static long MAX_DATACENTER_ID = ~(-1L << DATACENTER_BITS); // 31
	private final static long MAX_MACHINE_ID = ~(-1L << MACHINE_BITS);       // 31
	private final static long MAX_SEQUENCE = ~(-1L << SEQUENCE_BITS);        // 4095

	private final static long MACHINE_SHIFT = SEQUENCE_BITS;                         // 12
	private final static long DATACENTER_SHIFT = SEQUENCE_BITS + MACHINE_BITS;      // 17
	private final static long TIMESTAMP_SHIFT = SEQUENCE_BITS + MACHINE_BITS + DATACENTER_BITS; // 22

	private long sequence = 0L;
	private long lastTimestamp = -1L;

	public SnowflakeIdGenerator(long datacenterId, long machineId) {
		if (datacenterId < 0 || datacenterId > MAX_DATACENTER_ID) {
			throw new IllegalArgumentException("datacenterId must be between 0 and " + MAX_DATACENTER_ID);
		}
		if (machineId < 0 || machineId > MAX_MACHINE_ID) {
			throw new IllegalArgumentException("machineId must be between 0 and " + MAX_MACHINE_ID);
		}
		this.datacenterId = datacenterId;
		this.machineId = machineId;
	}

	public synchronized long nextId() {
		long currentTimestamp = System.currentTimeMillis();

		if (currentTimestamp < lastTimestamp) {
			throw new RuntimeException("Clock moved backwards.");
		}

		if (currentTimestamp == lastTimestamp) {
			sequence = (sequence + 1) & MAX_SEQUENCE;
			if (sequence == 0) {
				currentTimestamp = waitNextMillis(currentTimestamp);
			}
		} else {
			sequence = 0;
		}

		lastTimestamp = currentTimestamp;

		return ((currentTimestamp - EPOCH) << TIMESTAMP_SHIFT)
			| (datacenterId << DATACENTER_SHIFT)
			| (machineId << MACHINE_SHIFT)
			| sequence;
	}

	private long waitNextMillis(long currentTimestamp) {
		while (currentTimestamp <= lastTimestamp) {
			currentTimestamp = System.currentTimeMillis();
		}
		return currentTimestamp;
	}
}
