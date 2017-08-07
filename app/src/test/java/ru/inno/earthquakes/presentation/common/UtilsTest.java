package ru.inno.earthquakes.presentation.common;

import junit.framework.Assert;

import org.junit.Test;

/**
 * @author Artur Badretdinov (Gaket)
 *         06.08.17
 */
public class UtilsTest {

    @Test
    public void formatDistanceString() throws Exception {
        double d = 132_212.2112;
        Assert.assertEquals("132 212", Utils.formatDistanceString(d));
    }
}