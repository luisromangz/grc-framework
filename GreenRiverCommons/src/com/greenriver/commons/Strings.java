/*#################################################################
(c)2008/2009 Greenriver Consulting SL. All rights reserved.

This project and all subsequent files are licensed under the terms
and conditions of the GreenRiverLicense.

If you haven't received a copy of the license you can obtain it at
http://www.greenriverconsulting.es/licensing/greenriverlicense

Author: mangelp
###################################################################*/
package com.greenriver.commons;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Random;

/**
 * String utilities
 */
public class Strings {

    private static final char[] hexChars = {
        '0', '1', '2', '3',
        '4', '5', '6', '7',
        '8', '9', 'A', 'B',
        'C', 'D', 'E', 'F'
    };
    private static String LOWERCASE_CHARS = "abcdefghijklmnopqrstuvwxyz";
    private static String UPERCASE_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static String NUMBERS = "0123456789";
    private static Random random = new Random();
    private static Charset asciiCharset = Charset.forName("US-ASCII");

    /**
     * Formats an integer as hex
     * @param value
     * @return
     */
    public static String toHexString(int value) {
        byte[] data = new byte[2];

        data[0] = (byte) ((value & 0xF0) >>> 4);
        data[1] = (byte) ((value & 0x0F));

        return hexChars[data[0]] + "" + hexChars[data[1]];
    }

    /**
     * Formats a string as the hex string resulting of converting the input
     * string to byte array and then formatting that as hex.
     * @param str string to format
     * @return a string with the array formatted as hex
     */
    public static String toHexString(String str) {
        byte[] data = str.getBytes();
        return toHexString(data);
    }

    /**
     * Formats an byte array as hex into a string.
     * @param data array to format
     * @return a string with the array formatted as hex
     */
    public static String toHexString(byte[] data) {
        StringBuilder builder = new StringBuilder(data.length * 2);
        int highB, lowB;

        for (int i = 0; i < data.length; i++) {
            highB = (data[i] & 0xF0) >>> 4;
            lowB = (data[i] & 0x0F);
            builder.append(hexChars[highB]);
            builder.append(hexChars[lowB]);
        }

        return builder.toString();
    }

    /**
     * Formats an byte array as hex into a string.
     * @param data array to format
     * @return a string with the array formatted as hex
     */
    public static String toHexString(List<Byte> data) {
        StringBuilder builder = new StringBuilder(data.size() * 2);

        for (byte digit : data) {
            builder.append(hexChars[(digit & 0xF0) >>> 4]);
            builder.append(hexChars[digit & 0x0F]);
        }

        return builder.toString();
    }

    /**
     * Calculates the checksum of an string and then returns it as a hex
     * string.
     * @param str string to get the checksum from
     * @return the checksum as a hex string or null if MD5 is not supported
     * @throws NullPointerException if the input string is null
     */
    public static String toMD5HexString(String str) {
        if (str == null) {
            throw new NullPointerException("Null strings are not allowed");
        }

        MessageDigest digest = null;

        try {
            digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException ex) {
            return null;
        }

        byte[] data = digest.digest(str.getBytes());
        return toHexString(data);
    }

    /**
     * Gets if a string is null or empty. Usefull for those lazy developers that
     * apreciates saving some keystrokes.
     * @param str String to check
     * @return if the string is null reference or if it is empty string.
     */
    public static boolean isNullOrEmpty(String str) {
        return str == null || str.isEmpty();
    }

    /**
     * Compares two strings and returns true if both are null references or
     * they are the same string. In any other case returns false.
     * @param oneStr
     * @param otherStr
     * @return true if they are the same or false if not
     */
    public static boolean equals(String oneStr, String otherStr) {
        if (oneStr == null && otherStr == null) {
            return true;
        } else if (oneStr != null && otherStr == null) {
            return false;
        } else if (otherStr != null && oneStr == null) {
            return false;
        } else {
            return oneStr.equals(otherStr);
        }
    }

    /**
     * Compares two strings and returns true if both are null references or
     * they are the same case insensitive string. In any other case returns
     * false.
     * @param oneStr
     * @param otherStr
     * @return true if they are the same or false if not
     */
    public static boolean equalsIgnoreCase(String oneStr, String otherStr) {
        if (oneStr == null && otherStr == null) {
            return true;
        } else if (oneStr != null && otherStr == null) {
            return false;
        } else if (otherStr != null && oneStr == null) {
            return false;
        } else {
            return oneStr.equalsIgnoreCase(otherStr);
        }
    }

