package de.kvwl.n8dA.robotwars.server.network.messaging;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;

import de.kvwl.n8dA.robotwars.commons.utils.NetworkUtils;



public class RoboBattleJMSReceiverServer {
	
	private ActiveMQConnectionFactory connectionFactory;
	private Destination destination;
	private MessageConsumer consumer;
	private Connection connection;
	private Session session;
	
	
	public RoboBattleJMSReceiverServer() {
		
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
			
		connectionFactory = new ActiveMQConnectionFactory(NetworkUtils.FULL_HOST_TCP_ADDRESS);
		connection = connectionFactory.createConnection();
		connection.start();
		session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		destination = session.createTopic("QUEUE.CLIENTS");
		consumer = session.createConsumer(destination);
		
		} catch (Exception e) {
        e.printStackTrace();
    }
	}
}
