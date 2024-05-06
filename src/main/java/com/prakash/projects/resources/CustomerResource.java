package com.prakash.projects.resources;

import com.prakash.projects.dao.CustomerDAO;
import com.prakash.projects.db.Customer;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.InternalServerErrorException;
import java.util.Optional;

@Path("/customers")
@Produces(MediaType.APPLICATION_JSON)
public class CustomerResource {

    private final CustomerDAO customerDao;

    public CustomerResource(CustomerDAO customerDao) {
        this.customerDao = customerDao;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addCustomer(Customer customer) {
        try {
            if (customer.getIsActive() == null) {
                customer.setIsActive(true); // Default to true if not already set
            }
            long id = customerDao.insert(customer);
            Optional<Customer> newCustomer = customerDao.findById(id);
            return Response.status(Response.Status.CREATED).entity(newCustomer).build();
        } catch (Exception e) {
            throw new InternalServerErrorException("Internal server error occurred while adding the customer: " + e.getMessage());
        }
    }

    @GET
    @Path("/{id}")
    public Response getCustomer(@PathParam("id") long id) {
        try {
            return customerDao.findById(id)
                    .map(customer -> Response.ok(customer).build())
                    .orElseGet(() -> Response.status(Response.Status.NOT_FOUND).entity("Customer not found").build());
        } catch (Exception e) {
            throw new InternalServerErrorException("Internal server error occurred: " + e.getMessage());
        }
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateCustomer(@PathParam("id") long id, Customer customer) {
        try {
            customer.setCustomerId(id);
            customerDao.update(customer);
            return Response.ok().entity("Customer updated successfully").build();
        } catch (Exception e) {
            throw new InternalServerErrorException("Internal server error occurred while updating the customer: " + e.getMessage());
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deleteCustomer(@PathParam("id") long id) {
        try {
            customerDao.delete(id);
            return Response.ok().entity("Customer deleted successfully").build();
        } catch (Exception e) {
            throw new InternalServerErrorException("Internal server error occurred while deleting the customer: " + e.getMessage());
        }
    }
}
