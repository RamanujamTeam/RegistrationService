package in.ramanujam.service.converter;

import in.ramanujam.model.request.SendMailRequest;
import in.ramanujam.service.messaging.queue.model.MailMessage;
import org.springframework.stereotype.Component;

@Component
public class MailRequestToMessageConverter {

    public MailMessage convert(SendMailRequest request) {
        MailMessage mailMessage = new MailMessage();
        mailMessage.setSubject(request.getSubject());
        mailMessage.setSender(request.getSender());
        mailMessage.setToList(request.getToList());
        mailMessage.setBccList(request.getBccList());
        mailMessage.setCcList(request.getCcList());
        mailMessage.setReplyTo(request.getReplyTo());
        mailMessage.setBody(request.getBody());
        return mailMessage;
    }
}
