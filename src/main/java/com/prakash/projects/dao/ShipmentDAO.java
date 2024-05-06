package com.prakash.projects.dao;

import com.prakash.projects.db.Shipment;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;
import java.util.Optional;

public interface ShipmentDAO {

    @SqlUpdate("INSERT INTO shipments (customer_id, partner_id, source_location, target_location, item_description, status) VALUES (:customerId, :partnerId, :sourceLocation, :targetLocation, :itemDescription, :status)")
    @GetGeneratedKeys
    long insert(@BindBean Shipment shipment);

    @SqlQuery("SELECT * FROM shipments WHERE shipment_id = :shipmentId")
    @RegisterBeanMapper(Shipment.class)
    Optional<Shipment> findById(@Bind("shipmentId") long shipmentId);

    @SqlUpdate("UPDATE shipments SET customer_id = :customerId, partner_id = :partnerId, source_location = :sourceLocation, target_location = :targetLocation, item_description = :itemDescription, status = :status WHERE shipment_id = :shipmentId")
    void update(@BindBean Shipment shipment);

    @SqlUpdate("UPDATE shipments SET partner_id = :partnerId WHERE shipment_id = :shipmentId")
    void assignToPartner(@Bind("shipmentId") long shipmentId, @Bind("partnerId") long partnerId);

    @SqlUpdate("DELETE FROM shipments WHERE shipment_id = :shipmentId")
    void delete(@Bind("shipmentId") long shipmentId);

    @SqlQuery("SELECT * FROM shipments WHERE partner_id = :partnerId")
    @RegisterBeanMapper(Shipment.class)
    List<Shipment> findByPartnerId(@Bind("partnerId") long partnerId);

    @SqlUpdate("UPDATE shipments SET status = :status WHERE shipment_id = :shipmentId AND partner_id = :partnerId")
    void updateStatus(@Bind("shipmentId") long shipmentId, @Bind("partnerId") long partnerId, @Bind("status") Shipment.ShipmentStatus status);
}
