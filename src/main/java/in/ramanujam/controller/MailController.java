package in.ramanujam.controller;

import in.ramanujam.model.User;
import in.ramanujam.model.request.RegisterUserRequest;
import in.ramanujam.service.UserService;
import in.ramanujam.service.converter.UserRequestToUserConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

@Controller
public class MailController {

    @Autowired
    private UserRequestToUserConverter userRequestToUserConverter;
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/registration", method = RequestMethod.GET)
    String getRegistrationPage() {
        return "registration";
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)  // TODO: running this method twice with the same email does not cause an error!
    String registerUser(@ModelAttribute RegisterUserRequest request) {
        User user = userRequestToUserConverter.convert(request);
        try {
            userService.registerUser(user);
            // TODO: разделить successfully registered и successfully sent notification email
        } catch (Exception e) {
            // TODO: what to return in this case?
        }
        userService.sendConfirmationLink(user);

        return "check-email";
    }

    @RequestMapping(value = "/confirm/{encodedEmailAndCode}", method = RequestMethod.GET)
    String confirmEmail(@PathVariable String encodedEmailAndCode) {
        String emailAndCode;
        try {
            emailAndCode = new String(Base64.getDecoder().decode(encodedEmailAndCode), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return "error"; // TODO: what to return in this case?
        }
        String confirmationCode = emailAndCode.split("@")[2];
        String email = emailAndCode.split("@" + confirmationCode)[0];
        try {
            userService.confirmEmail(email, confirmationCode);
        } catch (Exception e) {
            // TODO: what to return in this case?
        }

        return "email-confirmed";
    }
}
