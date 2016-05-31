package in.ramanujam.service;

import in.ramanujam.model.User;
import in.ramanujam.repository.UserRepository;
import in.ramanujam.service.messaging.queue.MailMQPublisherService;
import in.ramanujam.service.messaging.queue.model.MailMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Base64;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;
    @Autowired
    private MailMQPublisherService mailMQPublisherService;

    public void registerUser(User user) { // TODO: additional - we can hash the password before saving
        repository.save(user);
    }

    public void sendConfirmationLink( User user ) {
        MailMessage message = new MailMessage();
        message.setSender("romach007@gmail.com");
        message.setReplyTo("romach007@gmail.com");
        message.setSubject("Confirm your registration");
        message.setToList(Arrays.asList(user.getEmail()));
        String encodedEmailAndCode;
        try
        {
            String emailAndCode = user.getEmail() + "@" + user.getConfirmationCode();
            encodedEmailAndCode = Base64.getEncoder().encodeToString(emailAndCode.getBytes("UTF-8"));
        }
        catch (UnsupportedEncodingException e)
        {
            return;
        }
        message.setBody("It's almost over!\nClick on the link bellow to confirm your registration:\nhttp://localhost:8080/confirm/" + encodedEmailAndCode ); // TODO: unhardcode server address
        mailMQPublisherService.publishMailMessage(message);
    }

    public boolean emailExists(String email) {
        return repository.findOne(email) != null; //TODO: test
    }

    public void confirmEmail(String email, String confirmationCode) { //TODO it should throw exception if confirmationCodeDoes not match
        User user = repository.findOne(email);
        if(user == null)
            throw new RuntimeException("User with email: " + email + " not found");
        if(!confirmationCode.equals(user.getConfirmationCode()))
            throw new RuntimeException("Email confirmation code does not match");

        user.setConfirmationCode(null);
        user.setConfirmed(true);
        repository.save(user);
    }

    public User findByEmail(String email) {
        return repository.findOne(email);
    }

}
