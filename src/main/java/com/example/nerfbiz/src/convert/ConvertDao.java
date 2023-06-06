package com.example.nerfbiz.src.convert;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class ConvertDao {

    private JdbcTemplate jdbcTemplate;

    public ConvertDao(DataSource dataSource){this.jdbcTemplate = new JdbcTemplate(dataSource);}

    public int lastInsertId(){
        String lastInsertIdQuery = "select last_insert_id() from object";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery,int.class);
    }

}
