package in.ramanujam.service.messaging.mail;

import in.ramanujam.service.messaging.queue.model.MailMessage;

public interface MailSenderService {

    void sendMail(MailMessage mailMessage);
}
