package de.kvwl.n8dA.robotwars.commons.utils;

import java.rmi.registry.Registry;

public class NetworkUtils {

	public static final String HOST_IP_ADDRESS = "localhost";
	
	public static final String FULL_HOST_TCP_ADDRESS = "tcp://"
			+ HOST_IP_ADDRESS + ":1527";
	public static final int SERVER_REGISTRY_PORT = Registry.REGISTRY_PORT;
	public static final String SERVER_NAME = "RoboBattleServer";
}
