package de.akquinet.android.bean;

import org.hornetq.jms.client.HornetQMessage;

import javax.annotation.Resource;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.*;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;

@MessageDriven(activationConfig = {
        @ActivationConfigProperty(propertyName = "destination", propertyValue = "queue/test"),
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue")})
public class MessageDrivenBean implements MessageListener {
    private static final Logger LOGGER = Logger.getLogger(MessageDrivenBean.class.getName());

    @Resource(mappedName = "java:/ConnectionFactory")
    private ConnectionFactory connectionFactory;

    @Resource(mappedName = "java:/topic/test")
    private Topic topic;

    public void onMessage(Message message) {
        String messageText = null;
        try {
            if (message instanceof TextMessage) {
                messageText = ((TextMessage) message).getText();
            }
        } catch (JMSException e) {
            e.printStackTrace();
        }
        LOGGER.info("Received message: " + messageText);


        LOGGER.info("Replying to sender ... ");
        Connection connection = null;
        try {
            connection = connectionFactory.createConnection();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            MessageProducer messageProducer = session.createProducer(topic);
            connection.start();
            // Text Message - works
            TextMessage replyMessage = session.createTextMessage(messageText);

            // Map Message - does not work
//            MapMessage replyMessage = session.createMapMessage();
//            replyMessage.setString("text", messageText);

            // Object Message - does not work
//            ObjectMessage replyMessage = session.createObjectMessage(new String(messageText));
            messageProducer.send(replyMessage);

        } catch (JMSException e) {
            LOGGER.log(Level.SEVERE, "Error while trying to reply.", e);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (JMSException e) {
                }
            }
        }
    }
}