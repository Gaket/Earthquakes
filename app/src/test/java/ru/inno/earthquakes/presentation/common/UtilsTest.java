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
        double d = 5.65;
        Assert.assertEquals("5,65", Utils.formatDistanceString(d));
    }

}