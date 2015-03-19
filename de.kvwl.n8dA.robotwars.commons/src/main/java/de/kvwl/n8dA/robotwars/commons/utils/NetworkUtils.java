package de.kvwl.n8dA.robotwars.commons.utils;

import java.rmi.registry.Registry;

public class NetworkUtils {

	public static final int BATTLE_SERVER_DEFAULT_REGISTRY_PORT = Registry.REGISTRY_PORT;

	public static final String BATTLE_SERVER_DEFAULT_FULL_TCP_ADDRESS = "tcp://localhost:1527";
	
	public static final  String SERVER_NAME = "RoboBattleServer";

	public static  final String TOPIC_FOR_CLIENTS = "TOPIC.CLIENTS";

	public static final String QUEUE_FOR_CLIENTS = "QUEUE.CLIENTS";

}
