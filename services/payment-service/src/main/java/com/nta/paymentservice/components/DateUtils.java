package com.nta.paymentservice.components;

import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Component
public class DateUtils {
    private final List<String> DATE_FORMATS_WITHOUT_TIMEZONE =
            List.of(
                    "yyyy-MM-dd HH:mm:ss",
                    "yyyy-MM-dd HH:mm:ss.0",
                    "MMM d, yyyy, h:mm:ss a",
                    "dd MMM yyyy",
                    "MMM dd, yyyy hh:mm:ss a",
                    "dd-MMM-yyyy HH:mm:ss",
                    "ss",
                    "yyyy/MM/dd HH:mm:ss");

    private DateUtils() {
        super();
    }

    public Date parseDate(final String dateString) throws ParseException {
        final String trimmedDateString = dateString.trim();
        Date date = parseDate("yyyy-MM-dd'T'HH:mm:sss'Z'", trimmedDateString);
        if (date == null) {
            for (final String format : DATE_FORMATS_WITHOUT_TIMEZONE) {
                date = parseDate(format, trimmedDateString);
                if (date != null) {
                    break;
                }
            }
        }
        if (date == null) {
            throw new ParseException("Failed to parse date string: " + dateString, 0);
        }
        return date;
    }

    public LocalDateTime parseDateTime(final String dateString) throws ParseException {
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        return LocalDateTime.parse(dateString, formatter);
    }

    private Date parseDate(final String format, final String dateString) {
        try {
            final DateFormat dateFormat = new SimpleDateFormat(format, Locale.getDefault());
            return dateFormat.parse(dateString);
        } catch (ParseException e) {
            return null;
        }
    }

    public Date parseUsingDefaultFormat(final String date) throws ParseException {
        final DateFormat df = getDefaultDateFormat();
        return df.parse(date);
    }

    public Date getFormattedDate(final String date) throws ParseException {
        final SimpleDateFormat format;
        if (date.contains("PM") || date.contains("AM")) {
            format = new SimpleDateFormat("MMM d, yyyy, h:mm:ss a", Locale.getDefault());
        } else if (date.contains("/")) {
            format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault());
        } else if (date.contains(":") && date.contains("-") && date.contains("T")) {
            format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX", Locale.getDefault());
        } else if (date.contains(":") && date.contains("-")) {
            format = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss", Locale.getDefault());
        } else {
            format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        }
        return format.parse(date.trim());
    }

    public SimpleDateFormat getDefaultDateFormat() {
        return new SimpleDateFormat("MMM d, yyyy, h:mm:ss a", Locale.getDefault());
    }

    public SimpleDateFormat getDefaultDateFormat(final String dateString) {
        return dateString.toUpperCase(Locale.ENGLISH).contains("AM")
                        || dateString.toUpperCase(Locale.ENGLISH).contains("PM")
                ? new SimpleDateFormat("MMM d, yyyy, h:mm:ss a", Locale.getDefault())
                : new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss", Locale.getDefault());
    }
}
