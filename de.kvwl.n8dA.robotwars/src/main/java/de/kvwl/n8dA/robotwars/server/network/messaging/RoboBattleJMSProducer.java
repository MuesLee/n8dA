package de.kvwl.n8dA.robotwars.server.network.messaging;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;

import de.kvwl.n8dA.robotwars.commons.network.messages.ClientNotificationType;

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
					"tcp://localhost:1527");
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

public void sendMessageToAllClients(ClientNotificationType clientNotificationType)
{
	try {
		Message message = session.createMessage();
		
		message.setIntProperty(ClientNotificationType.getNotificationName(), clientNotificationType.ordinal());
		sendMessage(message);
		
	} catch (JMSException e) {
	}
}

	public void spamMessages() {

		while (true) {

			try {
				Message message = session.createMessage();
				System.out.println("Sent message: " + message.hashCode() + " : "
						+ Thread.currentThread().getName());
				message.setIntProperty(ClientNotificationType.getNotificationName(), ClientNotificationType.START_TURN.ordinal());
				sendMessage(message);
			} catch (JMSException e) {
				e.printStackTrace();
			}
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
