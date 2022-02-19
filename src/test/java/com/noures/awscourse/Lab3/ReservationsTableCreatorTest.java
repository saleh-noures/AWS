package com.noures.awscourse.Lab3;


import com.amazonaws.services.dynamodbv2.model.ResourceNotFoundException;
import org.junit.Test;

import static org.junit.Assert.fail;

public class ReservationsTableCreatorTest {

    @Test
    public void test() throws Exception {
        ReservationsTableCreator.main(new String[0]);
        try {
            ReservationsTableCreator.dynamoDBClient.describeTable(ReservationsTableCreator.RESERVATIONS_TABLE_NAME);
        } catch (ResourceNotFoundException e) {
            String msg = ReservationsTableCreator.RESERVATIONS_TABLE_NAME
                    + " %s table does not exist. Do not need to remove it.";
            fail(msg);
        }
    }
}
