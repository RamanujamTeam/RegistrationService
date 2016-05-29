package in.ramanujam.service.mail;

import in.ramanujam.service.queue.model.MailMessage;

public interface MailSenderService {

    void sendMail(MailMessage mailMessage);
}
