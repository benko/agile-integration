package com.redhat.training.agile.amq;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageFormatException;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class GenericConsumer {
	private static final String DEFAULT_QUEUE_NAME = "jms/queue/tweets.en";
    private static final int TIMEOUT_MILLISECONDS = 5000;
    private static final int RECV_ATTEMPTS = 5;

	private static final Logger log = Logger.getLogger("GenericConsumer");

	public static void main(final String[] args) throws Exception {
		String queue = System.getProperty("consumer.queueName", DEFAULT_QUEUE_NAME);
		int delay = Integer.parseInt(System.getProperty("consumer.delay", String.valueOf(TIMEOUT_MILLISECONDS))); 
		int attempts = Integer.parseInt(System.getProperty("consumer.attempts", String.valueOf(RECV_ATTEMPTS))); 

		log.info("Starting initialisation from destination " + queue + ", delay: " + delay + "ms, attempts: " + attempts);

		Context jndi = null;
		ConnectionFactory cf = null;
		Destination q = null;
		try {
			jndi = new InitialContext();
			cf = (ConnectionFactory)jndi.lookup("ConnectionFactory");
			q = (Destination)jndi.lookup(queue);
		} catch (NamingException ne) {
			log.log(Level.SEVERE, "NamingException while initialising", ne);
			System.exit(1);
		}

		log.info("Initialised. Creating connections, sessions, etc...");

		Connection c = null;
		Session s = null;
		MessageConsumer mc = null;
		try {
			c = cf.createConnection();
			c.start();

			s = c.createSession(false, Session.AUTO_ACKNOWLEDGE);
			mc = s.createConsumer(q);

			log.info("Connection started. Waiting for new messages...");

			Message m = null;
			for (int x = 0; x < attempts; x++) {
				m = mc.receive(delay);
				if (m == null) {
					log.info("No message on attempt " + x + "...");
					continue;
				}
				
				log.info("Got message of type " + m.getClass().getName());
				if (m instanceof TextMessage) {
					log.info("Payload: " + ((TextMessage)m).getText());
				} else {
					try {
						log.info("String representation: " + m.getBody(String.class));
					} catch (MessageFormatException mfe) {
						log.log(Level.SEVERE, "Tried converting to string, sorry", mfe);
					}
				}
				
				// received a message!
				break;
			}
			
			log.info("Through receiving messages, bye!");
		} catch (JMSException jmse) {
			log.log(Level.SEVERE, "JMSException while receiving messages", jmse);
		} finally {
			if (mc != null) {
				mc.close();
			}
			if (s != null) {
				s.close();
			}
			if (c != null) {
				c.close();
			}
		}
	}
}
