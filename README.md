# ShipmentServiceExample

How to start the ShipmentServiceExample application
---

1. Run `mvn clean install` to build your application
1. Start application with `java -jar target/shipment-service-1.0-SNAPSHOT.jar server config.yml`
1. To check that your application is running enter url `http://localhost:8080`

Health Check
---

To see your applications health enter url `http://localhost:8081/healthcheck`

**APIs built for following usecases**
1. Create Customers, Delivery Partners --> On-boarding
2. Delete Customers, Delivery Partners --> Off-boarding
3. Create Shipments
4. Assign Shipments to delivery Partners
5. Fetch list of Shipments assigned to delivery partner
6. Update Status of shipment based on shipment id.


