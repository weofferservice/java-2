package org.zcorp.java2.model;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.constraints.Range;
import org.springframework.util.CollectionUtils;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.*;

import static org.zcorp.java2.util.MealsUtil.DEFAULT_CALORIES_PER_DAY;

@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@NamedQueries({
        @NamedQuery(name = User.DELETE, query = "DELETE FROM User u WHERE u.id=:id"),
        @NamedQuery(name = User.BY_EMAIL, query = "SELECT u FROM User u LEFT JOIN FETCH u.roles WHERE u.email=?1"),
        @NamedQuery(name = User.ALL_SORTED, query = "SELECT u FROM User u ORDER BY u.name, u.email"),
        @NamedQuery(name = User.UPDATE, query = "UPDATE User u SET u.name=:name, u.email=:email, u.password=:password, u.enabled=:enabled, u.registered=:registered, u.caloriesPerDay=:caloriesPerDay WHERE u.id=:id")
})
@Entity
//@NamedEntityGraph(name = User.GRAPH_WITH_MEALS, attributeNodes = {@NamedAttributeNode("meals")})
@Table(name = "users", uniqueConstraints = {@UniqueConstraint(columnNames = "email", name = "users_unique_email_idx")})
public class User extends AbstractNamedEntity {

//    public static final String GRAPH_WITH_MEALS = "User.withMeals";

    public static final String DELETE = "User.delete";
    public static final String BY_EMAIL = "User.getByEmail";
    public static final String ALL_SORTED = "User.getAllSorted";
    public static final String UPDATE = "User.update";

    @Column(name = "email", nullable = false, columnDefinition = "VARCHAR")
    @Email
    @NotBlank
    @Size(max = 100)
    private String email;

    @Column(name = "password", nullable = false, columnDefinition = "VARCHAR")
    @NotBlank
    @Size(min = 5, max = 100)
    private String password;

    @Column(name = "enabled", nullable = false, columnDefinition = "BOOL DEFAULT TRUE")
    private boolean enabled;

    @Column(name = "registered", nullable = false, columnDefinition = "TIMESTAMP DEFAULT now()")
    @NotNull
    private Date registered;

    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @CollectionTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id", nullable = false),
            foreignKey = @ForeignKey(name = "fk_user_roles", foreignKeyDefinition = "FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE;"),
            uniqueConstraints = @UniqueConstraint(name = "user_roles_idx", columnNames = {"user_id", "role"})
    )
    @Column(name = "role", nullable = false, columnDefinition = "VARCHAR")
    @Enumerated(EnumType.STRING)
    @ElementCollection(fetch = FetchType.EAGER)
//    @Fetch(FetchMode.SUBSELECT)
    @BatchSize(size = 200)
    private Set<Role> roles;

    @Column(name = "calories_per_day", nullable = false, columnDefinition = "INTEGER DEFAULT 2000")
    @Range(min = 10, max = 10000)
    private int caloriesPerDay;

    @OneToMany(mappedBy = "user")
    @OrderBy("dateTime DESC")
    private List<Meal> meals;

    public User() {
    }

    public User(User u) {
        this(u.getId(), u.getName(), u.getEmail(), u.getPassword(), u.getCaloriesPerDay(), u.isEnabled(), u.getRegistered(), u.getRoles());
    }

    public User(Integer id, String name, String email, String password, Role role, Role... roles) {
        this(id, name, email, password, DEFAULT_CALORIES_PER_DAY, true, new Date(), EnumSet.of(role, roles));
    }

    public User(Integer id, String name, String email, String password, int caloriesPerDay, boolean enabled, Date registered, Collection<Role> roles) {
        super(id, name);
        Objects.requireNonNull(email, "email must not be null");
        Objects.requireNonNull(password, "password must not be null");
        Objects.requireNonNull(registered, "registered must not be null");
        Objects.requireNonNull(roles, "roles must not be null");
        this.email = email;
        this.password = password;
        this.caloriesPerDay = caloriesPerDay;
        this.enabled = enabled;
        this.registered = registered;
        setRoles(roles);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getRegistered() {
        return registered;
    }

    public void setRegistered(Date registered) {
        this.registered = registered;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public int getCaloriesPerDay() {
        return caloriesPerDay;
    }

    public void setCaloriesPerDay(int caloriesPerDay) {
        this.caloriesPerDay = caloriesPerDay;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Collection<Role> roles) {
        this.roles = CollectionUtils.isEmpty(roles) ? Collections.emptySet() : EnumSet.copyOf(roles);
    }

    public List<Meal> getMeals() {
        return meals;
    }

    public void setMeals(List<Meal> meals) {
        this.meals = meals;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email=" + email +
                ", name=" + name +
                ", enabled=" + enabled +
                ", roles=" + roles +
                ", caloriesPerDay=" + caloriesPerDay +
                '}';
    }

}