package org.zcorp.java2.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.zcorp.java2.model.Role;
import org.zcorp.java2.model.User;
import org.zcorp.java2.repository.UserRepository;

import java.util.*;

@Repository
@Transactional(readOnly = true)
public class JdbcUserRepositoryImpl implements UserRepository {

    private static final ResultSetExtractor<List<User>> USER_LIST_RESULT_SET_EXTRACTOR = rs -> {
        Map<Integer, User> users = new LinkedHashMap<>();
        while (rs.next()) {
            int id = rs.getInt("id");
            User user = users.get(id);
            if (user == null) {
                String name = rs.getString("name");
                String email = rs.getString("email");
                String password = rs.getString("password");
                boolean enabled = rs.getBoolean("enabled");
                Date registered = rs.getTimestamp("registered");
                int caloriesPerDay = rs.getInt("calories_per_day");
                user = new User(id, name, email, password, caloriesPerDay, enabled, registered, Collections.emptySet());
                users.put(id, user);
            }
            String role = rs.getString("role");
            if (role != null) {
                user.getRoles().add(Role.valueOf(role));
            }
        }
        return new ArrayList<>(users.values());
    };

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;

    @Autowired
    public JdbcUserRepositoryImpl(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.insertUser = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    @Transactional
    public User save(User user) {
        SqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);

        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            user.setId(newKey.intValue());
        } else if (namedParameterJdbcTemplate.update(
                "UPDATE users SET name=:name, email=:email, password=:password, " +
                        "registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id", parameterSource) == 0) {
            return null;
        } else {
            jdbcTemplate.update("DELETE FROM user_roles WHERE user_id=?", user.getId());
        }

        SqlParameterSource[] roles = user.getRoles().stream()
                .map(role -> new MapSqlParameterSource()
                        .addValue("role", role.name())
                        .addValue("user_id", user.getId()))
                .toArray(MapSqlParameterSource[]::new);
        namedParameterJdbcTemplate.batchUpdate(
                "INSERT INTO user_roles (role, user_id) VALUES (:role, :user_id)", roles);

        return user;
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }

    @Override
    public User get(int id) {
        List<User> users = jdbcTemplate.query(
                "SELECT u.*, r.role FROM users u LEFT JOIN user_roles r ON u.id=r.user_id WHERE id=?",
                USER_LIST_RESULT_SET_EXTRACTOR, id);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public User getByEmail(String email) {
//        return jdbcTemplate.queryForObject("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
        List<User> users = jdbcTemplate.query(
                "SELECT u.*, r.role FROM users u LEFT JOIN user_roles r ON u.id=r.user_id WHERE email=?",
                USER_LIST_RESULT_SET_EXTRACTOR, email);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public List<User> getAll() {
        return jdbcTemplate.query(
                "SELECT u.*, r.role FROM users u LEFT JOIN user_roles r ON u.id=r.user_id ORDER BY u.name, u.email",
                USER_LIST_RESULT_SET_EXTRACTOR);
    }

}
