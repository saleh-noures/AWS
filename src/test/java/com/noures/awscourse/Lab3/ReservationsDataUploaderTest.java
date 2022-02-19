package com.noures.awscourse.Lab3;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ReservationsDataUploaderTest {

    @Test
    public void test() throws Exception {
        ReservationsDataUploader.main(new String[0]);
        assertEquals(1000, ReservationsDataUploader.numItemsAdded, 0);
    }
}
