package org.zcorp.java2.to;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UserTo extends BaseTo {
    private final String name;

    private final String email;

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
