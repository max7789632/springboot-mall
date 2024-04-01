package com.maxdeng.springbootmall.dao.impl;

import com.maxdeng.springbootmall.dao.UserDao;
import com.maxdeng.springbootmall.dto.UserRegisterRequest;
import com.maxdeng.springbootmall.model.Role;
import com.maxdeng.springbootmall.model.User;
import com.maxdeng.springbootmall.rowmapper.RoleRowMapper;
import com.maxdeng.springbootmall.rowmapper.UserRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class UserDaoImpl implements UserDao {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public User getUserById(Integer userId) {
        String sql = """
                SELECT user_id, email, password, created_date, last_modified_date
                FROM user WHERE user_id = :userId
                """;

        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);

        List<User> userList = namedParameterJdbcTemplate.query(sql, map, new UserRowMapper());

        if (!userList.isEmpty()) {
            return userList.get(0);
        } else {
            return null;
        }

    }

    @Override
    public User getUserByEmail(String email) {
        String sql = """
                SELECT user_id, email, password, created_date, last_modified_date
                FROM user WHERE email = :email
                """;

        Map<String, Object> map = new HashMap<>();
        map.put("email", email);

        List<User> userList = namedParameterJdbcTemplate.query(sql, map, new UserRowMapper());

        if (!userList.isEmpty()) {
            return userList.get(0);
        } else {
            return null;
        }
    }

    @Override
    public Integer createUser(UserRegisterRequest userRegisterRequest) {
        String sql = """
                INSERT INTO user (email, password, created_date, last_modified_date)
                VALUES(:email, :password, :createdDate, :lastModifiedDate)
                """;

        Map<String, Object> map = new HashMap<>();
        map.put("email", userRegisterRequest.getEmail());
        map.put("password", userRegisterRequest.getPassword());

        Date now = new Date();
        map.put("createdDate", now);
        map.put("lastModifiedDate", now);

        KeyHolder keyHolder = new GeneratedKeyHolder();

        namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource(map), keyHolder);

        int userId = keyHolder.getKey().intValue();

        return userId;
    }

    @Override
    public List<Role> getRolesByUserId(Integer userId) {
        String sql = """
                SELECT role.role_id, role.role_name FROM role
                JOIN user_has_role ON role.role_id = user_has_role.role_id
                WHERE user_has_role.user_id = :userId
                """;

        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);

        List<Role> userList = namedParameterJdbcTemplate.query(sql, map, new RoleRowMapper());

        return userList;
    }

    @Override
    public void createUserHasRole(Integer userId) {
        String sql = """
                INSERT INTO user_has_role (user_id, role_id)
                VALUES(:user_id, :role_id)
                """;

        Map<String, Object> map = new HashMap<>();
        map.put("user_id", userId);
        map.put("role_id", "2"); // 新會員固定權限 2:ROLE_NORMAL_USER

        KeyHolder keyHolder = new GeneratedKeyHolder();

        namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource(map), keyHolder);
    }
}
