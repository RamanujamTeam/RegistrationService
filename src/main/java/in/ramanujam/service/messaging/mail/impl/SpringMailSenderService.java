package in.ramanujam.service.messaging.mail.impl;


import in.ramanujam.service.messaging.mail.MailSenderService;
import in.ramanujam.service.messaging.queue.model.MailMessage;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


@Service
public class SpringMailSenderService implements MailSenderService {

    public static final String DEFAULT_CHARSET = "UTF-8";
    private static final Logger logger = LoggerFactory.getLogger(SpringMailSenderService.class);


    @Autowired
    private JavaMailSender javaMailSender;

    @Override
    public void sendMail(MailMessage mailMessage) {
        try {

            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(javaMailSender.createMimeMessage(), true, DEFAULT_CHARSET);

            mimeMessageHelper.setText(mailMessage.getBody(), true);
            mimeMessageHelper.setFrom(generateInternetAddress(mailMessage.getSender()));
            mimeMessageHelper.setTo(generateInternetAddresses(mailMessage.getToList()));
            mimeMessageHelper.setCc(generateInternetAddresses(mailMessage.getCcList()));
            mimeMessageHelper.setBcc(generateInternetAddresses(mailMessage.getBccList()));
            mimeMessageHelper.setReplyTo(generateInternetAddress(mailMessage.getReplyTo()));
            mimeMessageHelper.setSubject(mailMessage.getSubject());
            javaMailSender.send(mimeMessageHelper.getMimeMessage());

        } catch (UnsupportedEncodingException | MessagingException exception) { // TODO: we can use the error info here to show user
            logger.error("error occured while sending mail", exception);
        }
    }

    private InternetAddress[] generateInternetAddresses(List<String> emails) throws UnsupportedEncodingException {

        List<InternetAddress> internetAddresses = new ArrayList<>();

        if (CollectionUtils.isNotEmpty(emails)) {
            for (String email : emails) {
                internetAddresses.add(generateInternetAddress(email));
            }
        }
        return internetAddresses.stream().toArray(InternetAddress[]::new);
    }

    private InternetAddress generateInternetAddress(String email) throws UnsupportedEncodingException {
        if (StringUtils.isBlank(email)) {
            return new InternetAddress();
        }
        InternetAddress internetAddress = new InternetAddress();
        internetAddress.setAddress(email);
        return internetAddress;
    }

}
