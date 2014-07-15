/**
 * Copyright 2014 Telefonica Investigación y Desarrollo, S.A.U <br>
 * This file is part of FI-WARE project.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License.
 * </p>
 * <p>
 * You may obtain a copy of the License at:<br>
 * <br>
 * http://www.apache.org/licenses/LICENSE-2.0
 * </p>
 * <p>
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * </p>
 * <p>
 * See the License for the specific language governing permissions and limitations under the License.
 * </p>
 * <p>
 * For those usages not covered by the Apache version 2.0 License please contact with opensource@tid.es
 * </p>
 */

package com.telefonica.euro_iaas.paasmanager.rest.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Util class to compare in a unyfied way the dates.
 *
 * @author fernandolopezaguilar
 */
public class CompareDates {

    private Long limit;
    private static Logger log = LoggerFactory.getLogger(CompareDates.class);
    private long offset;

    /**
     * Constructor of the class.
     * @param limit Limit to compare two dates.
     */
    public CompareDates(Long limit) {
        this.limit = limit;
    }

    /**
     * Default constructor.
     */
    public CompareDates() {
        this.limit = 0L;
    }

    /**
     * Check that the different between the two dates is less than the limit fixed.
     * @param dateString    The first date.
     * @param now   The second date.
     * @return  True is the different is less than the limit
     *          False otherwise.
     */
    public boolean checkDate(String dateString, Date now) {
        boolean result;

        Date date = this.getDate(dateString, getType(dateString)); // 2

        long diff = getTimeDiff(date, now) - offset;

        log.info("Date1: " + dateString + "\tDate2: " + now.toString());
        log.info("Diff: " + diff);

        if (diff <= limit) {
            result = true;
        } else {
            result = false;
        }

        return result;
    }

    public void setLimit(Long limit) {
        this.limit = limit;
    }

    /**
     * Get the date different between two values.
     * @param dateOne   Date one.
     * @param dateTwo   Date two.
     * @return  The difference.
     */
    public long getTimeDiff(Date dateOne, Date dateTwo) {
        return Math.abs(dateOne.getTime() - dateTwo.getTime());
    }

    /**
     * Get the time difference between a Date and a string (not implemented).
     * @param deteOne   A date.
     * @param dateTow   A string which represents a date.
     * @return  The difference between them.
     */
    public long getTimeDiff(Date deteOne, String dateTow) {
        return 1;
    }

    /**
     * Get the difference of a string representing a date and a date.
     * @param dateOne   The string representing a date.
     * @param dateTwo   The date two.
     * @return  The difference between them.
     */
    public long getTimeDiff(String dateOne, Date dateTwo) {
        Date date1 = this.getDate(dateOne, getType(dateOne)); // 1);

        long timeDiff = date1.getTime() - dateTwo.getTime();

        return timeDiff;
    }

    /**
     * Return the corresponding date in the specified format.
     * @param dateString    The string representing the date.
     * @param typeFormat    The format type requested.
     * @return  The date in Date class with the format specified.
     */
    public Date getDate(String dateString, int typeFormat) {
        Date date = null;

        switch (typeFormat) {
            case 0:
                // Format: "2012-11-29T18:00:45Z";
                try {
                    XMLGregorianCalendar cal = DatatypeFactory.newInstance().newXMLGregorianCalendar(dateString);

                    cal.setTimezone(DatatypeConstants.FIELD_UNDEFINED);
                    Calendar c2 = cal.toGregorianCalendar();

                    date = c2.getTime();
                } catch (DatatypeConfigurationException ex) {
                    log.error("Cannot parse correctly the date: " + date);
                }

                break;

            case 1:
                // Format: Tue, 16 Aug 2011 19:50:26 GMT
                try {
                    date = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", new Locale("en_EN")).parse(dateString);

                } catch (ParseException ex) {
                    log.error("Cannot parse correctly the date: " + date);
                }

                break;

            case 2:
                // Format: Tue Dec 04 18:10:32 CET 2012
                try {
                    date = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", new Locale("en_EN")).parse(dateString);

                } catch (ParseException ex) {
                    log.error("Cannot parse correctly the date: " + date);
                }

                break;

            default:
                log.error("Unknow date format or date format not controlled: " + date);

        }

        return date;
    }

