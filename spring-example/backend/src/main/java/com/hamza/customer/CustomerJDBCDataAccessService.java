package com.hamza.customer;


import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("jdbc")
class CustomerJDBCDataAccessService implements CustomerDao {
    private final JdbcTemplate jdbcTemplate;
    private final CustomerRowMapper customerRowMapper;

    CustomerJDBCDataAccessService(JdbcTemplate jdbcTemplate, CustomerRowMapper customerRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.customerRowMapper = customerRowMapper;
    }

    @Override
    public List<Customer> selectAllCustomers() {
        String sql = """
                SELECT id, name, email, age FROM customer
                """;
        return jdbcTemplate.query(sql, customerRowMapper);
    }

    @Override
    public Optional<Customer> selectCustomerById(long id) {
        String sql = """
                SELECT id, name, email, age FROM customer WHERE id=?
                """;
        return jdbcTemplate.query(
                sql, customerRowMapper, id).stream()
                .findFirst();
    }

    @Override
    public void addCustomer(Customer customer) {
        String sql = """
                INSERT INTO customer (name, email, age)
                VALUES(?, ?, ?)
                """;
        jdbcTemplate.update(sql, customer.getName(), customer.getEmail(), customer.getAge());
    }

    @Override
    public boolean customerExistsByEmail(String email) {
        String sql = """
                SELECT id, name, email, age FROM customer WHERE email=?
                """;
        List<Customer> list = jdbcTemplate.query(sql, customerRowMapper, email);
        return list.size() > 0 ? true: false;
    }

    @Override
    public void removeCustomerById(long id) {
        String sql = """
                DELETE FROM customer WHERE id=?
                """;
        jdbcTemplate.update(sql, id);
    }

    @Override
    public void updateCustomer(Customer customer) {
        String sql = """
                UPDATE customer set name=?, email=?, age=?
                WHERE id=?
                """;
        jdbcTemplate.update(sql, customer.getName(), customer.getEmail(), customer.getAge(), customer.getId());
    }
}
