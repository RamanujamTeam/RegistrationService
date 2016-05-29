package in.ramanujam.service.queue;

import in.ramanujam.service.mail.MailSenderService;
import in.ramanujam.service.queue.model.MailMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class MailMQConsumerService {

    Logger logger = LoggerFactory.getLogger(MailMQConsumerService.class);

    @Autowired
    private MailSenderService mailSenderService;

    @JmsListener(destination = QueueConstants.MAIL_QUEUE_NAME, containerFactory = "jmsContainerFactory")
    public void onSendMailRequest(MailMessage mailMessage) {
        logger.info("Consuming mail message for sending mail to: " + mailMessage.getToList());
        mailSenderService.sendMail(mailMessage);
    }

}
