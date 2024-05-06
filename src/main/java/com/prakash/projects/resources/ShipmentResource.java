package com.prakash.projects.resources;

import com.prakash.projects.dao.ShipmentDAO;
import com.prakash.projects.db.Shipment;
import com.prakash.projects.db.Shipment.ShipmentStatus;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/shipments")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ShipmentResource {

    private final ShipmentDAO shipmentDao;

    public ShipmentResource(ShipmentDAO shipmentDao) {
        this.shipmentDao = shipmentDao;
    }

    @POST
    public Response createShipment(Shipment shipment) {
        try {
           // System.out.println("Received shipment: " + shipment.toString());
            long shipmentId = shipmentDao.insert(shipment);

            // Retrieve the complete shipment details including any default or auto-generated fields
            Shipment fullShipmentDetails = shipmentDao.findById(shipmentId)
                    .orElseThrow(() -> new IllegalStateException("Newly created shipment not found."));

            // Return the full shipment details
            return Response.status(Response.Status.CREATED).entity(fullShipmentDetails).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error creating shipment: " + e.getMessage()).build();
        }
    }


    @GET
    @Path("/{id}")
    public Response getShipment(@PathParam("id") long id) {
        try {
            return shipmentDao.findById(id)
                    .map(shipment -> Response.ok(shipment).build())
                    .orElseGet(() -> Response.status(Response.Status.NOT_FOUND).entity("Shipment not found").build());
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error retrieving shipment: " + e.getMessage()).build();
        }
    }

    @PUT
    @Path("/{id}/status")
    public Response updateShipmentStatus(@PathParam("id") long id, @QueryParam("partnerId") long partnerId, @QueryParam("status") ShipmentStatus status) {
        try {
            shipmentDao.updateStatus(id, partnerId, status);
            return Response.ok().entity("Status updated to " + status).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error updating shipment status: " + e.getMessage()).build();
        }
    }

    @PUT
    @Path("/{shipmentId}/assign")
    public Response assignShipmentToPartner(@PathParam("shipmentId") long shipmentId, @QueryParam("partnerId") long partnerId) {
        try {
            shipmentDao.assignToPartner(shipmentId, partnerId);
            return Response.ok().entity("Shipment " + shipmentId + " has been assigned to partner " + partnerId).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error assigning shipment to partner: " + e.getMessage()).build();
        }
    }

    @GET
    @Path("/delivery-partner/{partnerId}")
    public Response getShipmentsForPartner(@PathParam("partnerId") long partnerId) {
        try {
            List<Shipment> shipments = shipmentDao.findByPartnerId(partnerId);
            return Response.ok().entity(shipments).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error retrieving shipments for delivery partner: " + e.getMessage()).build();
        }
    }
}
