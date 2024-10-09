package com.test.annotation;

import org.springframework.jdbc.core.JdbcTemplate;


public class ModelBeanConfig {

    private JdbcTemplate jdbcTemplate;

    public ModelBeanConfig(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }
}
