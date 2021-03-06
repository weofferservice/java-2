package org.zcorp.java2.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.SafeHtml;
import org.springframework.util.CollectionUtils;
import org.zcorp.java2.ValidationGroup;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.*;

@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@NamedQueries({
        @NamedQuery(name = User.DELETE, query = "DELETE FROM User u WHERE u.id=:id"),
        @NamedQuery(name = User.BY_EMAIL, query = "SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.roles WHERE u.email=?1"),
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
    @SafeHtml(groups = {ValidationGroup.Web.class}) // https://stackoverflow.com/questions/17480809/are-xss-attacks-possible-through-email-addresses
    private String email;

    @Column(name = "password", nullable = false, columnDefinition = "VARCHAR")
    @NotBlank
    @Size(min = 5, max = 100)
    //https://stackoverflow.com/questions/12505141/only-using-jsonignore-during-serialization-but-not-deserialization/12505165#12505165
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Column(name = "enabled", nullable = false, columnDefinition = "BOOL DEFAULT TRUE")
    @NotNull
    private Boolean enabled;

    @Column(name = "registered", nullable = false, columnDefinition = "TIMESTAMP DEFAULT now()")
    @NotNull(groups = ValidationGroup.Persist.class)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
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
    @NotNull
    @Range(min = 10, max = 10000)
    private Integer caloriesPerDay;

    /*
    // Заставить Hibernate выполнять отдельные запросы на удаление дочерних объектов (meals) при удалении родителя (user)
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    */
    @OneToMany(mappedBy = "user")
    @OrderBy("dateTime DESC")
//    @JsonIgnore
    private List<Meal> meals;

    public User() {
    }

    public User(User u) {
        this(u.getId(), u.getName(), u.getEmail(), u.getPassword(), u.getCaloriesPerDay(), u.isEnabled(), u.getRegistered(), u.getRoles());
    }

    public User(Integer id, String name, String email, String password, Integer caloriesPerDay, Role role, Role... roles) {
        this(id, name, email, password, caloriesPerDay, true, new Date(), EnumSet.of(role, roles));
    }

    public User(Integer id, String name, String email, String password, Integer caloriesPerDay, Boolean enabled, Date registered, Collection<Role> roles) {
        super(id, name);
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

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public int getCaloriesPerDay() {
        return caloriesPerDay;
    }

    public void setCaloriesPerDay(Integer caloriesPerDay) {
        this.caloriesPerDay = caloriesPerDay;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Collection<Role> roles) {
        this.roles = CollectionUtils.isEmpty(roles) ? EnumSet.noneOf(Role.class) : EnumSet.copyOf(roles);
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
                ", registered=" + registered +
                ", roles=" + roles +
                ", caloriesPerDay=" + caloriesPerDay +
                '}';
    }

}