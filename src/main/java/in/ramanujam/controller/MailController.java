package in.ramanujam.controller;

import in.ramanujam.model.request.MailRequest;
import in.ramanujam.model.response.MailResponse;
import in.ramanujam.model.response.ResponseStatusType;
import in.ramanujam.service.converter.MailRequestToMessageConverter;
import in.ramanujam.service.queue.MailMQPublisherService;
import in.ramanujam.service.queue.model.MailMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@RestController
public class MailController {

    @Autowired
    protected MailMQPublisherService mailMQPublisherService;
    @Autowired
    private MailRequestToMessageConverter mailRequestToMessageConverter;

    @RequestMapping(value = "/mailtest", method = RequestMethod.GET)
    public MailResponse testSend() {
        MailRequest request = new MailRequest();
        request.setBody("Test Body");
        request.setSender("romach007@gmail.com");
        request.setSubject("testSubject");
        request.setToList(Arrays.asList("romach007@gmail.com", "anatolii.stepaniuk@gmail.com"));

        MailResponse response = new MailResponse();
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


    @RequestMapping(value = "/mail", method = RequestMethod.POST)
    public MailResponse send(@RequestBody MailRequest request) {
        MailResponse response = new MailResponse();
        System.out.println("mail route was touched");

        System.out.println("MailRequest:" + request.getBody() + " " + request.getSender());
        try {
            MailMessage message = mailRequestToMessageConverter.convert(request);
            mailMQPublisherService.publishMailMessage(message);
            response.setStatus(ResponseStatusType.SUCCESS.getValue());

        } catch(Exception e){
            response.setStatus(ResponseStatusType.FAILURE.getValue());
        }
        return response;
    }

}
