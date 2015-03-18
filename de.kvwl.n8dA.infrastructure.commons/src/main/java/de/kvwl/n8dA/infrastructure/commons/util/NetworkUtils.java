package de.kvwl.n8dA.infrastructure.commons.util;

import java.rmi.registry.Registry;

public class NetworkUtils {
	
	public static final String REWARD_SERVER_NAME = "RewardServer";
	public static final int REWARD_SERVER_REGISTRY_PORT = Registry.REGISTRY_PORT;
	public static final String REWARD_SERVER_HOST_IP_ADDRESS = "localhost";
	public static final String REWARD_SERVER_FULL_HOST_TCP_ADDRESS = "tcp://"
			+ REWARD_SERVER_HOST_IP_ADDRESS + ":1528";

}
