package com.redhat.training.agile.amq;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.qpid.jms.JmsConnectionFactory;

public class QueueProducer {

	public static void main(final String[] args) throws Exception {
		Connection connection = null;
		try {
			ConnectionFactory cf = new JmsConnectionFactory("amqp://localhost:61616");

			connection = cf.createConnection();

			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

			Queue queue = session.createQueue("exampleQueue");

			MessageProducer producer = session.createProducer(queue);

			int numMessages = 10;

			for (int i = 0; i < numMessages; i++) {
				TextMessage message = session.createTextMessage("This is text message " + i);

				producer.send(message);

				System.out.println("Sent message: " + message.getText());
			}

		} finally {
			if (connection != null) {
				connection.close();
			}
		}
	}
}
