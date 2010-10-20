/*#################################################################
(c)2008/2009 Greenriver Consulting SL. All rights reserved.

This project and all subsequent files are licensed under the terms
and conditions of the GreenRiverLicense.

If you haven't received a copy of the license you can obtain it at
http://www.greenriverconsulting.es/licensing/greenriverlicense

Author: mangelp
###################################################################*/
package com.greenriver.commons;

import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

/**
 * Container for statistics datum about adapter operations. The data is stored
 * in a map and several helpers are used to help to cast the data to some
 * common types.
 */
public class StatisticsStorage {

    //Reference name for the source of the data
    private String name;
    //Maximum length of the keys. Used when creating an string representation
    private int maxKeyLength;
    //Data
    private Map<String, Object> statistics;
    //Start date of the period of the data collection
    private Date start;
    //End date of the period of the data collection
    private Date end;

    public String getName() {
	return name;
    }

    public Map<String, Object> getStatistics() {
	return statistics;
    }

    protected int getMaxKeyLength() {
	return maxKeyLength;
    }

    public Date getStart() {
	return start;
    }

    public void setStart(Date start) {
	this.start = start;
    }

    public Date getEnd() {
	return end;
    }

    public void setEnd(Date end) {
	this.end = end;
    }

    public StatisticsStorage(String name) {
	this.name = name;
	statistics = new Hashtable<String, Object>();
    }

    /**
     * Gets an object
     * @param key
     * @return
     */
    public Object get(String key) {
	Object result = statistics.get(key);
	if (result == null) {
	    throw new IllegalArgumentException("Invalid key");
	}

	return result;
    }

    /**
     * Sets an object value
     * @param key
     * @param value
     */
    public void set(String key, Object value) {
	statistics.put(key, value);
    }

    public String getString(String key) {
	Object result = statistics.get(key);
	if (result == null) {
	    throw new IllegalArgumentException("Invalid key");
	}

	return result.toString();
    }

    public void setString(String key, String value) {
	if (key.length() > maxKeyLength) {
	    maxKeyLength = key.length();
	}
	statistics.put(key, value);
    }

    public void appendString(String key, String value) {
	value = getString(key).concat(value);
	statistics.put(key, value);
    }

    public int getInt(String key) {
	Integer result = (Integer) statistics.get(key);
	if (result == null) {
	    throw new IllegalArgumentException("Invalid key");
	}

	return result;
    }

    public void setInt(String key, int value) {
	if (key.length() > maxKeyLength) {
	    maxKeyLength = key.length();
	}
	statistics.put(key, value);
    }

    public void sumInt(String key, int value) {
	value += getInt(key);
	setInt(key, value);
    }

    public void subInt(String key, int value) {
	value = getInt(key) - value;
	setInt(key, value);
    }

    public long getLong(String key) {
	Long result = (Long) statistics.get(key);
	if (result == null) {
	    throw new IllegalArgumentException("Invalid key");
	}

	return result;
    }

    public void setLong(String key, long value) {
	if (key.length() > maxKeyLength) {
	    maxKeyLength = key.length();
	}
	statistics.put(key, value);
    }

    public void sumLong(String key, long value) {
	value += getLong(key);
	setLong(key, value);
    }

    public void subLong(String key, long value) {
	value = getLong(key) - value;
	setLong(key, value);
    }

    public float getFloat(String key) {
	Float result = (Float) statistics.get(key);
	if (result == null) {
	    throw new IllegalArgumentException("Invalid key");
	}

	return result;
    }

    public void setFloat(String key, float value) {
	if (key.length() > maxKeyLength) {
	    maxKeyLength = key.length();
	}
	statistics.put(key, value);
    }

    public void sumFloat(String key, float value) {
	value += getFloat(key);
	setFloat(key, value);
    }

    public void subFloat(String key, float value) {
	value = getFloat(key) - value;
	setFloat(key, value);
    }

    public StringBuilder toStringBuilder() {
	StringBuilder sb = new StringBuilder();
	appendTo(sb);
	return sb;
    }

    public void appendTo(StringBuilder sb) {
	String format = " * %1$-" + maxKeyLength + "s = %2$s\n";

	if (start != null && end != null) {
	    sb.append("Statistics collected from " + start + " to " + end);
	} else if (start != null) {
	    sb.append("Statistics collected since " + start);
	} else if (end != null) {
	    sb.append("Statistics collected until " + end);
	}

	sb.append("\n");
	
	for (String key : statistics.keySet()) {
	    sb.append(String.format(format, key, statistics.get(key)));
	}
    }
}
