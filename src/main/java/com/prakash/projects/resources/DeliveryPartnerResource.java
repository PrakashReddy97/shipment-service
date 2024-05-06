package com.prakash.projects.resources;

import com.prakash.projects.dao.DeliveryPartnerDAO;
import com.prakash.projects.db.DeliveryPartner;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.InternalServerErrorException;
import java.util.Optional;

@Path("/delivery-partners")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DeliveryPartnerResource {

    private final DeliveryPartnerDAO deliveryPartnerDao;

    public DeliveryPartnerResource(DeliveryPartnerDAO deliveryPartnerDao) {
        this.deliveryPartnerDao = deliveryPartnerDao;
    }

    @POST
    public Response addDeliveryPartner(DeliveryPartner partner) {
        try {
            if(partner.getIsActive() == null) {
                partner.setIsActive(true);
            }
            long partnerId = deliveryPartnerDao.insert(partner);
            Optional<DeliveryPartner> newDeliveryPartner = deliveryPartnerDao.findById(partnerId);
            return Response.status(Response.Status.CREATED).entity(newDeliveryPartner).build();
        } catch (Exception e) {
            throw new InternalServerErrorException("Internal server error occurred while adding the delivery partner: " + e.getMessage());
        }
    }

    @GET
    @Path("/{id}")
    public Response getDeliveryPartner(@PathParam("id") long id) {
        try {
            return deliveryPartnerDao.findById(id)
                    .map(partner -> Response.ok(partner).build())
                    .orElseGet(() -> Response.status(Response.Status.NOT_FOUND).entity("Delivery partner not found").build());
        } catch (Exception e) {
            throw new InternalServerErrorException("Internal server error occurred: " + e.getMessage());
        }
    }

    @PUT
    @Path("/{id}")
    public Response updateDeliveryPartner(@PathParam("id") long id, DeliveryPartner partner) {
        try {
            partner.setPartnerId(id);
            deliveryPartnerDao.update(partner);
            return Response.ok().entity("Delivery partner updated successfully").build();
        } catch (Exception e) {
            throw new InternalServerErrorException("Internal server error occurred while updating the delivery partner: " + e.getMessage());
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deactivateDeliveryPartner(@PathParam("id") long id) {
        try {
            deliveryPartnerDao.delete(id);
            return Response.status(Response.Status.NO_CONTENT).build();
        } catch (Exception e) {
            throw new InternalServerErrorException("Internal server error occurred while deactivating the delivery partner: " + e.getMessage());
        }
    }
}
