/*

  (c) Copyright 2011 Telefonica, I+D. Printed in Spain (Europe). All Rights
  Reserved.

  The copyright to the software program(s) is property of Telefonica I+D.
  The program(s) may be used and or copied only with the express written
  consent of Telefonica I+D or in accordance with the terms and conditions
  stipulated in the agreement/contract under which the program(s) have
  been supplied.

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

/**
 * 
 * @author fernandolopezaguilar
 */
public class CompareDates {

	private Long limit;
	private static org.apache.log4j.Logger log = org.apache.log4j.Logger
			.getLogger(CompareDates.class);
	private long offset;

	public CompareDates(Long limit) {
		this.limit = limit;
	}

	public CompareDates() {
		this.limit = 0L;
	}

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

	public long getTimeDiff(Date dateOne, Date dateTwo) {
		return Math.abs(dateOne.getTime() - dateTwo.getTime());
	}

	public long getTimeDiff(Date deteOne, String dateTow) {
		return 1;
	}

	public long getTimeDiff(String dateOne, Date dateTwo) {
		Date date1 = this.getDate(dateOne, getType(dateOne)); // 1);

		long timeDiff = date1.getTime() - dateTwo.getTime();

		return timeDiff;
	}

	public Date getDate(String dateString, int typeFormat) {
		Date date = null;

		switch (typeFormat) {
		case 0:
			// Format: "2012-11-29T18:00:45Z";
			try {
				XMLGregorianCalendar cal = DatatypeFactory.newInstance()
						.newXMLGregorianCalendar(dateString);

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
				date = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z",
						new Locale("en_EN")).parse(dateString);

			} catch (ParseException ex) {
				log.error("Cannot parse correctly the date: " + date);
			}

			break;

		case 2:
			// Format: Tue Dec 04 18:10:32 CET 2012
			try {
				date = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy",
						new Locale("en_EN")).parse(dateString);

			} catch (ParseException ex) {
				log.error("Cannot parse correctly the date: " + date);
			}

			break;
		}

		return date;
	}

	public String validateDates(String dateString1, String dateString2) {
		Date date1 = this.getDate(dateString1, getType(dateString1)); // 0);
		Date date2 = this.getDate(dateString2, getType(dateString2)); // 1);

		Long date = (long) 86400000;
		Long dateLong1 = date1.getTime();
		Long dateLong2 = date2.getTime();

		long timeDiff = dateLong1 - dateLong2;

		if (timeDiff != date) {
			log.error("Date format incorrect between token.expires "
					+ "and Header field in the HTTP message");

			dateLong1 += (date - timeDiff);
		}

		Date newdate = new Date(dateLong1);

		return newdate.toString();
	}

	public void setOffset(long offset) {
		this.offset = offset;
	}

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
