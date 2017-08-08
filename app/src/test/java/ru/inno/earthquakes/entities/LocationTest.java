package ru.inno.earthquakes.entities;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * @author Artur Badretdinov (Gaket)
 *         07.08.17
 */
@RunWith(JUnit4.class)
public class LocationTest {

    @Test
    public void distanceIsCorrect() throws Exception {
        Location.Coordinates Moscow = new Location.Coordinates(55.75222, 37.61556);
        Location.Coordinates LosAngeles = new Location.Coordinates(34.0522342, -118.2436849);
        double expectedDistance = 9772.50; // taken from https://www.distance.to/Moscow/Los-Angeles

        double distance = Location.distance(Moscow, LosAngeles);

        Assert.assertEquals(expectedDistance, distance, 5);
    }
}