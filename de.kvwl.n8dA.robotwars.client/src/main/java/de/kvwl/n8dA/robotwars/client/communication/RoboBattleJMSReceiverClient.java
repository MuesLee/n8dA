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



public class RoboBattleJMSReceiverClient {
	
	private ActiveMQConnectionFactory connectionFactory;
	private Destination destination;
	private MessageConsumer consumer;
	private Connection connection;
	private Session session;
	private UUID clientUUID;
	private String BATTLE_SERVER_FULL_TCP_ADDRESS;
	
	
	public RoboBattleJMSReceiverClient(UUID uuid, String BATTLE_SERVER_FULL_TCP_ADDRESS) {
		this.clientUUID = uuid;
		this.BATTLE_SERVER_FULL_TCP_ADDRESS = BATTLE_SERVER_FULL_TCP_ADDRESS;
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
			
		connectionFactory = new ActiveMQConnectionFactory(BATTLE_SERVER_FULL_TCP_ADDRESS);
		connection = connectionFactory.createConnection();
		connection.start();
		session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		destination = session.createTopic(NetworkUtils.TOPIC_FOR_CLIENTS);
		consumer = session.createConsumer(destination, ClientProperty.UUID.getName()+"='"+clientUUID.toString()+"' OR " +ClientProperty.UUID.getName()+"='"+ ClientProperty.ALL_CLIENTS.getName()+"'");
		
		} catch (Exception e) {
        e.printStackTrace();
    }
	}
}
