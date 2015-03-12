package de.kvwl.n8dA.robotwars.server.network.messaging;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

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

	public void sendMessages() throws JMSException {

		while (true) {

			String text = "Hello world! From: "
					+ Thread.currentThread().getName() + " : "
					+ this.hashCode();

			TextMessage message = session.createTextMessage(text);
			System.out.println("Sent message: " + message.hashCode() + " : "
					+ Thread.currentThread().getName());
			producer.send(message);
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
