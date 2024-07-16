package com.jiahe.px.common.core.utils;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;


public final class ByteUtils
{
  public static final short UNSIGNED_MAX_VALUE = 255;

  public static short toUnsigned(byte b)
  {
    return (short)((b < 0) ? 256 + b : b);
  }

  /**
   * 
   * @param b
   * @return
   */
  public static String toHexAscii(byte b) {
    StringWriter sw = new StringWriter(2);
    addHexAscii(b, sw);
    return sw.toString();
  }

  public static String toHexAscii(byte[] bytes)
  {
    int len = bytes.length;
    StringWriter sw = new StringWriter(len * 2);
    for (int i = 0; i < len; ++i)
      addHexAscii(bytes[i], sw);
    return sw.toString();
  }

  public static byte[] fromHexAscii(String s) throws NumberFormatException
  {
    try
    {
      int len = s.length();
      if (len % 2 != 0) {
        throw new NumberFormatException("Hex ascii must be exactly two digits per byte.");
      }
      int out_len = len / 2;
      byte[] out = new byte[out_len];
      int i = 0;
      StringReader sr = new StringReader(s);
      while (i < out_len)
      {
        int val = 16 * fromHexDigit(sr.read()) + fromHexDigit(sr.read());
        out[(i++)] = (byte)val;
      }
      return out;
    }
    catch (IOException e) {
      throw new InternalError("IOException reading from StringReader?!?!");
    }
  }

  static void addHexAscii(byte b, StringWriter sw) {
    short ub = toUnsigned(b);
    int h1 = ub / 16;
    int h2 = ub % 16;
    sw.write(toHexDigit(h1));
    sw.write(toHexDigit(h2));
  }

  private static int fromHexDigit(int c) throws NumberFormatException
  {
    if ((c >= 48) && (c < 58))
      return (c - 48);
    if ((c >= 65) && (c < 71))
      return (c - 55);
    if ((c >= 97) && (c < 103)) {
      return (c - 87);
    }
    throw new NumberFormatException((39 + c) + "' is not a valid hexadecimal digit.");
  }

  private static char toHexDigit(int h)
  {
    char out;
    if (h <= 9) out = (char)(h + 48);
    else out = (char)(h + 55);

    return out;
  }
  
  private static final char[] hex = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

  public static String logBytes(byte[] pkt)
  {
    int len = pkt.length;
    int iBufSize=len/16+1;

    StringBuffer line = new StringBuffer(80*iBufSize);

    for (int i = 0; i < len; i += 16)
    {
      int val;

      String sHex= Integer.toHexString( i );
      sHex = sHex.toUpperCase();
      sHex=StringHelper.PadLeft(sHex, 8, '0');
      
      line.append(sHex);
      line.append(':').append(' ');

      int j = 0;
      do
      {
        val = pkt[(i + j)] & 0xFF;

        line.append(hex[(val >> 4)]);
        line.append(hex[(val & 0xF)]);
        line.append(' ');

        ++j; 
        if (j==8) line.append(' ');
        if (j >= 16) break;  
      } while (i + j < len);

      for (; j < 16; ++j) {
        line.append("   ");
      }

      line.append('|');

      for (j = 0; (j < 16) && (i + j < len); ++j) {
        val = pkt[(i + j)] & 0xFF;

        if ((val > 31) && (val < 127))
          line.append((char)val);
        else {
          line.append(' ');
        }
        if (j==8) line.append(' ');
      }

      line.append('|');
      line.append('\r');
      line.append('\n');
    }
    line.append('\r');
    line.append('\n');
    return line.toString();
    
  }
}
