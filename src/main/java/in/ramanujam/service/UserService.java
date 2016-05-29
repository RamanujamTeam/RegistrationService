package in.ramanujam.service;

import in.ramanujam.model.User;
import in.ramanujam.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    public void registerUser(User user) { // TODO: additional - we can hash the password before saving
        repository.save(user);        //TODO: test
    }

    public boolean emailExists(String email) {
        return repository.findOne(email) != null; //TODO: test
    }

    public void confirmEmail(String email) {
        User user = repository.findOne(email); // TODO test
        user.setConfirmationCode(null);
        user.setConfirmed(true);
        repository.save(user);
    }

    public User findByEmail(String email) {
        return repository.findOne(email);
    }

}
