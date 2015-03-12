package de.kvwl.n8dA.robotwars.client;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;



public class RoboBattleJMSReceiver {
	
	private ActiveMQConnectionFactory connectionFactory;
	private Destination destination;
	private MessageConsumer consumer;
	private Connection connection;
	private Session session;

	public RoboBattleJMSReceiver() {
		initJMSConnection();
	}
	
	
	public void setMessageListener(MessageListener listener)
	{
		try {
			consumer.setMessageListener(listener);
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
	
	
	private void initJMSConnection()
	{
		try {
			
		connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:1527");
		connection = connectionFactory.createConnection();
		connection.start();
		session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		destination = session.createQueue("QUEUE.CLIENTS");
		consumer = session.createConsumer(destination);
		
		
		} catch (Exception e) {
        e.printStackTrace();
    }
	}
}
