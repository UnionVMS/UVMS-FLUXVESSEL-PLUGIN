package eu.europa.ec.fisheries.uvms.plugins.flux.vessel.model.mapper;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class XsdDateTimeConverterTest {

    @Test
    public void testUnmarshal() {
        DateTime expected = new DateTime(2001, 10, 26, 19, 32, 52, DateTimeZone.UTC);
        assertEquals(expected, XsdDateTimeConverter.unmarshal("2001-10-26T21:32:52+02:00"));
    }

    @Test
    public void testMarshalDate() {
        DateTime dateTime = new DateTime(2017,1,1,12,13, DateTimeZone.UTC);
        assertEquals("2017-01-01Z", XsdDateTimeConverter.marshalDate(dateTime));
    }

    @Test
    public void testMarshalDateTime() {
        DateTime dateTime = new DateTime(2017,1,1,12,13, DateTimeZone.UTC);
        assertEquals("2017-01-01T12:13:00Z", XsdDateTimeConverter.marshalDateTime(dateTime));
    }

}