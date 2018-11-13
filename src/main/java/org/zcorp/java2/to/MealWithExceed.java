package org.zcorp.java2.to;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonView;
import org.zcorp.java2.util.DateTimeUtil;
import org.zcorp.java2.web.json.View;

import java.time.LocalDateTime;
import java.util.Objects;

public class MealWithExceed extends BaseTo {
    @JsonView(View.JsonREST.class)
    private final LocalDateTime dateTime;

    private final String description;

    private final int calories;

    private final boolean exceed;

    public MealWithExceed(Integer id, LocalDateTime dateTime, String description, int calories, boolean exceed) {
        super(id);
        this.dateTime = Objects.requireNonNull(dateTime, "dateTime must not be null");
        this.description = Objects.requireNonNull(description, "description must not be null");
        this.calories = calories;
        this.exceed = exceed;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    @JsonGetter
    @JsonView(View.JsonUI.class)
    @JsonFormat(pattern = DateTimeUtil.DATE_TIME_PATTERN)
    public LocalDateTime getDateTimeUI() {
        return dateTime;
    }

    public String getDescription() {
        return description;
    }

    public int getCalories() {
        return calories;
    }

    public boolean isExceed() {
        return exceed;
    }

    @Override
    public String toString() {
        return "MealWithExceed{" +
                "id=" + id +
                ", dateTime=" + dateTime +
                ", description='" + description + "'" +
                ", calories=" + calories +
                ", exceed=" + exceed +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MealWithExceed that = (MealWithExceed) o;

        if (calories != that.calories) return false;
        if (exceed != that.exceed) return false;
        if (!id.equals(that.id)) return false;
        if (!dateTime.equals(that.dateTime)) return false;
        return description.equals(that.description);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + dateTime.hashCode();
        result = 31 * result + description.hashCode();
        result = 31 * result + calories;
        result = 31 * result + (exceed ? 1 : 0);
        return result;
    }
}
