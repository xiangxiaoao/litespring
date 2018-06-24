package org.litespring.test.v2;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.litespring.beans.SimpleTypeConverter;
import org.litespring.beans.TypeConverter;
import org.litespring.beans.TypeMismatchException;


public class TypeConverterTest {

    TypeConverter converter = null;

    @Before
    public void setUp() {
        converter = new SimpleTypeConverter();
    }

    @Test
    public void testConvertStringToInt() throws TypeMismatchException {
        Integer i = converter.convertIfNecessary("3",Integer.class);
        Assert.assertEquals(3, i.intValue());

        try {
            converter.convertIfNecessary("3.1", Integer.class);
        } catch (TypeMismatchException e) {
            return;
        }
        Assert.fail();
    }

    @Test
    public void testConvertStringToBoolean() throws TypeMismatchException {
        Boolean b = converter.convertIfNecessary("true", Boolean.class);
        Assert.assertEquals(true, b.booleanValue());

        try {
            converter.convertIfNecessary("abc", Boolean.class);
        } catch (TypeMismatchException e) {
            return;
        }
        Assert.fail();
    }
}
