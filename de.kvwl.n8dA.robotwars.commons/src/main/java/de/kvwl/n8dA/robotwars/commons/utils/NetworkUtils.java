package de.kvwl.n8dA.robotwars.commons.utils;

import java.rmi.registry.Registry;

public class NetworkUtils {

	public static  String HOST_IP_ADDRESS = "localhost";
	public static String HOST_PORT = "1527";
	public static  String FULL_HOST_TCP_ADDRESS = "tcp://"
			+ HOST_IP_ADDRESS + ":" +HOST_PORT;
	public static  int SERVER_REGISTRY_PORT = Registry.REGISTRY_PORT;
	public static final  String SERVER_NAME = "RoboBattleServer";

	public static  final String TOPIC_FOR_CLIENTS = "TOPIC.CLIENTS";

	public static final String QUEUE_FOR_CLIENTS = "QUEUE.CLIENTS";
}
