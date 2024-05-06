package com.prakash.projects.dao;

import com.prakash.projects.db.Customer;
import com.prakash.projects.db.DeliveryPartner;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import java.util.Optional;

public interface DeliveryPartnerDAO {

    @SqlUpdate("INSERT INTO delivery_partners (name, phone, email, is_active) VALUES (:name, :phone, :email, :isActive)")
    @GetGeneratedKeys
    long insert(@BindBean DeliveryPartner partner);

    @SqlQuery("SELECT * FROM delivery_partners WHERE partner_id = :id")
    @RegisterBeanMapper(DeliveryPartner.class)
    Optional<DeliveryPartner> findById(@Bind("id") long id);

    @SqlUpdate("UPDATE delivery_partners SET name = :name, email = :email, phone = :phone, is_active = :isActive WHERE partner_id = :id")
    void update(@BindBean DeliveryPartner deliveryPartner);

    @SqlUpdate("DELETE FROM delivery_partners WHERE partner_id = :id")
    void delete(@Bind("id") long id);
}

