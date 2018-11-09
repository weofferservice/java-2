package org.zcorp.java2.to;

public class UserTo {
    private final Integer id;

    private final String name;

    private final String email;

    private final String password;

    public UserTo(Integer id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public Integer getId() {
        return id;
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

    public boolean isNew() {
        return id == null;
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
