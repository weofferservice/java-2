package org.zcorp.java2.model;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.validation.groups.Default;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

@SuppressWarnings("JpaQlInspection")
@NamedQueries({
        @NamedQuery(name = Meal.BETWEEN, query = "SELECT m FROM Meal m WHERE m.user.id=:userId AND m.dateTime BETWEEN :startDateTime AND :endDateTime ORDER BY m.dateTime DESC"),
        @NamedQuery(name = Meal.ALL_SORTED, query = "SELECT m FROM Meal m WHERE m.user.id=:userId ORDER BY m.dateTime DESC"),
        @NamedQuery(name = Meal.DELETE, query = "DELETE FROM Meal m WHERE m.id=:id AND m.user.id=:userId"),
//        @NamedQuery(name = Meal.UPDATE, query = "UPDATE Meal m SET m.dateTime=:dateTime, m.description=:description, m.calories=:calories WHERE m.id=:id AND m.user.id=:userId")
})
@Entity
@Table(name = "meals", indexes = @Index(name = "meals_unique_user_id_date_time_idx", unique = true, columnList = "user_id, date_time"))
public class Meal extends AbstractBaseEntity {
    public static final String BETWEEN = "Meal.getBetween";
    public static final String ALL_SORTED = "Meal.getAllSorted";
    public static final String DELETE = "Meal.delete";
//    public static final String UPDATE = "Meal.update";

    @Column(name = "date_time", nullable = false, columnDefinition = "TIMESTAMP DEFAULT now()")
    @NotNull(groups = {HttpRequestParamsValidationGroup.class, Default.class})
    private LocalDateTime dateTime;

    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    @NotBlank(groups = {HttpRequestParamsValidationGroup.class, Default.class})
    @Size(min = 2, max = 120, groups = {HttpRequestParamsValidationGroup.class, Default.class})
    private String description;

    @Column(name = "calories", nullable = false, columnDefinition = "INTEGER DEFAULT 1000")
    @NotNull(groups = {HttpRequestParamsValidationGroup.class, Default.class})
    @Range(min = 10, max = 5000, groups = {HttpRequestParamsValidationGroup.class, Default.class})
    private Integer calories;

    // 1) Говорим Hibernate-у, что у нас в БД стоит ON DELETE CASCADE, а значит при удалении родителя (user) будут
    // автоматически удалены дети (meals). Это знание HIbernate использует, чтобы обновить кэш 2-го уровня.
    // 2) Предположение о том, что эта аннотация при генерации скриптов по entity создаст строку ON DELETE CASCADE
    // в этих скриптах - ошибочно
    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "fk_meals", foreignKeyDefinition = "FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE;"))
    @NotNull
    private User user;

    public Meal() {
    }

    public Meal(Meal m) {
        this(m.getId(), m.getDateTime(), m.getDescription(), m.getCalories());
    }

    public Meal(LocalDateTime dateTime, String description, Integer calories) {
        this(null, dateTime, description, calories);
    }

    public Meal(Integer id, LocalDateTime dateTime, String description, Integer calories) {
        super(id);
        Objects.requireNonNull(dateTime, "dateTime must not be null");
        Objects.requireNonNull(description, "description must not be null");
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getDescription() {
        return description;
    }

    public int getCalories() {
        return calories;
    }

    public LocalDate getDate() {
        return dateTime.toLocalDate();
    }

    public LocalTime getTime() {
        return dateTime.toLocalTime();
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCalories(Integer calories) {
        this.calories = calories;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Meal{" +
                "id=" + id +
                ", dateTime=" + dateTime +
                ", description='" + description + '\'' +
                ", calories=" + calories +
                '}';
    }

    public interface HttpRequestParamsValidationGroup {
    }
}
