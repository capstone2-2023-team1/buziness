package com.example.nerfbiz.src.user;

import com.example.nerfbiz.src.user.model.PostUserReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class UserDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public int createUser(PostUserReq postUserReq){
        String createUserQuery = "insert into user (id, password, email) VALUES (?, ?, ?)";
        Object[] createUserParams = new Object[]{postUserReq.getId(), postUserReq.getPassword(), postUserReq.getEmail()};
        this.jdbcTemplate.update(createUserQuery, createUserParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery,int.class);

    }

    public int checkEmail(String email){
        String checkEmailQuery = "select exists(select email from user where email = ?)";
        String checkEmailParams = email;
        return this.jdbcTemplate.queryForObject(checkEmailQuery, int.class, checkEmailParams);
    }

}
