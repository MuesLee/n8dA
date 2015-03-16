package de.kvwl.n8dA.robotwars.server.network.messaging;

import java.util.UUID;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;

import de.kvwl.n8dA.robotwars.commons.game.util.GameStateType;
import de.kvwl.n8dA.robotwars.commons.network.messages.ClientProperty;
import de.kvwl.n8dA.robotwars.commons.utils.NetworkUtils;

public class RoboBattleJMSProducerServer {

	private ActiveMQConnectionFactory connectionFactory;
	private Connection connection;
	private Session session;
	private Destination destination;
	private MessageProducer producer;

	public RoboBattleJMSProducerServer() {
		initJMSConnection();
	}

	private void initJMSConnection() {
		try {
			connectionFactory = new ActiveMQConnectionFactory(
					NetworkUtils.FULL_HOST_TCP_ADDRESS);
			connection = connectionFactory.createConnection();
			connection.start();
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			destination = session.createTopic("TOPIC.CLIENTS");
			producer = session.createProducer(destination);

			producer.setDeliveryMode(DeliveryMode.PERSISTENT);
		} catch (Exception e) {
			System.out.println("Caught: " + e);
			e.printStackTrace();
		}

	}
	
	
	
	private void sendMessage(Message message)
	{
		try {
			producer.send(message);
		} catch (JMSException e) {
			
		}
	}

public void sendGameStateNotificationToAllClients(GameStateType gameStateType)
{
	try {
		Message message = session.createMessage();
		
		message.setStringProperty(ClientProperty.CLIENT_UUID.getName(), ClientProperty.ALL_CLIENTS.getName() );
		
		message.setIntProperty(GameStateType.getNotificationName(), gameStateType.ordinal());
		sendMessage(message);
		
	} catch (JMSException e) {
	}
}

public void sendMessageToClient(UUID clientUUID, GameStateType gameStateType)
{
	try {
		Message message = session.createMessage();
		
		message.setStringProperty(ClientProperty.CLIENT_UUID.getName(), clientUUID.toString());
		message.setIntProperty(GameStateType.getNotificationName(), gameStateType.ordinal());

		sendMessage(message);
		
	} catch (JMSException e) {
	}
	
}

	public void closeConnections()
	{
		try {
			session.close();
			connection.close();
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
}
