package in.ramanujam.controller;

import in.ramanujam.model.User;
import in.ramanujam.model.request.RegisterUserRequest;
import in.ramanujam.model.response.RegisterUserResponse;
import in.ramanujam.model.response.ResponseStatusType;
import in.ramanujam.service.UserService;
import in.ramanujam.service.converter.MailRequestToMessageConverter;
import in.ramanujam.service.converter.UserRequestToUserConverter;
import in.ramanujam.service.messaging.queue.MailMQPublisherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

@RestController
public class MailController {

    @Autowired
    protected MailMQPublisherService mailMQPublisherService; // TODO: does it need to be protected?
    @Autowired
    private MailRequestToMessageConverter mailRequestToMessageConverter;
    @Autowired
    private UserRequestToUserConverter userRequestToUserConverter;
    @Autowired
    private UserService userService;

//    @RequestMapping(value = "/mailtest", method = RequestMethod.GET) // TODO: remove after testing
//    public SendMailResponse testSend() {
//        SendMailRequest request = new SendMailRequest();
//        request.setBody("Test Body");
//        request.setSender("romach007@gmail.com");
//        request.setSubject("testSubject");
//        request.setToList(Arrays.asList("anatolii.stepaniuk@gmail.com"));
//
//        SendMailResponse response = new SendMailResponse();
//        System.out.println("mail route was touched");
//
//        try {
//            MailMessage message = mailRequestToMessageConverter.convert(request);
//            mailMQPublisherService.publishMailMessage(message);
//            response.setStatus(ResponseStatusType.SUCCESS.getValue());
//
//        } catch(Exception e){
//            response.setStatus(ResponseStatusType.FAILURE.getValue());
//        }
//        return response;
//    }

//    @RequestMapping(value = "/sendemail", method = RequestMethod.POST)
//    public SendMailResponse sendEmail(@RequestBody SendMailRequest request) {
//        SendMailResponse response = new SendMailResponse();
//
//        try {
//            MailMessage message = mailRequestToMessageConverter.convert(request);
//            mailMQPublisherService.publishMailMessage(message);
//            response.setStatus(ResponseStatusType.SUCCESS.getValue());
//
//        } catch(Exception e){
//            response.setStatus(ResponseStatusType.FAILURE.getValue());
//        }
//        return response;
//    }

    @RequestMapping(value = "/register", method = RequestMethod.POST) // TODO Это тоже должно возвращать view, а не json
    public RegisterUserResponse registerUser(@RequestBody RegisterUserRequest request) // TODO: running this method twice with the same email does not cause an error!
    {
        RegisterUserResponse response = new RegisterUserResponse();
        User user = userRequestToUserConverter.convert(request);
        try {
            userService.registerUser(user);
            response.setStatus(ResponseStatusType.SUCCESS.getValue()); // TODO: разделить successfully registered и successfully sent notification email
        } catch (Exception e) {
            response.setStatus(ResponseStatusType.FAILURE.getValue());
        }
        userService.sendConfirmationLink(user);

        return response;
    }

    @RequestMapping(value = "/confirm/{encodedEmailAndCode}", method = RequestMethod.GET)
    // TODO it should return a view, not a rest response. Maybe it should be in a separate controller
    public RegisterUserResponse confirmEmail(@PathVariable String encodedEmailAndCode) {
        RegisterUserResponse response = new RegisterUserResponse();
        String emailAndCode;
        try{
         emailAndCode = new String (Base64.getDecoder().decode(encodedEmailAndCode), "UTF-8");
        }
        catch (UnsupportedEncodingException e){
            response.setStatus(e.getLocalizedMessage());
            return response;
        }
        String confirmationCode = emailAndCode.split("@")[2];
        String email = emailAndCode.split("@" + confirmationCode)[0];
        try {
            userService.confirmEmail(email, confirmationCode);
            response.setStatus("Email confirmed!");
        }
        catch (Exception e) {
            response.setStatus(e.getMessage());
        }

        return response;
    }
}
