package co.com.sofka.cuentaflex.libs.infrastructure.entry_points.auth_webservice.data;

import java.util.UUID;

public final class RegisterDto {
    private String firstName;
    private String lastName;
    private String identification;
    private UUID customerId;
    private String username;
    private String encryptedPassword;

    public RegisterDto() {}

    public RegisterDto(String firstName, String lastName, String identification, UUID customerId, String username, String encryptedPassword) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.identification = identification;
        this.customerId = customerId;
        this.username = username;
        this.encryptedPassword = encryptedPassword;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getIdentification() {
        return identification;
    }

    public void setIdentification(String identification) {
        this.identification = identification;
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public void setCustomerId(UUID customerId) {
        this.customerId = customerId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEncryptedPassword() {
        return encryptedPassword;
    }

    public void setEncryptedPassword(String encryptedPassword) {
        this.encryptedPassword = encryptedPassword;
    }
}
