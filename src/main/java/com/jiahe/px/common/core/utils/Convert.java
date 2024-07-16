package com.jiahe.px.common.core.utils;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.util.StringUtils;

import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Clob;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.CharacterIterator;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.text.StringCharacterIterator;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 閫氱敤鏁版嵁绫诲瀷杞崲鍑芥暟
 *
 * @author ZJL
 */
public class Convert {
    /**
     * Hexadecimal characters corresponding to each half byte value.
     */
    private static final char[] HexChars = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E',
            'F'};

    /**
     * The BASE64 encoding standard's 6-bit alphabet, from RFC 1521, plus the
     * padding character at the end.
     */
    private static final char[] Base64Chars = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N',
            'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i',
            'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3',
            '4', '5', '6', '7', '8', '9', '+', '/', '='};

    /**
     * Converts a long integer to an unsigned hexadecimal String. Treats the
     * integer as an unsigned 64 bit value and left-pads with the pad character
     * of the caller's choice.
     *
     * @param value The long integer to convert to a hexadecimal string.
     * @param len   The total padded length of the string. If the number is larger
     *              than the padded length, then this length of the string will be
     *              the length of the number.
     * @param pad   The character to use for padding.
     * @return Unsigned hexadecimal numeric string representing the specified
     * value.
     */
    public static final String LongToHex(long value, int len, char pad) {
        StringBuffer sb = new StringBuffer(Long.toHexString(value));
        int npad = len - sb.length();
        while (npad-- > 0)
            sb.insert(0, pad);
        return new String(sb);
    }

    /**
     * Converts an arbitrary array of bytes to ASCII hexadecimal string form,
     * with two hex characters corresponding to each byte. The length of the
     * resultant string in characters will be twice the length of the specified
     * array of bytes.
     *
     * @param bytes The array of bytes to convert to ASCII hex form.
     * @return An ASCII hexadecimal numeric string representing the specified
     * array of bytes.
     */
    public static final String BytesToHex(byte[] bytes) {
        if (bytes == null)
            return "";
        if (bytes.length == 0)
            return "";
        StringBuffer sb = new StringBuffer();
        int i;
        for (i = 0; i < bytes.length; i++) {
            sb.append(HexChars[(bytes[i] >> 4) & 0xf]);
            sb.append(HexChars[bytes[i] & 0xf]);
        }
        return new String(sb);
    }

    /**
     * 瀛楃涓叉浛鎹�(涓嶅尯鍒嗗ぇ灏忓啓)
     *
     * @param sourcestr 鍘熷瓧绗︿覆
     * @param oldstr    鏃х殑瀛楃涓�
     * @param newstr    鏇挎崲鐨勫瓧绗︿覆
     * @return
     */
    public static String Replace(String sourcestr, String oldstr, String newstr) {
        return StringHelper.Replace(sourcestr, oldstr, newstr);
    }

    /**
     * 瀛楃涓叉浛鎹�(涓嶅尯鍒嗗ぇ灏忓啓)
     *
     * @param sourcestr 鍘熷瓧绗︿覆
     * @param oldstr    鏃х殑瀛楃涓�
     * @param newstr    鏇挎崲鐨勫瓧绗︿覆
     * @return
     */
    public static String replace(String sourcestr, String oldstr, String newstr) {
        return StringHelper.Replace(sourcestr, oldstr, newstr);
    }

    /**
     * 鏇挎崲瀛楃涓�
     *
     * @param strExpression
     * @param strSearch
     * @param strReplace
     * @param intMode       0=鍖哄垎澶у皬鍐�,1=涓嶅尯鍒嗗ぇ灏忓啓
     * @return
     */
    public static String Replace(String strExpression, String strSearch, String strReplace, int intMode) {
        return StringHelper.Replace(strExpression, strSearch, strReplace, intMode);
    }

    /**
     * 鏇挎崲 Json鐨勭壒娈婂瓧绗�
     *
     * @param str 瑕佸鐞嗙殑瀛楃
     * @return
     */
    public static String formatJson(String str) {
        String s = str;
        s = Replace(s, "\"", "\\\"");
        s = Replace(s, "\\", "\\\\");
        s = Replace(s, "/", "\\/");

        return s;
    }

    /**
     * Hex String to Byte Array
     *
     * @param s
     * @return
     */
    public static final byte[] HexToBytes(String s) {
        if (s.equals(""))
            return null;
        byte[] arr = new byte[s.length() / 2];
        for (int start = 0; start < s.length(); start += 2) {
            String thisByte = s.substring(start, start + 2);
            arr[start / 2] = Byte.parseByte(thisByte, 16);
        }
        return arr;
    }

    /**
     * Performs RFC1521 style Base64 encoding of arbitrary binary data. The
     * output is a java String containing the Base64 characters representing the
     * binary data. Be aware that this string is in Unicode form, and should be
     * converted to UTF8 with the usual java conversion routines before it is
     * sent over a network. The output string is guaranteed to only contain
     * characters that are a single byte in UTF8 format. Also be aware that this
     * routine leaves it to the caller to break the string into 70 byte lines as
     * per RFC1521.
     *
     * @param bytes The array of bytes to convert to Base64 encoding.
     * @return An string containing the specified bytes in Base64 encoded form.
     */
    public static final String BytesToBase64String(byte[] bytes) {
        return BytesToBase64String(bytes, Base64Chars);
    }

    /**
     * Performs encoding of arbitrary binary data based on a 6 bit alphabet. The
     * output is a java String containing the encoded characters representing
     * the binary data. Be aware that this string is in Unicode form, and should
     * be converted to UTF8 with the usual java conversion routines before it is
     * sent over a network. The alphabet passed in via <code>chars</code> is
     * used without further checks, it's the callers responsibility to set it to
     * something meaningful.
     *
     * @param bytes The array of bytes to convert to Base64 encoding.
     * @param chars The alphabet used in encoding. Must contain exactly 65
     *              characters: A 6 bit alphabet plus one padding char at position
     *              65.
     * @return An string containing the specified bytes in Base64 encoded form.
     */
    public static final String BytesToBase64String(byte[] bytes, char[] chars) {
        if (bytes == null)
            return "";
        StringBuffer sb = new StringBuffer();
        int len = bytes.length, i = 0, ival;
        while (len >= 3) {
            ival = ((int) bytes[i++] + 256) & 0xff;
            ival <<= 8;
            ival += ((int) bytes[i++] + 256) & 0xff;
            ival <<= 8;
            ival += ((int) bytes[i++] + 256) & 0xff;
            len -= 3;
            sb.append(chars[(ival >> 18) & 63]);
            sb.append(chars[(ival >> 12) & 63]);
            sb.append(chars[(ival >> 6) & 63]);
            sb.append(chars[ival & 63]);
        }
        switch (len) {
            case 0: // No pads needed.
                break;
            case 1: // Two more output bytes and two pads.
                ival = ((int) bytes[i++] + 256) & 0xff;
                ival <<= 16;
                sb.append(chars[(ival >> 18) & 63]);
                sb.append(chars[(ival >> 12) & 63]);
                sb.append(chars[64]);
                sb.append(chars[64]);
                break;
            case 2: // Three more output bytes and one pad.
                ival = ((int) bytes[i++] + 256) & 0xff;
                ival <<= 8;
                ival += ((int) bytes[i] + 256) & 0xff;
                ival <<= 8;
                sb.append(chars[(ival >> 18) & 63]);
                sb.append(chars[(ival >> 12) & 63]);
                sb.append(chars[(ival >> 6) & 63]);
                sb.append(chars[64]);
                break;
        }
        return new String(sb);
    }

    /**
     * Performs RFC1521 style Base64 decoding of Base64 encoded data. The output
     * is a byte array containing the decoded binary data. The input is expected
     * to be a normal Unicode String object.
     *
     * @param s The Base64 encoded string to decode into binary data.
     * @return An array of bytes containing the decoded data.
     */
    public static final byte[] Base64StringToBytes(String s) {
        if (s.equals("") || s == null)
            return null;
        try {
            StringCharacterIterator iter = new StringCharacterIterator(s);
            ByteArrayOutputStream bytestr = new ByteArrayOutputStream();
            DataOutputStream outstr = new DataOutputStream(bytestr);
            char c;
            int d, i, group;
            int[] bgroup = new int[4];
            decode:
            for (i = 0, group = 0, c = iter.first(); c != CharacterIterator.DONE; c = iter.next()) {
                switch (c) {
                    case 'A':
                        d = 0;
                        break;
                    case 'B':
                        d = 1;
                        break;
                    case 'C':
                        d = 2;
                        break;
                    case 'D':
                        d = 3;
                        break;
                    case 'E':
                        d = 4;
                        break;
                    case 'F':
                        d = 5;
                        break;
                    case 'G':
                        d = 6;
                        break;
                    case 'H':
                        d = 7;
                        break;
                    case 'I':
                        d = 8;
                        break;
                    case 'J':
                        d = 9;
                        break;
                    case 'K':
                        d = 10;
                        break;
                    case 'L':
                        d = 11;
                        break;
                    case 'M':
                        d = 12;
                        break;
                    case 'N':
                        d = 13;
                        break;
                    case 'O':
                        d = 14;
                        break;
                    case 'P':
                        d = 15;
                        break;
                    case 'Q':
                        d = 16;
                        break;
                    case 'R':
                        d = 17;
                        break;
                    case 'S':
                        d = 18;
                        break;
                    case 'T':
                        d = 19;
                        break;
                    case 'U':
                        d = 20;
                        break;
                    case 'V':
                        d = 21;
                        break;
                    case 'W':
                        d = 22;
                        break;
                    case 'X':
                        d = 23;
                        break;
                    case 'Y':
                        d = 24;
                        break;
                    case 'Z':
                        d = 25;
                        break;
                    case 'a':
                        d = 26;
                        break;
                    case 'b':
                        d = 27;
                        break;
                    case 'c':
                        d = 28;
                        break;
                    case 'd':
                        d = 29;
                        break;
                    case 'e':
                        d = 30;
                        break;
                    case 'f':
                        d = 31;
                        break;
                    case 'g':
                        d = 32;
                        break;
                    case 'h':
                        d = 33;
                        break;
                    case 'i':
                        d = 34;
                        break;
                    case 'j':
                        d = 35;
                        break;
                    case 'k':
                        d = 36;
                        break;
                    case 'l':
                        d = 37;
                        break;
                    case 'm':
                        d = 38;
                        break;
                    case 'n':
                        d = 39;
                        break;
                    case 'o':
                        d = 40;
                        break;
                    case 'p':
                        d = 41;
                        break;
                    case 'q':
                        d = 42;
                        break;
                    case 'r':
                        d = 43;
                        break;
                    case 's':
                        d = 44;
                        break;
                    case 't':
                        d = 45;
                        break;
                    case 'u':
                        d = 46;
                        break;
                    case 'v':
                        d = 47;
                        break;
                    case 'w':
                        d = 48;
                        break;
                    case 'x':
                        d = 49;
                        break;
                    case 'y':
                        d = 50;
                        break;
                    case 'z':
                        d = 51;
                        break;
                    case '0':
                        d = 52;
                        break;
                    case '1':
                        d = 53;
                        break;
                    case '2':
                        d = 54;
                        break;
                    case '3':
                        d = 55;
                        break;
                    case '4':
                        d = 56;
                        break;
                    case '5':
                        d = 57;
                        break;
                    case '6':
                        d = 58;
                        break;
                    case '7':
                        d = 59;
                        break;
                    case '8':
                        d = 60;
                        break;
                    case '9':
                        d = 61;
                        break;
                    case '+':
                        d = 62;
                        break;
                    case '/':
                        d = 63;
                        break;
                    default:
                        // Any character not in Base64 alphabet is treated
                        // as end of data. This includes the '=' (pad) char.
                        break decode; // Skip illegal characters.
                }
                bgroup[i++] = d;
                if (i >= 4) {
                    i = 0;
                    group = ((bgroup[0] & 63) << 18) + ((bgroup[1] & 63) << 12) + ((bgroup[2] & 63) << 6)
                            + (bgroup[3] & 63);
                    outstr.writeByte(((group >> 16) & 255));
                    outstr.writeByte(((group >> 8) & 255));
                    outstr.writeByte(group & 255);
                }
            }
            // Handle the case of remaining characters and
            // pad handling. If input is not a multiple of 4
            // in length, then '=' pads are assumed.
            switch (i) {
                case 2:
                    // One output byte from two input bytes.
                    group = ((bgroup[0] & 63) << 18) + ((bgroup[1] & 63) << 12);
                    outstr.writeByte(((group >> 16) & 255));
                    break;
                case 3:
                    // Two output bytes from three input bytes.
                    group = ((bgroup[0] & 63) << 18) + ((bgroup[1] & 63) << 12) + ((bgroup[2] & 63) << 6);
                    outstr.writeByte(((group >> 16) & 255));
                    outstr.writeByte(((group >> 8) & 255));
                    break;
                default:
                    // Any other case, including correct 0, is treated as
                    // end of data.
                    break;
            }
            outstr.flush();
            return bytestr.toByteArray();
        } catch (IOException e) {
        } // Won't happen. Return null if it does.
        return null;
    }

    /**
     * Int32杞崲涓篠tring
     *
     * @param value
     * @return
     */
    public static String Int32ToString(int value) {
        return Integer.toString(value);
    }

    /**
     * String 杞崲涓篒nt32
     *
     * @param value
     * @return
     */
    public static int StringToInt32(String value) {
        return Integer.parseInt(value);
    }

    /**
     * Short绫诲瀷杞崲涓篠tring
     *
     * @param value
     * @return
     */
    public static String Int16ToString(short value) {
        return Short.toString(value);
    }

    /**
     * String杞崲涓篠hort
     *
     * @param value
     * @return
     */
    public static Short StringToInt16(String value) {
        return Short.parseShort(value);
    }

    /**
     * Long杞崲涓篠tring
     *
     * @param value
     * @return
     */
    public static String Int64ToString(long value) {
        return Long.toString(value);
    }

    /**
     * String杞崲涓篖ong
     *
     * @param value
     * @return
     */
    public static long StringToInt64(String value) {
        return Long.parseLong(value);
    }

    /**
     * Double杞崲涓篠tring
     *
     * @param value
     * @return
     */
    public static String DoubleToString(double value) {
        return Double.toString(value);
    }

    /**
     * String杞崲涓篋ouble
     *
     * @param value
     * @return
     */
    public static double StringToDouble(String value) {
        return Double.parseDouble(value);
    }

    /**
     * Float杞崲涓篠tring
     *
     * @param value
     * @return
     */
    public static String FloatToString(float value) {
        return Float.toString(value);
    }

    /**
     * String杞崲涓篎loat
     *
     * @param value
     * @return
     */
    public static float StringToFloat(String value) {
        return Float.parseFloat(value);
    }

    /**
     * String杞崲涓築igDecimal
     *
     * @param value
     * @return
     */
    public static BigDecimal StringToDecimal(String value) {
        if (value == null || value.equals(""))
            value = "0";
        return new BigDecimal(value);
    }

    /**
     * Double杞崲涓築igDecimal
     *
     * @param value
     * @return
     */
    public static BigDecimal DoubleToDecimal(double value) {
        return new BigDecimal(value);
    }

    /**
     * BigDecimal杞崲涓篠tring
     *
     * @param value
     * @return
     */
    public static String DecimalToString(BigDecimal value) {
        return value.toString();
    }

    /**
     * AscII浠ｇ爜杞崲涓篊har
     *
     * @param code
     * @return
     */
    public static Character AscIIToChar(int code) {
        Character aChar = new Character((char) code);
        return aChar;
    }

    /**
     * Char杞崲涓篒nt
     *
     * @param ch
     * @return
     */
    public static Integer CharToInt(char ch) {
        int i = (int) ch;
        return i;
    }

    /**
     * Boolean杞崲涓篒nt
     *
     * @param value
     * @return
     */
    public static Integer BooleanToInt(boolean value) {
        int i = (value) ? 1 : 0;
        return i;
    }

    /**
     * Int杞崲涓築oolean
     *
     * @param value
     * @return
     */
    public static Boolean IntToBoolean(int value) {
        boolean i = (value != 0);
        return i;
    }

    /**
     * 閫氱敤Object杞崲涓篒nt32鐢�
     *
     * @param value
     * @return
     */
    public static Integer ToInt32(Object value) {
        if (value instanceof Integer) {
            int value2 = ((Integer) value).intValue();
            return value2;
        } else if (value instanceof Short) {
            Short s = (Short) value;
            return s.intValue();
        } else if (value instanceof String) {
            String s = (String) value;
            return StringToInt32(s);
        } else if (value instanceof StringBuffer) {
            StringBuffer s = (StringBuffer) value;
            return StringToInt32(s.toString());
        } else if (value instanceof Double) {
            double d = ((Double) value).doubleValue();
            int value2 = (int) d;
            return value2;
        } else if (value instanceof Float) {
            float f = ((Float) value).floatValue();
            int value2 = (int) f;
            return value2;
        } else if (value instanceof Long) {
            long l = ((Long) value).longValue();
            int value2 = (int) l;
            return value2;
        } else if (value instanceof Boolean) {
            boolean b = ((Boolean) value).booleanValue();
            return BooleanToInt(b);
        } else if (value instanceof Date) {
            // Date d = (Date) value;
            return 0;
        } else if (value instanceof BigDecimal) {
            BigDecimal d = (BigDecimal) value;
            int value2 = d.toBigInteger().intValue();
            return value2;
        } else if (value instanceof BigInteger) {
            BigInteger d = (BigInteger) value;
            int value2 = d.intValue();
            return value2;
        }
        return 0;
    }

    /**
     * 閫氱敤Object杞崲涓篠hort
     *
     * @param value
     * @return
     */
    public static Short ToInt16(Object value) {
        if (value instanceof Integer) {
            int value2 = ((Integer) value).intValue();
            return (short) value2;
        } else if (value instanceof Short) {
            Short s = (Short) value;
            return s;
        } else if (value instanceof String) {
            String s = (String) value;
            return StringToInt16(s);
        } else if (value instanceof StringBuffer) {
            StringBuffer s = (StringBuffer) value;
            return StringToInt16(s.toString());
        } else if (value instanceof Double) {
            double d = ((Double) value).doubleValue();
            int value2 = (int) d;
            return (short) value2;
        } else if (value instanceof Float) {
            float f = ((Float) value).floatValue();
            int value2 = (int) f;
            return (short) value2;
        } else if (value instanceof Long) {
            long l = ((Long) value).longValue();
            int value2 = (int) l;
            return (short) value2;
        } else if (value instanceof Boolean) {
            boolean b = ((Boolean) value).booleanValue();
            int i = (b) ? 1 : 0;
            return (short) i;
        } else if (value instanceof Date) {
            // Date d = (Date) value;
            return (short) 0;
        } else if (value instanceof BigDecimal) {
            BigDecimal d = (BigDecimal) value;
            int value2 = d.toBigInteger().intValue();
            return (short) value2;
        }
        return 0;
    }

    /**
     * 閫氱敤Object杞崲涓篖ong
     *
     * @param value
     * @return
     */
    public static Long ToInt64(Object value) {
        if (value instanceof Integer) {
            int value2 = ((Integer) value).intValue();
            return (long) value2;
        } else if (value instanceof Short) {
            Short s = (Short) value;
            return (long) s;
        } else if (value instanceof String) {
            String s = (String) value;
            return StringToInt64(s);
        } else if (value instanceof StringBuffer) {
            StringBuffer s = (StringBuffer) value;
            return StringToInt64(s.toString());
        } else if (value instanceof Double) {
            double d = ((Double) value).doubleValue();
            int value2 = (int) d;
            return (long) value2;
        } else if (value instanceof Float) {
            float f = ((Float) value).floatValue();
            int value2 = (int) f;
            return (long) value2;
        } else if (value instanceof Long) {
            long l = ((Long) value).longValue();
            return l;
        } else if (value instanceof Boolean) {
            boolean b = ((Boolean) value).booleanValue();
            int i = (b) ? 1 : 0;
            return (long) i;
        } else if (value instanceof Date) {
            // Date d = (Date) value;
            return (long) 0;
        } else if (value instanceof BigDecimal) {
            BigDecimal d = (BigDecimal) value;
            long value2 = d.toBigInteger().longValue();
            return value2;
        }
        return (long) 0;
    }

    /**
     * 閫氱敤Object杞崲涓篠tring
     *
     * @param value
     * @return
     * @throws @throws Throwable
     */
    public static String ToString(Object value) {
        if (value instanceof Integer) {
            int value2 = ((Integer) value).intValue();
            return Int32ToString(value2);
        } else if (value instanceof Short) {
            Short s = (Short) value;
            return Int16ToString(s);
        } else if (value instanceof Character) {
            Character s = (Character) value;
            return s.toString();

        } else if (value instanceof String) {
            String s = (String) value;
            return s;
        } else if (value instanceof StringBuffer) {
            StringBuffer s = (StringBuffer) value;
            return s.toString();
        } else if (value instanceof Double) {
            double d = ((Double) value).doubleValue();
            String str = DoubleToString(d);
            if (str.endsWith(".0"))
                str = str.replaceAll("\\.0", "");
            if (str.endsWith(".00"))
                str = str.replaceAll("\\.00", "");
            return str;
        } else if (value instanceof Float) {
            float f = ((Float) value).floatValue();
            String str = FloatToString(f);
            if (str.endsWith(".0"))
                str = str.replaceAll("\\.0", "");
            if (str.endsWith(".00"))
                str = str.replaceAll("\\.00", "");
            return str;
        } else if (value instanceof Long) {
            long l = ((Long) value).longValue();
            return Int64ToString(l);
        } else if (value instanceof java.sql.Blob) {
            java.sql.Blob blob = (java.sql.Blob) value;
            if (blob == null)
                return "";
            byte[] data = Convert.BlobToBytes(blob);
            /*
             * try { return new String(data,"UTF-8"); } catch
             * (UnsupportedEncodingException e) { e.printStackTrace(); }
             */
            return ByteUtils.logBytes(data);
            // return Convert.BytesToHex(data);
        } else if (value instanceof Clob) {
            Clob clob = (Clob) value;
            if (clob == null)
                return "";
            return Convert.ClobToString(clob);
        } else if (value instanceof Boolean) {
            boolean b = ((Boolean) value).booleanValue();
            return b ? "true" : "false";
        } else if (value instanceof Date) {
            Date d = (Date) value;
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String str = formatter.format(d);
            return str;
        } else if (value instanceof Timestamp) {
            Timestamp d2 = (Timestamp) value;
            Date d = Convert.ConvertToSQLDate(d2);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String str = formatter.format(d);
            return str;
        } else if (value instanceof Calendar) {
            Calendar d = (Calendar) value;
            Date d2 = d.getTime();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String str = formatter.format(d2);
            return str;

        } else if (value instanceof BigDecimal) {
            BigDecimal d = (BigDecimal) value;
            return d.toString();
        }
        return "";
    }

    public static String DateToStr(Date date, String dateFormat) {
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
        String str = formatter.format(date);
        return str;
    }

    public static String SQLDateToStr(Timestamp date, String dateFormat) {
        Date date2 = Convert.ToDateTime(date);
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
        String str = formatter.format(date2);
        return str;
    }

    /**
     * 閫氱敤Object杞崲涓篋ouble
     *
     * @param value
     * @return
     */
    public static Double ToDouble(Object value) {
        if (value instanceof Integer) {
            int value2 = ((Integer) value).intValue();
            return StringToDouble(Int32ToString(value2));
        } else if (value instanceof Short) {
            Short s = (Short) value;
            return StringToDouble(Int16ToString(s));
        } else if (value instanceof String) {
            String s = (String) value;
            return StringToDouble(s);
        } else if (value instanceof StringBuffer) {
            StringBuffer s = (StringBuffer) value;
            return StringToDouble(s.toString());
        } else if (value instanceof Double) {
            double d = ((Double) value).doubleValue();
            return d;
        } else if (value instanceof Float) {
            float f = ((Float) value).floatValue();
            return StringToDouble(FloatToString(f));
        } else if (value instanceof Long) {
            long l = ((Long) value).longValue();
            return StringToDouble(Int64ToString(l));
        } else if (value instanceof Boolean) {
            boolean b = ((Boolean) value).booleanValue();
            double d = b ? (double) 1 : (double) 0;
            return d;
        } else if (value instanceof Date) {
            Date d = (Date) value;
            return Convert.ToDouble(d.getTime());
        } else if (value instanceof BigDecimal) {
            BigDecimal d = (BigDecimal) value;
            double value2 = d.doubleValue();
            return value2;
        }
        return (double) 0;
    }

    /**
     * 閫氱敤Object杞崲涓築oolean
     *
     * @param value
     * @return
     */
    public static Boolean ToBoolean(Object value) {
        if (value instanceof String) {
            String s = (String) value;
            if (s.equalsIgnoreCase("true"))
                return true;
            if (s.equalsIgnoreCase("t"))
                return true;
            if (s.equalsIgnoreCase("1"))
                return true;

            if (s.equalsIgnoreCase("false"))
                return false;
            if (s.equalsIgnoreCase("f"))
                return false;
            if (s.equalsIgnoreCase("0"))
                return false;

        }
        int iValue = Convert.ToInt32(value);
        return IntToBoolean(iValue);
    }

    /**
     * 閫氱敤Object杞崲涓篎loat
     *
     * @param value
     * @return
     */
    public static Float ToFloat(Object value) {
        if (value instanceof Integer) {
            int value2 = ((Integer) value).intValue();
            return StringToFloat(Int32ToString(value2));
        } else if (value instanceof Short) {
            Short s = (Short) value;
            return StringToFloat(Int16ToString(s));
        } else if (value instanceof String) {
            String s = (String) value;
            return StringToFloat(s);
        } else if (value instanceof StringBuffer) {
            StringBuffer s = (StringBuffer) value;
            return StringToFloat(s.toString());
        } else if (value instanceof Double) {
            double d = ((Double) value).doubleValue();
            return StringToFloat(DoubleToString(d));
        } else if (value instanceof Float) {
            float f = ((Float) value).floatValue();
            return f;
        } else if (value instanceof Long) {
            long l = ((Long) value).longValue();
            return StringToFloat(Int64ToString(l));
        } else if (value instanceof Boolean) {
            boolean b = ((Boolean) value).booleanValue();
            float d = b ? (float) 1 : (float) 0;
            return d;
        } else if (value instanceof Date) {
            Date d = (Date) value;
            return Convert.ToFloat(d.getTime());
        } else if (value instanceof BigDecimal) {
            BigDecimal d = (BigDecimal) value;
            float value2 = d.floatValue();
            return value2;
        }
        return (float) 0;
    }

    /**
     * 閫氱敤Object杞崲涓築igDecimal
     *
     * @param value
     * @return
     */
    public static BigDecimal ToDecimal(Object value) {
        if (value instanceof Integer) {
            int value2 = ((Integer) value).intValue();
            return StringToDecimal(Int32ToString(value2));
        } else if (value instanceof Short) {
            Short s = (Short) value;
            return StringToDecimal(Int16ToString(s));
        } else if (value instanceof String) {
            String s = (String) value;
            return StringToDecimal(s);
        } else if (value instanceof StringBuffer) {
            StringBuffer s = (StringBuffer) value;
            return StringToDecimal(s.toString());
        } else if (value instanceof Double) {
            double d = ((Double) value).doubleValue();
            return StringToDecimal(DoubleToString(d));
        } else if (value instanceof Float) {
            float f = ((Float) value).floatValue();
            return StringToDecimal(FloatToString(f));
        } else if (value instanceof Long) {
            long l = ((Long) value).longValue();
            return StringToDecimal(Int64ToString(l));
        } else if (value instanceof Boolean) {
            boolean b = ((Boolean) value).booleanValue();
            BigDecimal d = b ? new BigDecimal(1) : new BigDecimal(0);
            return d;
        } else if (value instanceof Date) {
            Date d = (Date) value;
            return Convert.ToDecimal(d.getTime());
        } else if (value instanceof BigDecimal) {
            BigDecimal d = (BigDecimal) value;
            return d;
        }
        return new BigDecimal(0);
    }

    /**
     * 閫氱敤Object杞崲涓篋ate
     *
     * @param value
     * @return
     */
    public static Date ToDateTime(Object value) {
        if (value instanceof Date) {
            Date d = (Date) value;
            return d;
        }
        if (value instanceof Timestamp) {
            Timestamp d = (Timestamp) value;
            return new Date(d.getTime());
        }
        if (value instanceof String) {
            String d = Convert.ToString(value);
            return StringToDateTime(d);
        }
        if (value instanceof StringBuffer) {
            StringBuffer d = (StringBuffer) value;
            return StringToDateTime(d.toString());
        }
        if (value instanceof Long) {
            long lValue = Convert.ToInt64(value);
            Date d = new Date(lValue);
            return d;
        }
        if (value instanceof Integer) {
            long lValue = Convert.ToInt64(value);
            Date d = new Date(lValue);
            return d;
        }
        if (value instanceof Short) {
            long lValue = Convert.ToInt64(value);
            Date d = new Date(lValue);
            return d;
        }
        if (value instanceof Double) {
            long lValue = Convert.ToInt64(value);
            Date d = new Date(lValue);
            return d;
        }
        if (value instanceof Float) {
            long lValue = Convert.ToInt64(value);
            Date d = new Date(lValue);
            return d;
        }
        if (value instanceof BigDecimal) {
            long lValue = Convert.ToInt64(value);
            Date d = new Date(lValue);
            return d;
        }
        Calendar calender = Calendar.getInstance();
        return calender.getTime();

    }

    public static Date StringToDateTime(String value, String sFormat) {
        String DateTime = value;
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        SimpleDateFormat s = new SimpleDateFormat(sFormat);

        Date date = new Date();
        try {
            date = s.parse(DateTime);
        } catch (ParseException e) {
        }
        calendar.setTime(date);
        return date;
    }

    public static Date StringToDateTime(String value) {
        if (value.equals("0000-00-00"))
            value = "1970-01-01";
        if (value.length() > 12)
            return StringToDateTime(value, "yyyy-MM-dd HH:mm:ss");
        return StringToDateTime(value, "yyyy-MM-dd");
    }

    /**
     * 閫氱敤Object杞崲涓篋ate
     *
     * @param value
     * @return
     */
    public static Timestamp ToSQLDateTime(Object value) {
        if (value instanceof Date) {
            Date d = (Date) value;
            return new Timestamp(d.getTime());
        }
        if (value instanceof Timestamp) {
            Timestamp d = (Timestamp) value;
            return d;
        }
        if (value instanceof String) {
            String d = Convert.ToString(value);
            if (d == null || d.equals("") || d.equals("0000-00-00")) {
                return new Timestamp(1970, 1, 1, 0, 0, 0, 0);
            }
            Date d2 = StringToDateTime(d);
            return ConvertToSQLDate(d2);
        }
        if (value instanceof StringBuffer) {
            StringBuffer d = (StringBuffer) value;
            Date d2 = StringToDateTime(d.toString());
            return ConvertToSQLDate(d2);
        }
        if (value instanceof Long) {
            long lValue = Convert.ToInt64(value);
            Timestamp d = new Timestamp(lValue);
            return d;
        }
        if (value instanceof Integer) {
            long lValue = Convert.ToInt64(value);
            Timestamp d = new Timestamp(lValue);
            return d;
        }
        if (value instanceof Short) {
            long lValue = Convert.ToInt64(value);
            Timestamp d = new Timestamp(lValue);
            return d;
        }
        if (value instanceof Double) {
            long lValue = Convert.ToInt64(value);
            Timestamp d = new Timestamp(lValue);
            return d;
        }
        if (value instanceof Float) {
            long lValue = Convert.ToInt64(value);
            Timestamp d = new Timestamp(lValue);
            return d;
        }
        if (value instanceof BigDecimal) {
            long lValue = Convert.ToInt64(value);
            Timestamp d = new Timestamp(lValue);
            return d;
        }
        Calendar calender = Calendar.getInstance();
        Date date = calender.getTime();
        return new Timestamp(date.getTime());

    }

    /**
     * java.util.Date Convert to java.sql.Date
     *
     * @param value
     * @return
     */
    public static Timestamp ConvertToSQLDate(Date value) {
        return new Timestamp(value.getTime());
    }

    /**
     * 鍒ゆ柇Object鏄惁涓篘ull
     *
     * @param value
     * @return
     */
    public static Boolean IsNull(Object value) {
        if (value == null)
            return true;
        return false;
    }

    /**
     * Blob Convert to Byte Array
     *
     * @param value
     * @return
     */
    public static byte[] BlobToBytes(java.sql.Blob value) {
        if (value == null)
            return null;
        byte[] data = null;
        try {
            data = value.getBytes(1, (int) value.length());
            return data;
        } catch (SQLException e) {
            return null;
        }
    }

    /**
     * CLOB澶у瓧娈佃浆鎹�
     *
     * @param cl
     * @return
     * @throws SQLException
     * @throws IOException
     */
    public static String ClobToString(Clob cl) {
        try {
            String reString = "";
            Reader is = cl.getCharacterStream();// 寰楀埌娴�
            BufferedReader br = new BufferedReader(is);
            String s = br.readLine();
            StringBuffer sb = new StringBuffer();
            // 鎵ц寰幆灏嗗瓧绗︿覆鍏ㄩ儴鍙栧嚭浠樺�肩粰StringBuffer鐢盨tringBuffer杞垚STRING
            while (s != null) {
                sb.append(s);
                s = br.readLine();
            }
            reString = sb.toString();
            return reString;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "";
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "";
        }
    }

    /**
     * Byte Array Convert to Blob
     *
     * @param value
     * @return
     */
    public static java.sql.Blob BytesToBlob(byte[] value) {
        try {
            java.sql.Blob blob = new SerialBlob(value);
            return blob;
        } catch (SerialException e) {
            return null;
        } catch (SQLException e) {
            return null;
        }
    }

    public static java.sql.Blob StringToBlob(String value) {
        try {
            byte[] b = StringToUTF8Bytes(value);
            java.sql.Blob blob = BytesToBlob(b);
            return blob;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * UTF8 Byte Array Convert to Unicode String
     *
     * @param utf8
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String UTF8BytesToString(byte[] utf8) throws UnsupportedEncodingException {
        if (utf8 == null)
            return null;
        return new String(utf8, "UTF-8");
    }

    /**
     * Unicode String Convert to UTF8 Byte Array
     *
     * @param sourceStr
     * @return
     * @throws UnsupportedEncodingException
     */
    public static byte[] StringToUTF8Bytes(String sourceStr) throws UnsupportedEncodingException {
        if (sourceStr.equals("") || sourceStr == null)
            return null;
        byte[] utf8 = sourceStr.getBytes("UTF-8");
        return utf8;
    }

    /**
     * GBK Byte Array Convert to Unicode String
     *
     * @param utf8
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String GBKBytesToString(byte[] gbk) throws UnsupportedEncodingException {
        if (gbk == null)
            return null;
        return new String(gbk, "GBK");
    }

    /**
     * GBK String Convert to UTF8 Byte Array
     *
     * @param sourceStr
     * @return
     * @throws UnsupportedEncodingException
     */
    public static byte[] StringToGBKBytes(String sourceStr) throws UnsupportedEncodingException {
        if (sourceStr.equals("") || sourceStr == null)
            return null;
        byte[] gbk = sourceStr.getBytes("GBK");
        return gbk;
    }

    /**
     * UTF8 String Convert to GBK String
     *
     * @param sourceStr
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String UTF8ToGBK(String sourceStr) throws UnsupportedEncodingException {
        if (sourceStr.equals("") || sourceStr == null)
            return "";
        byte[] utf8 = sourceStr.getBytes("UTF-8");
        return new String(utf8, "GBK");
    }

    /**
     * GBK String Convert to UTF8 String
     *
     * @param sourceStr
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String GBKToUTF8(String sourceStr) throws UnsupportedEncodingException {
        if (sourceStr.equals("") || sourceStr == null)
            return "";
        byte[] utf8 = sourceStr.getBytes("GBK");
        return new String(utf8, "UTF-8");
    }

    /**
     * Hex String to Integer
     *
     * @param hexStr
     * @return
     */
    public static int HexToInt(String hexStr) {
        int i = Integer.valueOf(hexStr, 16).intValue();
        return i;
    }

    /**
     * Get Date1 and Date2's Days
     *
     * @param date1
     * @param date2
     * @return
     */
    public static int DaysDiff(Date date1, Date date2) {
        long diff = date2.getTime() - date1.getTime();
        if (diff < 0.0)
            diff = -diff;
        diff = diff / (1000 * 60 * 60 * 24);
        return Convert.ToInt32(diff);
    }

    /**
     * 鑾峰彇褰撳ぉ鐨勬棩鏈�
     *
     * @return "YYYY-MM-DD"
     */
    public static String getTodayString() {
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
        Date now = new Date();
        return sd.format(now);
    }

    /**
     * 鑾峰彇褰撳墠鏃堕棿
     *
     * @return HH:mm:ss
     */
    public static String getNowTime() {
        SimpleDateFormat sd = new SimpleDateFormat("HH:mm:ss");
        Date now = new Date();
        return sd.format(now);
    }

    /**
     * 鑾峰彇鐜板湪鐨勬椂闂�
     *
     * @return "YYYY-MM-DD HH:MM:SS"
     */
    public static String getNowString() {
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();
        return sd.format(now);
    }

    /**
     * 鑾峰彇鏄ㄥぉ鏃ユ湡
     *
     * @return
     */
    public static String getLastTodayString() {
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
        Date now = new Date();
        Date lastday = addDate(now, -1);
        return sd.format(lastday);
    }

    /**
     * 鑾峰彇鏄庡ぉ鏃ユ湡
     *
     * @return
     */
    public static String getNextTodayString() {
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
        Date now = new Date();
        Date nextday = addDate(now, 1);
        return sd.format(nextday);
    }

    private static Calendar fromCal = Calendar.getInstance();

    /**
     * 鍙栧緱鏌愬ぉ鐩稿姞(鍑�)寰岀殑閭ｄ竴澶�
     *
     * @param date 鍘熸棩鏈�
     * @param day  鍔犲ぉ鏁�
     * @return 鏃ユ湡
     */
    public static Date addDate(Date date, int day) {
        try {
            fromCal.setTime(date);
            fromCal.add(Calendar.DATE, day);

            // System.out.println("Date:"+dateFormat.format(fromCal.getTime()));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return fromCal.getTime();
    }

    public static String formatTime(long timeLong) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(timeLong);
    }

    /**
     * 杞崲瀛楃涓蹭负Calendar绫诲瀷
     *
     * @param sDate           String 瑕佽浆鎹㈢殑鏃ユ湡
     * @param sDateTimeFormat String 瑕佽浆鎹㈢殑鏃ユ湡鏍煎紡
     * @return Calendar 杩斿洖鎸囧畾鏍煎紡鐨勬棩鏈�
     */
    public static Calendar stringToCalendar(String sDate, String sDateTimeFormat) {
        Calendar cal = null;
        if (sDate != null && !sDate.equals("")) {
            try {
                cal = Calendar.getInstance();
                cal.setTime((new SimpleDateFormat(sDateTimeFormat)).parse(sDate));
            } catch (Exception exc) {
                cal = null;
            }
        }
        return cal;
    }

    /**
     * 鏃ユ湡灏忔椂璁＄畻(鍔犳垨鑰呭噺)
     *
     * @param sDate 鏃ユ湡瀵硅薄,濡�:2007-09-25 01:12:28
     * @param oper  oper 鍒嗛挓, 鍔�20灏忔椂濡�: +20, 鍑�20灏忔椂濡�: -20
     * @return 澶勭悊鍚庣殑鏃ユ湡
     */
    public static Calendar calHour(String sDate, int oper) {
        if (sDate != null) {
            try {
                Calendar cal = stringToCalendar(sDate, "yyyy-MM-dd HH:mm:ss");
                cal.add(Calendar.HOUR, oper);
                return cal;
            } catch (Exception exc) {
                return null;
            }
        }
        return null;
    }

    /**
     * 鏃ユ湡灏忔椂璁＄畻(鍔犳垨鑰呭噺)
     *
     * @param sDate 鏃ユ湡瀵硅薄,濡�:2007-09-25 01:12:28
     * @param oper  oper 鍒嗛挓, 鍔�20灏忔椂濡�: +20, 鍑�20灏忔椂濡�: -20
     * @return 澶勭悊鍚庣殑鏃ユ湡
     */
    public static Calendar calMinute(String sDate, int oper) {
        if (sDate != null) {
            try {
                Calendar cal = stringToCalendar(sDate, "yyyy-MM-dd HH:mm:ss");
                cal.add(Calendar.MINUTE, oper);
                return cal;
            } catch (Exception exc) {
                return null;
            }
        }
        return null;
    }

    /**
     * 鍔熻兘璇存槑: 杞崲鏃ユ湡鏃堕棿涓烘寚瀹氱殑瀛楃涓叉牸寮�
     *
     * @param sourceObject 瑕佽浆鎹㈢殑鏃ユ湡鏍煎紡鐨勫璞�,鍙互鏄棩鏈熷瓧绗︿覆,涔熷彲浠ユ槸鏃ユ湡绫诲瀷.
     *                     瑕佹眰鏃ユ湡瀛楃涓插弬鏁版牸寮忎负:yyyy-MM-dd HH:mm:ss
     * @return 杩斿洖鏃ユ湡"瀛楃涓�"鏍煎紡:yyyy-MM-dd HH:mm:ss
     * @author Snoopy Chen
     */
    public static String formatDateTime(Object sourceObject) {
        if (sourceObject == null) {
            return "";
        } else if (sourceObject instanceof String) {
            SimpleDateFormat dF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return dF.format(stringToDate((String) sourceObject));
        } else if (sourceObject instanceof Date) {
            SimpleDateFormat dF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return dF.format((Date) sourceObject);
        } else if (sourceObject instanceof Calendar) {
            SimpleDateFormat dF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Calendar t = (Calendar) sourceObject;
            return dF.format(t.getTime());
        }
        return "";
    }

    /**
     * 杞崲瀛楃涓蹭负Date绫诲瀷
     *
     * @param sDate String
     * @return Calendar 鏍煎紡涓�:yyyy-MM-dd 鎴栬��:yyyy-MM-dd HH:mm
     */
    public static Date stringToDate(String sDate) {
        if (sDate != null && !sDate.equals("")) {
            try {
                String sDateTimeFormat = "yyyy-MM-dd HH:mm:ss";
                if (sDate.length() == 8)
                    sDateTimeFormat = "HH:mm:ss";
                if (sDate.length() == 10)
                    sDateTimeFormat = "yyyy-MM-dd";
                Date dt = (new SimpleDateFormat(sDateTimeFormat)).parse(sDate);

                return dt;
            } catch (Exception exc) {
                return null;
            }
        }
        return null;
    }

    public static Date stringToDate(String sDate, String sDateTimeFormat) {
        Date dt = null;
        if (sDate != null && !sDate.equals("")) {
            try {
                dt = new Date();
                if (sDate.length() == 5)
                    sDateTimeFormat = "HH:mm";
                else if (sDate.length() == 8)
                    sDateTimeFormat = "HH:mm:ss";
                else if (sDate.length() == 10)
                    sDateTimeFormat = "yyyy-MM-dd";
                dt = (new SimpleDateFormat(sDateTimeFormat)).parse(sDate);
            } catch (Exception exc) {
                dt = null;
            }
        }
        return dt;
    }

    public static String dateFormat(Date date, String sDateTimeFormat) {
        String strdt = null;
        if (date != null) {
            try {
                SimpleDateFormat format = new SimpleDateFormat(sDateTimeFormat);
                strdt = format.format(date);
            } catch (Exception exc) {
                return strdt;
            }
        }
        return strdt;
    }

    /**
     * 鑾峰彇褰撳墠鏃堕棿(骞�-鏈�-鏃�)
     *
     * @return String
     * @author: ctfzh
     */
    public static String getShortDate() {
        SimpleDateFormat dF = new SimpleDateFormat("yyyy-MM-dd");
        return dF.format(new Date());
    }

    /**
     * 鑾峰彇褰撳墠鏃堕棿(骞�-鏈�-鏃� 鏃跺垎绉�)
     *
     * @return String
     * @author: wzh
     */
    public static String getNowFullTime() {
        SimpleDateFormat dF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dF.format(new Date());
    }

    /**
     * 鑾峰彇褰撳墠鏃堕棿(骞�-鏈�-鏃�)
     *
     * @param pDate Date
     * @return String
     * @author: ctfzh
     */
    public static String getShortDate(Date pDate) {
        SimpleDateFormat dF = new SimpleDateFormat("yyyy-MM-dd");
        return dF.format(pDate);
    }

    /**
     * 鑾峰彇褰撳墠鏃堕棿(骞�-鏈�-鏃� 鏃�:鍒�:绉�)
     *
     * @return String
     */
    public static String getLongDate() {
        SimpleDateFormat dF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dF.format(new Date());
    }

    // 姣旇緝鏃堕棿锛寀nit鏄椂闂村崟浣�
    public static int compareTime(String time1, String time2, String unit) {
        if (time1 == null || time2 == null || time1.equals("") || time2.equals(""))
            return -1;

        try {
            Date dt1 = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).parse(time1);
            Date dt2 = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).parse(time2);

            long time1_L = dt1.getTime();
            long time2_L = dt2.getTime();
            if (time1_L == time2_L) {
                return 0;
            }
            long compareTime = time2_L - time1_L;

            if (unit.equals("dd")) {
                compareTime = compareTime / (24 * 3600 * 1000);
            } else if (unit.equals("hh")) {
                compareTime = compareTime / (3600 * 1000);
            } else if (unit.equals("mm")) {
                compareTime = compareTime / (60 * 1000);
            } else if (unit.equals("ss")) {
                compareTime = compareTime / (1000);
            } else {
                compareTime = -1;
            }

            int time = new Long(compareTime).intValue();
            return time;
        } catch (Exception e) {
            return -1;
        }
    }

    // 姣旇緝鏃堕棿锛寀nit鏄椂闂村崟浣�
    public static int compareTime(Date time1, Date time2, String unit) {
        if (time1 == null || time2 == null) {
            return -1;
        }

        try {
            long time1_L = time1.getTime();
            long time2_L = time2.getTime();
            if (time1_L == time2_L) {
                return 0;
            }
            long compareTime = time2_L - time1_L;

            if (unit.equals("dd")) {
                compareTime = compareTime / (24 * 3600 * 1000);
            } else if (unit.equals("hh")) {
                compareTime = compareTime / (3600 * 1000);
            } else if (unit.equals("mm")) {
                compareTime = compareTime / (60 * 1000);
            } else if (unit.equals("ss")) {
                compareTime = compareTime / (1000);
            } else {
                compareTime = -1;
            }

            int time = new Long(compareTime).intValue();
            return time;
        } catch (Exception e) {
            return -1;
        }

    }

    public static double round(double v, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        BigDecimal b = new BigDecimal(Double.toString(v));
        BigDecimal one = new BigDecimal("1");
        return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    // 澧炲姞鍏ㄨ瀛楃杞崐瑙掑瓧绗﹀拰鍗婅瀛楃杞叏瑙掑瓧绗︽柟娉�
    // Convert.toHalfWidth(String Str)
    // Convert.toFullWidth(String Str)

    /**
     * 鍏ㄨ杞崐瑙�
     *
     * @param Str
     * @return
     */
    public static String toHalfWidth(String Str) {
        char[] c = Str.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }
            if (c[i] > 65280 && c[i] < 65375)
                c[i] = (char) (c[i] - 65248);
        }
        return new String(c);
    }

    /**
     * 鍗婅杞叏瑙�
     *
     * @param Str
     * @return
     */
    public static String toFullWidth(String Str) {
        char[] c = Str.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == (char) 32) {
                c[i] = 12288;
                continue;
            }
            if (c[i] + 65248 > 65280 && c[i] + 65248 < 65375)
                c[i] = (char) (c[i] + 65248);
        }
        return new String(c);
    }

    /**
     * 鍩轰簬姝ｅ垯琛ㄨ揪寮忔浛鎹㈡墍鏈夊叧閿瓧 閽辨捣鍏� 2011-12-03
     *
     * @param operator
     * @return
     */
    public static String toRelationAll(String input) {
        // 浣跨敤姝ｅ垯琛ㄨ揪寮忔灇涓炬墍鏈夊叧绯昏繍绠楃
        String replacement = "";
        if (!input.isEmpty()) {
            Pattern p = Pattern.compile("\\(.{2}\\)");
            Matcher m = p.matcher(input);
            StringBuffer sb = new StringBuffer();
            while (m.find()) {
                String operator = m.group().replaceAll("(\\()|(\\))", "");
                m.appendReplacement(sb, toRelation(operator, m.group()));
            }
            m.appendTail(sb);
            replacement = sb.toString();
        }
        return replacement;
    }

    /**
     * 绠�鍖栨搷浣滅杞崲 閽辨捣鍏� 2011-12-02
     *
     * @param operator
     * @return
     */
    public static String toRelation(String operator, String defaultOpt) {
        String relation = "";
        if ("LK".equalsIgnoreCase(operator)) {
            relation = "LIKE";
        } else if ("NK".equalsIgnoreCase(operator)) {
            relation = "NOT LIKE";
        } else if ("IN".equalsIgnoreCase(operator)) {
            relation = "IN";
        } else if ("NI".equalsIgnoreCase(operator)) {
            relation = "NOT IN";
        } else if ("EQ".equalsIgnoreCase(operator)) {
            relation = "=";
        } else if ("NE".equalsIgnoreCase(operator)) {
            relation = "<>";
        } else if ("GE".equalsIgnoreCase(operator)) {
            relation = ">=";
        } else if ("LE".equalsIgnoreCase(operator)) {
            relation = "<=";
        } else if ("GR".equalsIgnoreCase(operator)) {
            relation = ">";
        } else if ("LS".equalsIgnoreCase(operator)) {
            relation = "<";
        } else if ("BT".equalsIgnoreCase(operator)) {
            relation = "BETWEEN";
        } else if ("NB".equalsIgnoreCase(operator)) {
            relation = "NOT BETWEEN";
        } else if ("NL".equalsIgnoreCase(operator)) {
            relation = "IS NULL";
        } else if ("NN".equalsIgnoreCase(operator)) {
            relation = "IS NOT NULL";
        } else {
            relation = defaultOpt;
        }
        return relation;
    }

    public static String lpad(String str, int num, String pad) {
        String n_str = str;
        if (str == null)
            n_str = " ";
        for (int i = str.length(); i < num; i++) {
            n_str = pad + n_str;
        }
        return n_str;
    }

    public static int daysBetween(Date smdate, Date bdate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        smdate = sdf.parse(sdf.format(smdate));
        bdate = sdf.parse(sdf.format(bdate));
        Calendar cal = Calendar.getInstance();
        cal.setTime(smdate);
        long time1 = cal.getTimeInMillis();
        cal.setTime(bdate);
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / (1000 * 3600 * 24);
        return Integer.parseInt(String.valueOf(between_days));
    }

    public static Object mapToObject(Map<String, Object> map, Class<?> beanClass) throws Exception {
        if (map == null)
            return null;

        Object obj = beanClass.newInstance();

        BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor property : propertyDescriptors) {
            Method setter = property.getWriteMethod();
            if (setter != null) {
                setter.invoke(obj, map.get(property.getName()));
            }
        }

        return obj;
    }

    public static Map<String, Object> objectToMap(Object obj) throws Exception {
        if (obj == null)
            return null;

        Map<String, Object> map = new HashMap<String, Object>();

        BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor property : propertyDescriptors) {
            String key = property.getName();
            if (key.compareToIgnoreCase("class") == 0) {
                continue;
            }
            Method getter = property.getReadMethod();
            Object value = getter != null ? getter.invoke(obj) : null;
            map.put(key, value);
        }

        return map;
    }

    public static Map<String, Object> Obj2Map(Object obj) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        List<Field> fieldList = new ArrayList<>();
        Class tempClass = obj.getClass();
        while (tempClass != null) {//当父类为null的时候说明到达了最上层的父类(Object类).
            fieldList.addAll(Arrays.asList(tempClass .getDeclaredFields()));
            tempClass = tempClass.getSuperclass(); //得到父类,然后赋给自己

        }
        for (Field field : fieldList) {
            field.setAccessible(true);
            map.put(field.getName(), field.get(obj));
        }
        return map;
    }

    public static Object fromJsonToJava(JSONObject json, Class pojo) throws Exception{
        // 首先得到pojo所定义的字段
        Field [] fields = pojo.getDeclaredFields();
        // 根据传入的Class动态生成pojo对象
        Object obj = pojo.newInstance();
        for(Field field: fields){
            // 设置字段可访问（必须，否则报错）
            field.setAccessible(true);
            // 得到字段的属性名
            String name = field.getName();
            // 这一段的作用是如果字段在JSONObject中不存在会抛出异常，如果出异常，则跳过。
            try{
                json.get(name);
            }catch(Exception ex){
                continue;
            }
            if(json.get(name) != null && !"".equals(json.getString(name))){
                // 根据字段的类型将值转化为相应的类型，并设置到生成的对象中。
                if(field.getType().equals(Long.class) || field.getType().equals(long.class)){
                    field.set(obj, Long.parseLong(json.getString(name)));
                }else if(field.getType().equals(String.class)){
                    field.set(obj, json.getString(name));
                } else if(field.getType().equals(Double.class) || field.getType().equals(double.class)){
                    field.set(obj, Double.parseDouble(json.getString(name)));
                } else if(field.getType().equals(Integer.class) || field.getType().equals(int.class)){
                    field.set(obj, Integer.parseInt(json.getString(name)));
                } else if(field.getType().equals(Date.class)){
                    field.set(obj, ToDateTime(json.getString(name)));
                }else{
                    continue;
                }
            }
        }
        return obj;
    }

    public static boolean isJson(String content) {
        if(StringUtils.isEmpty(content)){
            return false;
        }

        if(!isJSONObject(content) && !isJSONArray(content)){ //不是json格式
            return false;
        }
        return true;
    }

    public static boolean isJSONObject(String content) {
        if(StringUtils.isEmpty(content)){
            return false;
        }
        try {
            JSONObject.parseObject(content);
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    public static boolean isJSONArray(String content) {
        if(StringUtils.isEmpty(content)){
            return false;
        }
        try {
            JSONArray.parseObject(content);
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    /**
     * 获取精确到秒的时间戳
     * @return
     */
    public static Long getSecondTimestamp(Date date){
        if (null == date) {
            return 0l;
        }
        String timestamp = String.valueOf(date.getTime());
        int length = timestamp.length();
        if (length > 3) {
            return Long.valueOf(timestamp.substring(0,length-3));
        } else {
            return 0l;
        }
    }

    /**
     * 获取精确到秒的时间戳
     * @return
     */
    public static Long getSecondTimestamp(){
        return getSecondTimestamp(new Date());
    }

    /*
     * @Author wjw
     * @Description //TODO 取日期
     * @Date 11:12 2024/6/14
     * @Param []
     * @return java.util.Date
     **/
    public static Date DateTimeToDate(Date sDate){
        if (sDate == null){
            return null;
        }
        Date result = StringToDateTime(dateFormat(sDate,"yyyy-MM-dd"), "yyyy-MM-dd");
        return result;
    }

    public static Date TimestampToDate(Timestamp timestamp){
        if (timestamp == null){
            return null;
        }
        Date sDate = new Date(timestamp.getTime());
        return DateTimeToDate(sDate);
    }
}
