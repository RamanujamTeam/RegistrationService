package in.ramanujam.service.messaging.queue;

import in.ramanujam.service.messaging.queue.model.MailMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Transactional
@Service
public class MailMQPublisherService {

    Logger logger = LoggerFactory.getLogger(MailMQPublisherService.class);

    @Autowired
    private JmsTemplate jmsTemplate;

    public void publishMailMessage(MailMessage mailMessage) {
        logger.info("Publishing mail message for sending mail to: " + mailMessage.getToList());
        jmsTemplate.convertAndSend(QueueConstants.MAIL_QUEUE_NAME, mailMessage);
    }
}
