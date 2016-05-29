package in.ramanujam.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
public class User {

    @Id
    @Column(name = "email", unique = true, nullable = false) // TODO: do we need to specify the column name?
    private String email; // TODO: add validation constraints
    @NotNull
    @Column
    private String password;
    @NotNull
    @Column
    private boolean isConfirmed;
    @Column
    private String confirmationCode;

    public User() {

    }

    public User(String email, String password, boolean isConfirmed, String confirmationCode) {
        this.email = email;
        this.password = password;
        this.isConfirmed = isConfirmed;
        this.confirmationCode = confirmationCode;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public boolean isConfirmed() {
        return isConfirmed;
    }

    public void setConfirmed(boolean confirmed) {
        isConfirmed = confirmed;
    }

    public String getConfirmationCode() {
        return confirmationCode;
    }

    public void setConfirmationCode(String code) {
        confirmationCode = code;
    }
}
