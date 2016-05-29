package in.ramanujam.controller;

import in.ramanujam.model.User;
import in.ramanujam.model.request.RegisterUserRequest;
import in.ramanujam.model.request.SendMailRequest;
import in.ramanujam.model.response.RegisterUserResponse;
import in.ramanujam.model.response.SendMailResponse;
import in.ramanujam.model.response.ResponseStatusType;
import in.ramanujam.service.UserService;
import in.ramanujam.service.converter.MailRequestToMessageConverter;
import in.ramanujam.service.converter.UserRequestToUserConverter;
import in.ramanujam.service.messaging.queue.MailMQPublisherService;
import in.ramanujam.service.messaging.queue.model.MailMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

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

    @RequestMapping(value = "/mailtest", method = RequestMethod.GET) // TODO: remove after testing
    public SendMailResponse testSend() {
        SendMailRequest request = new SendMailRequest();
        request.setBody("Test Body");
        request.setSender("romach007@gmail.com");
        request.setSubject("testSubject");
        request.setToList(Arrays.asList("anatolii.stepaniuk@gmail.com"));

        SendMailResponse response = new SendMailResponse();
        System.out.println("mail route was touched");

        try {
            MailMessage message = mailRequestToMessageConverter.convert(request);
            mailMQPublisherService.publishMailMessage(message);
            response.setStatus(ResponseStatusType.SUCCESS.getValue());

        } catch(Exception e){
            response.setStatus(ResponseStatusType.FAILURE.getValue());
        }
        return response;
    }

    @RequestMapping(value = "/sendemail", method = RequestMethod.POST)
    public SendMailResponse sendEmail(@RequestBody SendMailRequest request) {
        SendMailResponse response = new SendMailResponse();

        try {
            MailMessage message = mailRequestToMessageConverter.convert(request);
            mailMQPublisherService.publishMailMessage(message);
            response.setStatus(ResponseStatusType.SUCCESS.getValue());

        } catch(Exception e){
            response.setStatus(ResponseStatusType.FAILURE.getValue());
        }
        return response;
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public RegisterUserResponse registerUser(@RequestBody RegisterUserRequest request) // TODO: running this method twice with the same email does not cause an error!
     {
         RegisterUserResponse response = new RegisterUserResponse();
         User user = userRequestToUserConverter.convert(request);
         try {
             userService.registerUser(user);
             response.setStatus(ResponseStatusType.SUCCESS.getValue());
         } catch (Exception e) {
             response.setStatus(ResponseStatusType.FAILURE.getValue());
         }
         return response;
     }
}