    /**
     * Validate that the difference between the two dates is less than 24h.
     * @param dateString1   The first date.
     * @param dateString2   The second date.
     * @return  The difference between two dates.
     */
    public String validateDates(String dateString1, String dateString2) {
        Date date1 = this.getDate(dateString1, getType(dateString1)); // 0);
        Date date2 = this.getDate(dateString2, getType(dateString2)); // 1);

        Long date = (long) 86400000;
        Long dateLong1 = date1.getTime();
        Long dateLong2 = date2.getTime();

        long timeDiff = dateLong1 - dateLong2;

        if (timeDiff != date) {

            dateLong1 += (date - timeDiff);
        }

        Date newdate = new Date(dateLong1);

        return newdate.toString();
    }

    public void setOffset(long offset) {
        this.offset = offset;
    }

    /**
     * Analyze the date in format string and return the type of data representation.
     * @param data  The date in string format.
     * @return  The type of data representation.
     */
    protected int getType(String data) {
        // Regular Expression [0-9]*\-[0-9]*\-[0-9]*T[0-9]*:[0-9]*:[0-9]*Z$
        // as a Java string "[0-9]*\\-[0-9]*\\-[0-9]*T[0-9]*:[0-9]*:[0-9]*Z$"
        // example: 2012-12-28T18:00:45Z
        String pattern = "[0-9]*\\-[0-9]*\\-[0-9]*T[0-9]*:[0-9]*:[0-9]*Z$";

        boolean matching = data.matches(pattern);

        int result = -1;

        if (matching) {
            result = 0;
        } else {
            // Regular Expression
            // [a-zA-Z]*\,\s*[0-9]*\s*[a-zA-Z]*\s*[0-9]*\s*[0-9]*:[0-9]*:[0-9]*\s*GMT$
            // as a Java string
            // "[a-zA-Z]*\\,\\s*[0-9]*\\s*[a-zA-Z]*\\s*[0-9]*\\s*[0-9]*:[0-9]*:[0-9]*\\s*GMT$"
            // example: Tue, 16 Aug 2011 19:50:26 GMT
            pattern = "[a-zA-Z]*\\,\\s*[0-9]*\\s*[a-zA-Z]*\\s*[0-9]*\\s*[0-9]*:[0-9]*:[0-9]*\\s*GMT$";

            matching = data.matches(pattern);

            if (matching) {
                result = 1;
            } else {
                // Regular Expression
                // [a-zA-Z]*\s*[a-zA-Z]*\s*[0-9]*\s*[0-9]*:[0-9]*:[0-9]*\s*CET\s*[0-9]*$
                // as a Java string
                // "[a-zA-Z]*\\s*[a-zA-Z]*\\s*[0-9]*\\s*[0-9]*:[0-9]*:[0-9]*\\s*CET\\s*[0-9]*$"
                // example: Tue Dec 04 18:10:32 CET 2012
                pattern = "[a-zA-Z]*\\s*[a-zA-Z]*\\s*[0-9]*\\s*[0-9]*:[0-9]*:[0-9]*\\s*CET\\s*[0-9]*$";

                matching = data.matches(pattern);

                if (matching) {
                    result = 2;
                } else {
                    // Regular Expression
                    // [a-zA-Z]*\s*[a-zA-Z]*\s*[0-9]*\s*[0-9]*:[0-9]*:[0-9]*\s*CEST\s*[0-9]*$
                    // as a Java string
                    // "[a-zA-Z]*\\s*[a-zA-Z]*\\s*[0-9]*\\s*[0-9]*:[0-9]*:[0-9]*\\s*CEST\\s*[0-9]*$"
                    // example: Tue Dec 04 18:10:32 CEST 2012
                    pattern = "[a-zA-Z]*\\s*[a-zA-Z]*\\s*[0-9]*\\s*[0-9]*:[0-9]*:[0-9]*\\s*CEST\\s*[0-9]*$";

                    matching = data.matches(pattern);

                    if (matching) {
                        result = 2;
                    }
                }
            }
        }
        return result;
    }

}
