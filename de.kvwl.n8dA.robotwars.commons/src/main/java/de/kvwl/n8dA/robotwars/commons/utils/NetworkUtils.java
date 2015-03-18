package de.kvwl.n8dA.robotwars.commons.utils;

import java.rmi.registry.Registry;

//TODO Timo: IP:Port dynamisch
// mach das mal so, dass die IP:Port angaben oder was es da so
// gibt zumindest über die -Dxxx JVM options angegeben werden können
public class NetworkUtils {

	public static final String HOST_IP_ADDRESS = "localhost";

	public static final String FULL_HOST_TCP_ADDRESS = "tcp://"
			+ HOST_IP_ADDRESS + ":1527";
	public static final int SERVER_REGISTRY_PORT = Registry.REGISTRY_PORT;
	public static final String SERVER_NAME = "RoboBattleServer";

	public static final String TOPIC_FOR_CLIENTS = "TOPIC.CLIENTS";

	public static final String QUEUE_FOR_CLIENTS = "QUEUE.CLIENTS";
}
