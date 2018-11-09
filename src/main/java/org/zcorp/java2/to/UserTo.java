package org.zcorp.java2.to;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class UserTo extends BaseTo {
    @NotBlank
    @Size(min = 2, max = 100)
    private final String name;

    @Email
    @NotBlank
    @Size(max = 100)
    private final String email;

    @Size(min = 5, max = 32, message = "length must be between 5 and 32 characters")
    private final String password;

    @JsonCreator
    public UserTo(@JsonProperty("id") Integer id,
                  @JsonProperty("name") String name,
                  @JsonProperty("email") String email,
                  @JsonProperty("password") String password) {
        super(id);
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return "UserTo{" +
                "id=" + id +
                ", name=" + name +
                ", email=" + email +
                '}';
    }
}
