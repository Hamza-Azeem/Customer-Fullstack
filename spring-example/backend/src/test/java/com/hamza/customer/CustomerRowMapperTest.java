package com.hamza.customer;

import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CustomerRowMapperTest {

    @Test
    void mapRow() throws SQLException {
        // Arrange
        CustomerRowMapper customerRowMapper = new CustomerRowMapper();
        ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.getLong("id")).thenReturn(1L);
        when(resultSet.getString("name")).thenReturn("hamza");
        when(resultSet.getString("email")).thenReturn("hamza@gmail.com");
        when(resultSet.getInt("age")).thenReturn(22);
        // Act
        Customer actual = customerRowMapper.mapRow(resultSet, 1);
        // Assert
        assertThat(actual.getId()).isEqualTo(1);
        assertThat(actual.getName()).isEqualTo("hamza");
        assertThat(actual.getEmail()).isEqualTo("hamza@gmail.com");
        assertThat(actual.getAge()).isEqualTo(22);
    }
}