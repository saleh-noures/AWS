package com.noures.awscourse.Lab3;


import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.*;

import java.util.ArrayList;

// The ReservationsTableCreator class creates a table and global secondary index to store data about reservations
// This sample uses the DynamoDB document API
// Run this class first to create the table
public class ReservationsTableCreator {

    public static final String RESERVATIONS_TABLE_NAME = "Reservations";
    public static final String CITY_DATE_INDEX_NAME = "ReservationsByCityDate";
    public static DynamoDB dynamoDB = null;
    public static AmazonDynamoDB dynamoDBClient = null;

    public static void main(String[] args) throws Exception {
        try {

            // Instantiate DynamoDB client and object
            dynamoDBClient = AmazonDynamoDBClientBuilder.standard().build();

            // Create an instance of DynamoDB class
            dynamoDB = new DynamoDB(dynamoDBClient);

            // Remove the table if it already exists
            removeReservationsTableIfExists();

            // Create the reservations table and index
            createReservationsTableWrapper();

        } catch (AmazonServiceException ase) {
            System.out.println("Error Message:    " + ase.getMessage());
            System.out.println("HTTP Status Code: " + ase.getStatusCode());
            System.out.println("AWS Error Code:   " + ase.getErrorCode());
            System.out.println("Error Type:       " + ase.getErrorType());
            System.out.println("Request ID:       " + ase.getRequestId());
        } catch (AmazonClientException ace) {
            System.out.println("Error Message: " + ace.getMessage());
        }
    }

    private static void removeReservationsTableIfExists() {
        try {
            Table table = dynamoDB.getTable(RESERVATIONS_TABLE_NAME);
            DescribeTableResult descTableResult = dynamoDBClient.describeTable(RESERVATIONS_TABLE_NAME);
            if (descTableResult != null && descTableResult.getTable().getTableStatus().equals("ACTIVE")) {
                System.out.println("The Reservations table already exists. To start fresh with the Java lab, it will now be deleted.");
                table.delete();
                table.waitForDelete();
            }
        } catch (ResourceNotFoundException e) {
            System.out.printf("The %s table does not exist. \n", RESERVATIONS_TABLE_NAME);
        } catch (InterruptedException ie) {
        }
    }

    private static void createReservationsTableWrapper() {
        // Create attribute definitions for the primary key attributes of the table and
        // indexes
        ArrayList<AttributeDefinition> attributeDefinitions = new ArrayList<AttributeDefinition>();
        attributeDefinitions.add(new AttributeDefinition().withAttributeName("CustomerId").withAttributeType("S"));
        attributeDefinitions.add(new AttributeDefinition().withAttributeName("City").withAttributeType("S"));
        attributeDefinitions.add(new AttributeDefinition().withAttributeName("Date").withAttributeType("S"));

        // Create key schema element for the table's primary key attribute
        KeySchemaElement tableKeySchemaElem = new KeySchemaElement().withAttributeName("CustomerId")
                .withKeyType(KeyType.HASH);

        // Create object to specify table's provisioned throughput
        ProvisionedThroughput tableProvisionedThroughput = new ProvisionedThroughput(5L, 10L);

        // Create global secondary index object
        // The code uses fluent setter methods to initialize the GlobalSecondaryIndex
        // object
        // Remember, creating GSI will create a new table for the original one with the new index. No wonder it is expensive!
        GlobalSecondaryIndex gsi = new GlobalSecondaryIndex().withIndexName(CITY_DATE_INDEX_NAME)
                .withKeySchema(new KeySchemaElement("City", KeyType.HASH), new KeySchemaElement("Date", KeyType.RANGE))
                .withProvisionedThroughput(new ProvisionedThroughput(5L, 5L))
                .withProjection(new Projection().withProjectionType(ProjectionType.ALL));

        // Create the table and index by using the given parameters
        createReservationsTableWithIndex(dynamoDB, RESERVATIONS_TABLE_NAME, attributeDefinitions, tableKeySchemaElem,
                tableProvisionedThroughput, gsi);
    }

    /**
     * Create a DynamoDB table
     *
     * @param dynamoDB
     *            Instance of DynamoDB class
     * @param tableName
     *            Table name
     * @param attributeDefinitions
     *            Attribute definitions for the table
     * @param tableKeySchemaElem
     *            Table's key schema element
     * @param tableProvisionedThroughput
     *            Provisioned Throughput
     * @param gsi
     *            Global secondary index
     */
    public static void createReservationsTableWithIndex(DynamoDB dynamoDB, String tableName,
                                                        ArrayList<AttributeDefinition> attributeDefinitions, KeySchemaElement tableKeySchemaElem,
                                                        ProvisionedThroughput tableProvisionedThroughput, GlobalSecondaryIndex gsi) {
        // Define a request to create a table
        CreateTableRequest request = new CreateTableRequest().withTableName(tableName).withKeySchema(tableKeySchemaElem)
                .withAttributeDefinitions(attributeDefinitions).withProvisionedThroughput(tableProvisionedThroughput)
                .withGlobalSecondaryIndexes(gsi);

        System.out.println("Creating the Reservations table.");

        // Create the table using the CreateTableRequest
        Table table = dynamoDB.createTable(request);

        // Wait for the table to become active
        try {
            table.waitForActive();
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
        System.out.println("The Reservations table has been created.");
    }
}
