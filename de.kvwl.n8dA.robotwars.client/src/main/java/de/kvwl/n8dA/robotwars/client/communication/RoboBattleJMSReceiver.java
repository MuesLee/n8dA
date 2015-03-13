package de.kvwl.n8dA.robotwars.client.communication;

import java.util.UUID;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;

import de.kvwl.n8dA.robotwars.commons.network.messages.ClientProperty;
import de.kvwl.n8dA.robotwars.commons.utils.NetworkUtils;



public class RoboBattleJMSReceiver {
	
	private ActiveMQConnectionFactory connectionFactory;
	private Destination destination;
	private MessageConsumer consumer;
	private Connection connection;
	private Session session;
	private UUID clientUUID;
	
	
	public RoboBattleJMSReceiver(UUID uuid) {
		this.clientUUID = uuid;
		
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
		destination = session.createQueue("QUEUE.CLIENTS");
		consumer = session.createConsumer(destination, ClientProperty.CLIENT_UUID+"'"+clientUUID.toString()+"' OR " +ClientProperty.CLIENT_UUID+"'"+ ClientProperty.ALL_CLIENTS+"'");
		
		} catch (Exception e) {
        e.printStackTrace();
    }
	}
}
