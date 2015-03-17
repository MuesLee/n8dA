package de.kvwl.n8dA.robotwars.client.communication;

import java.util.UUID;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;

import de.kvwl.n8dA.robotwars.commons.game.actions.RobotAction;
import de.kvwl.n8dA.robotwars.commons.network.messages.ClientProperty;
import de.kvwl.n8dA.robotwars.commons.utils.NetworkUtils;

public class RoboBattleJMSProducerClient implements AsyncServerCommunicator{

	private ActiveMQConnectionFactory connectionFactory;
	private Connection connection;
	private Session session;
	private Destination destination;
	private MessageProducer producer;

	private UUID clientUUID;

	public RoboBattleJMSProducerClient(UUID clientUUID) {
		this.clientUUID = clientUUID;
		initJMSConnection(clientUUID);
	}

	private void initJMSConnection(UUID clientUUID) {
		try {
			connectionFactory = new ActiveMQConnectionFactory(
					NetworkUtils.FULL_HOST_TCP_ADDRESS);
			connection = connectionFactory.createConnection();
			connection.start();
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			destination = session.createQueue(NetworkUtils.QUEUE_FOR_CLIENTS);
			producer = session.createProducer(destination);

			producer.setDeliveryMode(DeliveryMode.PERSISTENT);
		} catch (Exception e) {
			System.out.println("Caught: " + e);
			e.printStackTrace();
		}

	}

	private void sendMessage(Message message) {
		try {
			message.setStringProperty(ClientProperty.CLIENT_UUID.getName(),
					clientUUID.toString());
			producer.send(message);
		} catch (JMSException e) {

		}
	}

	@Override
	public void sendReadyToBeginBattleToServer() {
		try {
			Message message = session.createMessage();
			message.setBooleanProperty(ClientProperty.READY_TO_START_THE_BATTLE.getName(), true);
		} catch (JMSException e) {
			
		}
	}
	@Override
	public void sendRobotActionToServer(RobotAction robotAction) {
		try {
			ObjectMessage message = session.createObjectMessage();

			message.setObject(robotAction);
			sendMessage(message);

		} catch (JMSException e) {
		}
	}
	@Override
	public void closeConnections() {
		try {
			session.close();
			connection.close();
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
}
