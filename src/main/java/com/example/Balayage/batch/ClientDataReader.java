/*
package com.example.Balayage.batch;

import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.support.PostgresPagingQueryProvider;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

public class ClientDataReader {

    @Autowired
    private DataSource dataSource;

    public JdbcPagingItemReader<Student> getPaginationReader(Student student) {
        final JdbcPagingItemReader<Student> reader = new JdbcPagingItemReader<>();
        final StudentMapper studentMapper = new StudentMapper();
        reader.setDataSource(dataSource);
        reader.setFetchSize(100);
        reader.setPageSize(100);
        reader.setRowMapper(studentMapper);
        reader.setQueryProvider(createQuery());
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("id", student.getId());
        parameters.put("name", student.getName());
        reader.setParameterValues(parameters);
        return reader;
    }

    //TODO Comment these
    private String getFromClause() {
        return "( SELECT * from CLIENTS where id = :id) AS RESULT_TABLE ";
    }
    //TODO comment these
    private PostgresPagingQueryProvider createQuery() {
        final Map<String, Order> sortKeys = new HashMap<>();
        sortKeys.put("id", Order.ASCENDING);
        final PostgresPagingQueryProvider queryProvider = new PostgresPagingQueryProvider();
        queryProvider.setSelectClause("*");
        queryProvider.setFromClause(getFromClause());
        queryProvider.setSortKeys(sortKeys);
        return queryProvider;
    }



}
*/
