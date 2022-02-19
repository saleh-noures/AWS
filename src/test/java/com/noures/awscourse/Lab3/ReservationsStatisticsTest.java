package com.noures.awscourse.Lab3;


import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ReservationsStatisticsTest {

    @Test
    public void test() throws Exception {
        ReservationsStatistics.main(new String[] { "Reno" });
        assertEquals(178, ReservationsStatistics.itemCount, 0);
    }
}
