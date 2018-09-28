package org.zcorp.java2.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.zcorp.java2.model.Role;
import org.zcorp.java2.model.User;
import org.zcorp.java2.repository.UserRepository;

import java.util.*;

@Repository
@Transactional(readOnly = true)
public class JdbcUserRepositoryImpl implements UserRepository {

    private static final RowMapper<User> ROW_MAPPER = BeanPropertyRowMapper.newInstance(User.class);

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
            insertRoles(user);
        } else {
            if (namedParameterJdbcTemplate.update(
                    "UPDATE users SET name=:name, email=:email, password=:password, " +
                            "registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id", parameterSource) == 0) {
                return null;
            }
            // Более правильно, но и более сложно:
            // 1) получать роли из БД и сравнивать их с теми, что есть в объекте user.
            // 2) если роли были изменены, то в java высчитываем отличия, и уже тогда делаем нужные delete/insert ролей.
            // Мы упростим себе задачу, будем удалять все роли и сохранять их заново даже, если они не изменялись
            deleteRoles(user);
            insertRoles(user);
        }
        return user;
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }

    @Override
    public User get(int id) {
        List<User> users = jdbcTemplate.query("SELECT * FROM users WHERE id=?", ROW_MAPPER, id);
        return setRoles(DataAccessUtils.singleResult(users));
    }

    @Override
    public User getByEmail(String email) {
//        return jdbcTemplate.queryForObject("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
        List<User> users = jdbcTemplate.query("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
        return setRoles(DataAccessUtils.singleResult(users));
    }

    @Override
    public List<User> getAll() {
        // Получаем все роли из таблицы user_roles
        Map<Integer, Set<Role>> roles = new HashMap<>();
        jdbcTemplate.query("SELECT * FROM user_roles",
                // RowCallbackHandler (вызывается для каждой строки)
                rs -> {
                    roles.computeIfAbsent(rs.getInt("user_id"), userId -> EnumSet.noneOf(Role.class))
                            .add(Role.valueOf(rs.getString("role")));
                }
        );

        // Получаем всех юзеров из таблицы users
        List<User> users = jdbcTemplate.query("SELECT * FROM users ORDER BY name, email", ROW_MAPPER);

        // Не нарушая порядок сортировки юзеров, в каждого юзера загоняем, полученные из таблицы user_roles, роли это юзера
        users.forEach(user -> user.setRoles(roles.get(user.getId())));

        return users;
    }

    private void insertRoles(User user) {
        Set<Role> roles = user.getRoles();
        if (!CollectionUtils.isEmpty(roles)) {
            jdbcTemplate.batchUpdate("INSERT INTO user_roles (user_id, role) VALUES (?, ?)", roles, roles.size(),
                    (ps, role) -> {
                        ps.setInt(1, user.getId());
                        ps.setString(2, role.name());
                    }
            );
        }
    }

    private void deleteRoles(User user) {
        jdbcTemplate.update("DELETE FROM user_roles WHERE user_id=?", user.getId());
    }

    private User setRoles(User user) {
        if (user != null) {
            List<Role> roles = jdbcTemplate.query("SELECT role FROM user_roles WHERE user_id=?",
                    (rs, rowNum) -> Role.valueOf(rs.getString("role")), // inline RowMapper
                    user.getId());
            user.setRoles(roles);
        }
        return user;
    }

}
