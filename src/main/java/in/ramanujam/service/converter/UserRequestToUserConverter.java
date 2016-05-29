package in.ramanujam.service.converter;

import in.ramanujam.model.User;
import in.ramanujam.model.request.RegisterUserRequest;
import org.springframework.stereotype.Component;

@Component
public class UserRequestToUserConverter {
    public User convert(RegisterUserRequest request) {
        return new User(request.getEmail(), request.getPassword(), false, "someRandomString"); // TODO: add random string generation
    }
}
