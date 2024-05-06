package com.prakash.projects;

import com.codahale.metrics.health.HealthCheck;
import com.prakash.projects.dao.CustomerDAO;
import com.prakash.projects.dao.DeliveryPartnerDAO;
import com.prakash.projects.dao.ShipmentDAO;
import com.prakash.projects.resources.CustomerResource;
import com.prakash.projects.resources.DeliveryPartnerResource;
import com.prakash.projects.resources.HelloWorldResource;
import com.prakash.projects.resources.ShipmentResource;
import io.dropwizard.Application;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.jdbi3.JdbiFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.jdbi.v3.core.Jdbi;
import io.dropwizard.migrations.MigrationsBundle;

public class ShipmentServiceExampleApplication extends Application<ShipmentServiceExampleConfiguration> {

    public static void main(final String[] args) throws Exception {
        new ShipmentServiceExampleApplication().run(args);
    }

    @Override
    public String getName() {
        return "ShipmentServiceExample";
    }

    @Override
    public void initialize(final Bootstrap<ShipmentServiceExampleConfiguration> bootstrap) {
        // Adding the MigrationsBundle to manage database migrations
        bootstrap.addBundle(new MigrationsBundle<ShipmentServiceExampleConfiguration>() {
            @Override
            public DataSourceFactory getDataSourceFactory(ShipmentServiceExampleConfiguration configuration) {
                return configuration.getDataSourceFactory();
            }
        });
    }

    @Override
    public void run(final ShipmentServiceExampleConfiguration configuration,
                    final Environment environment) {

        final JdbiFactory factory = new JdbiFactory();
        final Jdbi jdbi = factory.build(environment, configuration.getDataSourceFactory(), "shipment-service");

        environment.jersey().register(new HelloWorldResource());
        environment.healthChecks().register("HealthCheck", new HealthCheck() {
            @Override
            protected Result check() throws Exception {
                return Result.healthy();
            }
        });

        final CustomerDAO customerDao = jdbi.onDemand(CustomerDAO.class);
        environment.jersey().register(new CustomerResource(customerDao));

        final DeliveryPartnerDAO deliveryPartnerDAO = jdbi.onDemand(DeliveryPartnerDAO.class);
        environment.jersey().register(new DeliveryPartnerResource(deliveryPartnerDAO));

        final ShipmentDAO shipmentDAO = jdbi.onDemand(ShipmentDAO.class);
        environment.jersey().register(new ShipmentResource(shipmentDAO));
    }

}
