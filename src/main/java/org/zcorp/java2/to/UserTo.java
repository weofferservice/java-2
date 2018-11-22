package org.zcorp.java2.to;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.SafeHtml;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

import static org.zcorp.java2.util.UserUtil.DEFAULT_CALORIES_PER_DAY;

public class UserTo extends BaseTo implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotBlank
    @Size(min = 2, max = 100)
    @SafeHtml
    private final String name;

    @Email
    @NotBlank
    @Size(max = 100)
    @SafeHtml // https://stackoverflow.com/questions/17480809/are-xss-attacks-possible-through-email-addresses
    private final String email;

    @NotBlank
    @Size(min = 5, max = 32)
    private final String password;

    @NotNull
    @Range(min = 10, max = 10000)
    private final Integer caloriesPerDay;

    @JsonCreator
    public UserTo(@JsonProperty("id") Integer id,
                  @JsonProperty("name") String name,
                  @JsonProperty("email") String email,
                  @JsonProperty("password") String password,
                  @JsonProperty("caloriesPerDay") Integer caloriesPerDay) {
        super(id);
        this.name = name;
        this.email = email;
        this.password = password;
        this.caloriesPerDay = caloriesPerDay != null ? caloriesPerDay : DEFAULT_CALORIES_PER_DAY;
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

    public Integer getCaloriesPerDay() {
        return caloriesPerDay;
    }

    @Override
    public String toString() {
        return "UserTo{" +
                "id=" + id +
                ", name=" + name +
                ", email=" + email +
                ", caloriesPerDay=" + caloriesPerDay +
                '}';
    }
}
