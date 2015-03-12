package de.kvwl.n8dA.robotwars.commons.network.messages;

public enum ClientNotificationType {
START_TURN, GAMEOVER_LOST, GAMEOVER_WON;

public static String getNotificationName() {
	return "CLIENT_NOTIFICATION_TYPE";
}

}
