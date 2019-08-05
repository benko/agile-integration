package com.redhat.training.agile.amq;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.MessageConsumer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.qpid.jms.JmsConnectionFactory;

public class QueueConsumer {

	public static void main(final String[] args) throws Exception {
		Connection connection = null;
		try {
			ConnectionFactory cf = new JmsConnectionFactory("amqp://localhost:61616");

			connection = cf.createConnection();

			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

			Queue queue = session.createQueue("exampleQueue");

			MessageConsumer messageConsumer = session.createConsumer(queue);

			connection.start();

			System.out.println("Reading new messages...");
			TextMessage message = null;
			do {
				message = (TextMessage) messageConsumer.receive(5000);
				if (message != null) {
					System.out.println();
					System.out.println("Got message: " + message.getText());
				}
			} while (message != null);

		} finally {
			if (connection != null) {
				connection.close();
			}
		}
	}
}
