package eu.europa.ec.fisheries.uvms.plugins.flux.vessel.model.mapper;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import javax.xml.bind.DatatypeConverter;

public class XsdDateTimeConverter {
    
    public static DateTime unmarshal(String dateTime) {
        return new DateTime(DatatypeConverter.parseDate(dateTime).getTime(), DateTimeZone.UTC);
    }

    public static String marshalDate(DateTime dateTime) {
        return DatatypeConverter.printDate(dateTime.withZone(DateTimeZone.UTC).toGregorianCalendar());
    }

    public static String marshalDateTime(DateTime dateTime) {
        return DatatypeConverter.printDateTime(dateTime.withZone(DateTimeZone.UTC).toGregorianCalendar());
    }
}