    /**
     * Converts a byte array that represents an string in ascii charset to an
     * utf-8 java string.
     * @param array
     * @param offset
     * @param length
     * @return
     */
    public static String fromAscii(byte[] array, int offset, int length) {
        CharBuffer buf =
                asciiCharset.decode(ByteBuffer.wrap(array, offset, length));
        return buf.toString();
    }

    /**
     * Converts an utf-8 string into a byte array that represents the string
     * in ascii charset.
     * @param str
     * @return
     */
    public static byte[] toAscii(String str) {
        ByteBuffer bb = asciiCharset.encode(str);
        return bb.array();
    }

    public static boolean contains(String[] haystack, String needle) {
        return Arrays.asList(haystack).contains(needle);
    }

    public static boolean startsWith(String prefix, String target) {
        return startsWith(prefix, target, false);
    }

    public static boolean startsWith(String prefix, String target, boolean caseInsensitive) {
        if (prefix == null
                || (prefix.length() == 0 && target != null)) {
            return true;
        } else if (target == null) {
            return false;
        } else if (prefix.length() > target.length()) {
            return false;
        } else if (prefix.length() == target.length()) {
            target = target.substring(0, prefix.length());
            return (caseInsensitive && Strings.equalsIgnoreCase(prefix, target))
                    || (!caseInsensitive && Strings.equals(prefix, target));
        }

        target = target.substring(0, prefix.length());

        if (!caseInsensitive) {
            return Strings.equals(prefix, target);
        } else {
            return Strings.equalsIgnoreCase(prefix, target);
        }
    }

    public static boolean endsWith(String suffix, String target) {
        return endsWith(suffix, target, false);
    }

    public static boolean endsWith(String suffix, String target, boolean caseInsensitive) {
        if (suffix == null
                || (suffix.length() == 0 && target != null)) {
            return true;
        } else if (target == null) {
            return false;
        } else if (suffix.length() > target.length()) {
            return false;
        } else if (suffix.length() == target.length()) {
            target = target.substring(0, suffix.length());
            return (caseInsensitive && Strings.equalsIgnoreCase(suffix, target))
                    || (!caseInsensitive && Strings.equals(suffix, target));
        }

        target = target.substring(target.length() - suffix.length());

        if (!caseInsensitive) {
            return Strings.equals(suffix, target);
        } else {
            return Strings.equalsIgnoreCase(suffix, target);
        }
    }

    public static String random(int size) {
        String src = LOWERCASE_CHARS + UPERCASE_CHARS + NUMBERS;
        StringBuilder result = new StringBuilder(size);
        int srcLen = src.length();
        int randomInt = 0;
        char ch;

        while (size > 0) {
            randomInt = random.nextInt(srcLen);
            ch = src.charAt(randomInt);
            result.append(ch);
            size--;
        }

        return result.toString();
    }

    /**
     * Returns a lowercased version of a given string, in which the only
     * lowercased chars are the ones between startIndex (included) and endIndex
     * (not included).
     * @param value 
     * @param startIndex
     * @param endIndex
     * @return 
     */
    public static String toLowerCase(String value, int startIndex, int endIndex) {
        String lowercased = value.substring(startIndex, endIndex);

        lowercased = lowercased.toLowerCase();

        String result = "";

        if (startIndex > 0) {
            result = value.substring(0, startIndex);
        }

        result += lowercased;

        if (endIndex < value.length()) {
            result += value.substring(endIndex);
        }

        return result;
    }

    public static String toUpperCase(String value, int startIndex, int endIndex) {
        String uppercased = value.substring(startIndex, endIndex);

        uppercased = uppercased.toUpperCase();

        String result = "";

        if (startIndex > 0) {
            result = value.substring(0, startIndex);
        }

        result += uppercased;

        if (endIndex < value.length()) {
            result += value.substring(endIndex);
        }

        return result;
    }

    public static String join(Collection collection, String glue) {
        if (collection == null || collection.isEmpty()) {
            return "";
        }

        StringBuilder builder = new StringBuilder();
        Iterator iter = collection.iterator();
        while (iter.hasNext()) {
            builder.append(iter.next());
            if (!iter.hasNext()) {
                break;
            }
            builder.append(glue);
        }
        return builder.toString();
    }

    /**
     * Creates a String formed by repeating a given string a number of times.
     * @param string The string to be repeated.
     * @param count The number of times the string must be repeated.
     * @return
     */
    public final static String repeat(String string, int count) {
        String result = "";
        // Yeah, it's probably the most innefficient way to do this. So what?
        for(int i=0;i<count;i++){
            result+=string;
        }

        return result;
    }


    /**
     * Joins a series of strings passed as an array.
     * @param elements
     * @param glue
     */
    public static String join(String[] elements, String glue) {
        return Strings.join(Arrays.asList(elements), glue);
    }
}
