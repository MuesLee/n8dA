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

import de.kvwl.n8dA.robotwars.commons.network.messages.ClientNotificationType;
import de.kvwl.n8dA.robotwars.commons.network.messages.ClientProperty;
import de.kvwl.n8dA.robotwars.commons.utils.NetworkUtils;

public class RoboBattleJMSProducer {

	private ActiveMQConnectionFactory connectionFactory;
	private Connection connection;
	private Session session;
	private Destination destination;
	private MessageProducer producer;

	public RoboBattleJMSProducer() {
		initJMSConnection();
	}

	private void initJMSConnection() {
		try {
			connectionFactory = new ActiveMQConnectionFactory(
					NetworkUtils.FULL_HOST_TCP_ADDRESS);
			connection = connectionFactory.createConnection();
			connection.start();
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			destination = session.createQueue("QUEUE.CLIENTS");
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

public void sendClientNotificationToAllClients(ClientNotificationType clientNotificationType)
{
	try {
		Message message = session.createMessage();
		
		message.setStringProperty(ClientProperty.CLIENT_UUID.getName(), ClientProperty.ALL_CLIENTS.getName() );
		
		message.setIntProperty(ClientNotificationType.getNotificationName(), clientNotificationType.ordinal());
		sendMessage(message);
		
	} catch (JMSException e) {
	}
}

public void sendMessageToClient(UUID clientUUID, ClientNotificationType clientNotificationType)
{
	try {
		Message message = session.createMessage();
		
		message.setStringProperty(ClientProperty.CLIENT_UUID.getName(), clientUUID.toString());
		message.setIntProperty(ClientNotificationType.getNotificationName(), clientNotificationType.ordinal());

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
