package com.prakash.projects.dao;

import com.prakash.projects.db.Customer;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.Optional;

public interface CustomerDAO {

    @SqlUpdate("INSERT INTO customers (name, email, phone, is_active) VALUES (:name, :email, :phone, :isActive)")
    @GetGeneratedKeys
    long insert(@BindBean Customer customer);

    @SqlQuery("SELECT * FROM customers WHERE customer_id = :id")
    @RegisterBeanMapper(Customer.class)
    Optional<Customer> findById(@Bind("id") long id);

    @SqlUpdate("UPDATE customers SET name = :name, email = :email, phone = :phone, is_active = :isActive WHERE customer_id = :id")
    void update(@BindBean Customer customer);

    @SqlUpdate("DELETE FROM customers WHERE customer_id = :id")
    void delete(@Bind("id") long id);
}
