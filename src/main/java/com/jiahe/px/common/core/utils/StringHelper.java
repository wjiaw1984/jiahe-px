package com.jiahe.px.common.core.utils;

import java.io.ByteArrayOutputStream;
import java.rmi.server.ObjID;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class StringHelper {
	
	/**
	 * 鏄惁绌哄瓧绗︿覆
	 * @param string
	 * @return
	 */
	public static boolean isNullOrEmpty(String string)
	{
		return string == null || string.equals("");
	}

	/**
	 * 杩炴帴瀛楃涓�
	 * @param separator
	 * @param stringarray
	 * @return
	 */
	public static String join(String separator, String[] stringarray)
	{
		if (stringarray == null)
			return null;
		else
			return join(separator, stringarray, 0, stringarray.length);
	}


	/**
	 * 杩炴帴瀛楃涓�
	 * @param separator
	 * @param stringarray
	 * @param startindex
	 * @param count
	 * @return
	 */
	public static String join(String separator, String[] stringarray, int startindex, int count)
	{
		String result = "";

		if (stringarray == null)
			return null;

		for (int index = startindex; index < stringarray.length && index - startindex < count; index++)
		{
			if (separator != null && index > startindex)
				result += separator;

			if (stringarray[index] != null)
				result += stringarray[index];
		}

		return result;
	}

	/**
	 * 鍒犻櫎鍚庨潰鐨勭┖鏍�
	 * @param string
	 * @return
	 */
	public static String trimEnd(String string)
	{
		if (string == null)
			return string;

		int lengthToKeep = string.length();
		for (int index = string.length() - 1; index >= 0; index--)
		{
			boolean removeChar = false;
			if (Character.isWhitespace(string.charAt(index)))
			{
					lengthToKeep = index;
					removeChar = true;
			}
			if ( ! removeChar)
				break;
		}
		return string.substring(0, lengthToKeep);
	}

	/**
	 * 鍒犻櫎鍚庨潰鎸囧畾鐨勫瓧绗�
	 * @param string
	 * @param charsToTrim
	 * @return
	 */
	public static String trimEnd(String string, Character... charsToTrim)
	{
		if (string == null || charsToTrim == null)
			return string;

		int lengthToKeep = string.length();
		for (int index = string.length() - 1; index >= 0; index--)
		{
			boolean removeChar = false;
			if (charsToTrim.length == 0)
			{
				if (Character.isWhitespace(string.charAt(index)))
				{
					lengthToKeep = index;
					removeChar = true;
				}
			}
			else
			{
				for (int trimCharIndex = 0; trimCharIndex < charsToTrim.length; trimCharIndex++)
				{
					if (string.charAt(index) == charsToTrim[trimCharIndex])
					{
						lengthToKeep = index;
						removeChar = true;
						break;
					}
				}
			}
			if ( ! removeChar)
				break;
		}
		return string.substring(0, lengthToKeep);
	}

	/**
	 * 鍒犻櫎鍓嶅悗绌烘牸
	 * @param string
	 * @return
	 */
	public static String trim(String string)
	{
		String str=trimStart(string);
		str=trimEnd(str);
		return str;
	}
	
	/**
	 * 鍒犻櫎鍓嶇┖鏍�
	 * @param string
	 * @return
	 */
	public static String trimStart(String string)
	{
		if (string == null)
			return string;

		int startingIndex = 0;
		for (int index = 0; index < string.length(); index++)
		{
			boolean removeChar = false;
			if (Character.isWhitespace(string.charAt(index)))
			{
					startingIndex = index + 1;
					removeChar = true;
			}
			
			if ( ! removeChar)
				break;
		}
		return string.substring(startingIndex);
	}
	
	/**
	 * 鍒犻櫎鍓嶉潰鎸囧畾瀛楃
	 * @param string
	 * @param charsToTrim
	 * @return
	 */
	public static String trimStart(String string, Character... charsToTrim)
	{
		if (string == null || charsToTrim == null)
			return string;

		int startingIndex = 0;
		for (int index = 0; index < string.length(); index++)
		{
			boolean removeChar = false;
			if (charsToTrim.length == 0)
			{
				if (Character.isWhitespace(string.charAt(index)))
				{
					startingIndex = index + 1;
					removeChar = true;
				}
			}
			else
			{
				for (int trimCharIndex = 0; trimCharIndex < charsToTrim.length; trimCharIndex++)
				{
					if (string.charAt(index) == charsToTrim[trimCharIndex])
					{
						startingIndex = index + 1;
						removeChar = true;
						break;
					}
				}
			}
			if ( ! removeChar)
				break;
		}
		return string.substring(startingIndex);
	}

	/**
	 * 鍒犻櫎鍓嶅悗鎸囧畾瀛楃
	 * @param string
	 * @param charsToTrim
	 * @return
	 */
	public static String trim(String string, Character... charsToTrim)
	{
		return trimEnd(trimStart(string, charsToTrim), charsToTrim);
	}

	public static boolean stringsEqual(String s1, String s2)
	{
		if (s1 == null && s2 == null)
			return true;
		else
			return s1 != null && s1.equals(s2);
	}
	
	  public static String Ltrim(String str){
		  if(str == null || str.trim().equals("")) return "";
		  
		  String retStr = str;
		  Pattern p = Pattern.compile("(^\\s*)");
		  Matcher m = p.matcher(str);
		  String lBlank = "";
	  	  if(m.find()){
	  		lBlank = m.group(0);
	  	 }   	
	  	 retStr = retStr.substring(lBlank.length(),retStr.length());
	  	 return retStr;
	  }
	  
	  public static String Rtrim(String str){
	      if(str == null || str.trim().equals("")) return "";
		  
		  String retStr = str;
		  Pattern p = Pattern.compile("(\\s*$)");
		  Matcher m = p.matcher(str);
		  String rBlank = "";
	  	  if(m.find()){
	  		 rBlank = m.group(0);
//	  		 System.out.println(rBlank.length());
	  	  }   
	  	  
	  	  retStr = retStr.substring(0,retStr.length()-rBlank.length());
	  	  return retStr;
	  }
	  /*
	   * 鏍煎紡鍙戝瓧绗︿覆,绀轰緥锛歴tr= abc ({0},'{1}','{2}','{3}'),param={"111","222","333","444"}
	   * 
	   */
	  public static String Format(String str,String[] param){
		  StringBuffer strRet = new StringBuffer("");
		  if(str == null || str.equals("")) return "";
		  if(param == null || param.length == 0) return str;
		  
		  String[] arrStr = str.split("\\{");
		  int index = 0;
		  String tmpStr = "";
		  int tmpInt = 0;
		  if(arrStr != null && arrStr.length > 0){		  
			  for(int i = 0; i < arrStr.length; i++){			  
				  index = arrStr[i].indexOf("}");
				  //System.out.println("str="+str+";index="+index);
				  if(index < 0){
					  strRet.append(arrStr[i]);
					  continue;
				  }
				  
				  tmpStr = arrStr[i].substring(0,index).trim();
				  tmpInt = stringToInt(tmpStr,-1);
				  System.out.println("tmpStr="+tmpStr);
				  if(tmpInt > -1 && tmpInt < param.length){
					  System.out.println(param[tmpInt]);
					  strRet.append(""+param[tmpInt]);
					  strRet.append(arrStr[i].substring(index+1, arrStr[i].length()));
				  }else{
					  strRet.append("{");
					  strRet.append(arrStr[i]);
				  }
			  }
		  }
		  return strRet.toString();
	  }
	  
	  /*
	   * 鏍煎紡鍙戝瓧绗︿覆,绀轰緥锛歴tr= abc ({0},'{1}','{2}','{3}'),param={"111","222","333","444"}
	   * 
	   */
	  public static String Format(String str,ArrayList<String> paramList){
		  String[] param=(String[])paramList.toArray();
		  return Format(str,param);
		  
	  }
	  
	  /**
	   * 鍒囧垎瀛楃涓�
	   * @param in
	   * @param dism
	   * @return
	   */
	  public static String[] split(String in, String dism) {
			String o[] = { "" };
			if (in == null) {
				return o;
			}
			if (in.equals("")) {
				return o;
			}

			if (dism == null) {
				o[0] = in;
				return o;
			}
			if (dism.equals("")) {
				o[0] = in;
				return o;
			}

			Stack<String> stk = new Stack<String>();

			int i_len = dism.length();
			int i_1 = 0;
			String temp_str = in;
			String str_1;

			while (true) {
				i_1 = temp_str.indexOf(dism);

				if (i_1 == -1) {
					stk.push(temp_str);
					break;
				}
				str_1 = temp_str.substring(0, i_1);
				temp_str = temp_str.substring(i_1 + i_len);
				stk.push(str_1);
			}

			int size = stk.size();
			String out[] = new String[size];
			for (int i = 0; i < size; i++) {
				out[size - i - 1] = stk.pop().toString();
			}
			return out;

		}
	  /**
		 * 鏇挎崲瀛楃涓�
		 * 
		 * @param str 婧愬瓧绗︿覆
		 * @param find 鏌ユ壘鐨勫瓧绗︿覆
		 * @param replacewith 鏇挎崲鐨勫瓧绗︿覆
		 * @return 鏇挎崲鍚庣殑瀛楃涓�
		 */
		public static String replace(String str, String find, String replacewith) {
			if (str == null) {
				return str;
			}
			if (find == null) {
				return str;
			}
			if (replacewith == null) {
				replacewith = "";
			}
			int i_len = find.length();
			int i_1 = 0;
			String temp_str = str;
			String str_1, str_2;
			int i = 0;
			int len = replacewith.length();

			while (true) {
				i_1 = temp_str.indexOf(find, i);
				if (i_1 == -1) {
					break;
				}
				str_1 = temp_str.substring(0, i_1);
				str_2 = temp_str.substring(i_1 + i_len);
				temp_str = str_1 + replacewith + str_2;
				i = i_1 + len;
			}
			return temp_str;
		}
	  
		/**
		 * 鐢熸垚闅忔満ID
		 * @return
		 */
	  public static String generateUID() {
	    int id = (new Integer( (new SimpleDateFormat("Mddhhmmss")).format(Calendar.
	        getInstance().getTime()))).intValue();
	    id += (new ObjID()).toString().hashCode();
	    return String.valueOf(id);
	  }
	 

	  /**
	   * 鍒囧垎瀛楃涓�
	   * @param source String
	   * @param delim String
	   * @return ArrayList
	   */

	  public static ArrayList<String> breakStringToArrayList(String source, String delim) {
	    StringTokenizer sToken = new StringTokenizer(source, delim);
	    ArrayList<String> arr = new ArrayList<String>();
	    while (sToken.hasMoreTokens()) {
	      arr.add(sToken.nextToken());
	    }
	    return arr;
	  }

	  /**
	   * 鐢熸垚鏂囦欢鍚�
	   * @return
	   */
	  public static String generateFileName() {
	    int id = (new Integer( (new SimpleDateFormat("Mddhhmmss")).format(Calendar.
	        getInstance().getTime()))).intValue();
	    return String.valueOf(id);
	  }

	  public static String objectToString(Object sourceObject) {
	    String returnString = null;
	    if (sourceObject == null) {
	      return "";
	    }
	    if (sourceObject instanceof String) {
	      return (String) sourceObject;
	    }
	    if (sourceObject instanceof Long) {
	      return sourceObject.toString();
	    }
	    if (sourceObject instanceof Integer) {
	      return sourceObject.toString();
	    }
	    if (sourceObject instanceof Float) {
	      return sourceObject.toString();
	    }
	    if (sourceObject instanceof Double) {
	      return sourceObject.toString();
	    }
	    if (sourceObject instanceof Calendar) {
	      return fillString(sourceObject,"");
	    }
	    if (sourceObject instanceof java.math.BigDecimal) {
	      return sourceObject.toString();
	    }

	    return returnString;
	  }

	  public static Calendar stringToCalendar(String sDate) {
	    Calendar cal = stringToCalendar(sDate, "yyyy-MM-dd");
	    return cal;
	  }

	  /**
	   *
	   * @param monthCode
	   * @return
	   */
	  public static String getCurrentDecYMonth() {
	    String monthCode = new SimpleDateFormat("yyyyMMdd").format(Calendar.
	        getInstance().getTime());

	    int year = Integer.parseInt(monthCode.substring(0, 4));
	    int month = Integer.parseInt(monthCode.substring(4, 6)) - 1;

	    if (month == 0) {
	      month = 12;
	      year = year - 1;
	    }
	    if (month > 9) {
	      return Integer.toString(year) + Integer.toString(month);
	    }
	    else {
	      return Integer.toString(year) + "0" + Integer.toString(month);
	    }
	  }

	  /**
	   *
	   * @param monthCode
	   * @return
	   */
	  public static String getCurrentYMonth() {
	    String monthCode = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.
	        getInstance().getTime());
	    return monthCode;
	  }

	  /**
	   *
	   * @param monthCode
	   * @return
	   */
	  public static String getAddMonth(String monthCode) {
	    int year = Integer.parseInt(monthCode.substring(0, 4));
	    int month = Integer.parseInt(monthCode.substring(4, 6)) + 1;
	    if (month == 13) {
	      month = 1;
	      year = year + 1;
	    }
	    if (month > 9) {
	      return Integer.toString(year) + Integer.toString(month);
	    }
	    else {
	      return Integer.toString(year) + "0" + Integer.toString(month);
	    }
	  }

	    public static String getDayAdd(String sDate,int oper){
	      if(sDate == null || sDate.equals(""))  return "";
	        try{
	          Calendar cal= stringToCalendar(sDate);
	          cal.add(Calendar.DAY_OF_MONTH, oper);
	          String tempDay=new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
	          return tempDay;
	        }catch(Exception exc){
	          return "";
	        }
	    }


	    public static String getMonthAdd(String sDate,int oper){
	      if(sDate == null || sDate.equals(""))  return "";
	        try{
	          Calendar cal= stringToCalendar(sDate);
	          cal.add(Calendar.MONTH, oper);
	          String tempDay=new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
	          return tempDay;
	        }catch(Exception exc){
	          return "";
	        }
	    }

	    public static String getYearAdd(String sDate,int oper){
	      if(sDate == null || sDate.equals(""))  return "";
	        try{
	          Calendar cal= stringToCalendar(sDate);
	          cal.add(Calendar.YEAR, oper);
	          String tempDay=new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
	          return tempDay;
	        }catch(Exception exc){
	          return "";
	        }
	    }
	  public static String getCurrentYear() {
	    SimpleDateFormat dF = new SimpleDateFormat("yyyy");
	    return dF.format(new Date());
	  }
	  public static String getStrTimeYear(String strtime,String format){
	      if(strtime==null || strtime.equals("")) return "";
	      int firstFormatIndex=strtime.indexOf(format);
	      if(firstFormatIndex<1) return "";
	      return strtime.substring(0,firstFormatIndex);
	  }
	  public static String getStrTimeMonth(String strtime,String format){
	     if(strtime==null || strtime.equals("")) return "";
	     int firstFormatIndex=strtime.indexOf(format);
	     if(firstFormatIndex<1) return "";
	     int secondFormatIndex=strtime.indexOf(format,firstFormatIndex+1);
	     if(secondFormatIndex<1) return "";
	     return strtime.substring(firstFormatIndex+1,secondFormatIndex);
	  }
	  public static String getStrTimeDate(String strtime,String format){
	       if(strtime==null || strtime.equals("")) return "";
	       int blankIndex=strtime.indexOf(" ");
	       if(blankIndex>0){
	         strtime=strtime.substring(0,blankIndex);
	         int lastFormatIndex=strtime.lastIndexOf(format);
	         return strtime.substring(lastFormatIndex+1,blankIndex);
	       }else{
	         int lastFormatIndex=strtime.lastIndexOf(format);
	         return strtime.substring(lastFormatIndex+1,strtime.length());
	       }
	  }
	  public static String getStrTimeHours(String strtime,String format){
	     int blankIndex=strtime.indexOf(" ");
	     int MHindex=strtime.indexOf(":");
	     if(blankIndex<0 || MHindex<0){
	       return "";
	     }else{
	       strtime=strtime.substring(blankIndex).trim();
	       int firstMHindex=strtime.indexOf(":");
	       return strtime.substring(0,firstMHindex);
	     }
	  }
	  public static String getStrTimeMinutes(String strtime,String format){
	     int blankIndex=strtime.indexOf(" ");
	     int MHindex=strtime.indexOf(":");
	     if(blankIndex<0 || MHindex<0){
	       return "";
	     }else{
	       strtime=strtime.substring(blankIndex).trim();
	       int firstMHindex=strtime.indexOf(":");
	       int lastMHindex=strtime.lastIndexOf(":");
	       return strtime.substring(firstMHindex+1,lastMHindex);
	     }
	  }
	  public static String getStrTimeSeconds(String strtime,String format){
	     int blankIndex=strtime.indexOf(" ");
	     int MHindex=strtime.indexOf(":");
	     if(blankIndex<0 || MHindex<0){
	       return "";
	     }else{
	       strtime=strtime.substring(blankIndex).trim();
	       @SuppressWarnings("unused")
		int firstMHindex=strtime.indexOf(":");
	       int lastMHindex=strtime.lastIndexOf(":");
	       int dianIndex=strtime.indexOf(".");
	       if(dianIndex>0)
	         return strtime.substring(lastMHindex+1,dianIndex);
	       else return strtime.substring(lastMHindex+1,strtime.length());
	     }
	  }
	  public static String calendarTostring(Calendar cal, String sDateTimeFormat) {
	    String returnValue = "-";
	    if (cal != null) {
	      try {
	        returnValue = (new SimpleDateFormat(sDateTimeFormat)).format(cal.
	            getTime());
	      }
	      catch (Exception exc) {

	      }
	    }
	    return returnValue;
	  }

	  public static Calendar stringToCalendar(String sDate, String sDateTimeFormat) {
	    Calendar cal = null;
	    if (sDate != null && !sDate.equals("")) {
	      try {
	        cal = Calendar.getInstance();
	        cal.setTime( (new SimpleDateFormat(sDateTimeFormat)).parse(sDate));
	      }
	      catch (Exception exc) {
	        cal = null;
	      }
	    }
	    return cal;
	  }

	  public static Float stringToFloat(String sFloat) {
	    Float f = null;
	    if (sFloat != null && !sFloat.equals("")) {
	      try {
	        f = new Float(sFloat);
	      }
	      catch (NumberFormatException numberformatexception) {}
	    }
	    return f;
	  }

	  public static Double stringToDouble(String sDouble) {
	    Double d = null;
	    if (sDouble != null && !sDouble.equals("")) {

	      try {
	        d = new Double(sDouble);
	      }
	      catch (NumberFormatException numberformatexception) {}
	    }
	    return d;
	  }

	  public static double switTodouble(Object sourceObject) {
	    double d = 0.0;
	    if (sourceObject == null) {
	      return d;
	    }
	    if (sourceObject instanceof Double) {
	      if (!sourceObject.equals("") && !sourceObject.equals("-")) {
	        try {
	          return ( (Double) sourceObject).doubleValue();
	        }
	        catch (NumberFormatException numberformatexception) {}
	      }

	    }

	    return d;
	  }

	  public static Integer stringToInteger(String sInteger) {
	    Integer i = null;
	    if (sInteger != null && !sInteger.equals("")) {
	      try {
	        i = new Integer(sInteger);
	      }
	      catch (NumberFormatException numberformatexception) {}
	    }
	    return i;
	  }

	  public static int stringToInt(String str,int defaultValue){
	    if(str==null || str.equals("")) return defaultValue;
	    int returnValue=0;
	    try{
	       returnValue = Integer.parseInt(str);
	    }catch(NumberFormatException numberformatexception){
	      return defaultValue;
	    }
	    return returnValue;
	  }
	  public static long stringToLong(String str){
	    if(str==null || str.equals("")) return 0L;
	    long returnValue=0L;
	    try{
	       returnValue = new Long(str).longValue();
	    }catch(NumberFormatException numberformatexception){
	      return 0L;
	    }
	    return returnValue;
	  }

	  public static Properties String2Propties(String source) {
	    StringTokenizer sToken = new StringTokenizer(source, ",");
	    String temp = null;
	    StringTokenizer tmpToken = null;
	    Properties prop = new Properties();
	    String key = null;
	    String value = null;
	    while (sToken.hasMoreTokens()) {
	      temp = sToken.nextToken();
	      tmpToken = new StringTokenizer(temp, "=");
	      if (tmpToken.hasMoreTokens()) {
	        key = tmpToken.nextToken();
	        if (tmpToken.hasMoreTokens()) {
	          value = tmpToken.nextToken();
	        }
	        else {
	          value = "";
	        }
	        prop.setProperty(key, value);
	      }
	    }
	    return prop;
	  }
	  public static String iso2gb(String str,String encodetype) {
	      try {
	          if(str == null) {
	             return "";
	          }
	          byte[] bs = str.getBytes("iso-8859-1");
	          String str_temp = "";
	          str_temp = new String(bs, encodetype);
	          return str_temp;
	      } catch (Exception e) {
	          e.printStackTrace();
	      }
	          return str;
	}

	  public static String html2text(String src) {
	    StringBuffer strbuf = new StringBuffer();
	    if (src == null) {
	      return new String("");
	    }
	    char ch;
	    int i;
	    for (i = 0; i < src.length(); i++) {
	      ch = src.charAt(i);
	      switch (ch) {
	        case '<':
	          strbuf.append("&lt;");
	          break;
	        case '>':
	          strbuf.append("&gt;");
	          break;
	        case '&':
	          strbuf.append("&amp;");
	          break;
	        case '\"':
	          strbuf.append("&quot;");
	          break;
	        default:
	          strbuf.append(ch);
	          break;
	      }
	    }

	    return strbuf.toString();
	  }

	  public static String fillString(Object sourceObject,String dateFormat) {
	    String	returnValue=Convert.ToString(sourceObject);
	    
	    return returnValue;
	  }

	  public static boolean isDigit(String sourceObject) {
	    if (sourceObject == null) {
	      return false;
	    }
	    else {
	      for (int i = 0; i < sourceObject.length() - 1; i++) {
	        String temp = sourceObject.substring(i, i + 1);
	        if (temp.indexOf("0") == 0 || temp.indexOf("1") == 0 ||
	            temp.indexOf("2") == 0 || temp.indexOf("3") == 0 ||
	            temp.indexOf("4") == 0 || temp.indexOf("5") == 0 ||
	            temp.indexOf("6") == 0 || temp.indexOf("7") == 0 ||
	            temp.indexOf("8") == 0 || temp.indexOf("9") == 0) {
	          continue;
	        }
	        else {
	          return false;
	        }
	      }
	      return true;
	    }
	  }

	  
	  public static String gb2iso(String str) {
	    try {
	      if (str == null) {
	        return null;
	      }
	      byte[] bs = str.getBytes("gb2312");
	       
	      String str_temp = new String(bs, "iso-8859-1");
	      return str_temp;
	    }
	    catch (Exception e) {
	      e.printStackTrace();
	    }
	    return str;
	  }

	  public static String iso2gb(String str) {
	    try {
	      if (str == null) {
	        return null;
	      }
	      byte[] bs = str.getBytes("iso-8859-1");
	      String str_temp = "";
	       
	      str_temp = new String(bs, "gb2312");
	       
	      return str_temp;
	    }
	    catch (Exception e) {
	      e.printStackTrace();
	    }
	    return str;
	  }

	  public static String big2iso(String str) {
	    try {
	      if (str == null) {
	        return null;
	      }
	      byte[] bs = str.getBytes("big5");
	      String str_temp = new String(bs, "iso-8859-1");
	      return str_temp;
	    }
	    catch (Exception e) {
	      e.printStackTrace();
	    }
	    return str;
	  }

	  public static String iso2big(String str) {
	    try {
	      if (str == null) {
	        return null;
	      }
	      byte[] bs = str.getBytes("iso-8859-1");
	      String str_temp = new String(bs, "big5");
	      return str_temp;
	    }
	    catch (Exception e) {
	      e.printStackTrace();
	    }
	    return str;
	  }
	  
	  public static String insert(String oldStr,int index,String insertStr){
		  if(oldStr == null) return "";
		  if(oldStr.equals("") && index == 0) return insertStr;
		  if(index > oldStr.length()) return oldStr;
		  
		  StringBuffer retBuf = new StringBuffer("");
		  retBuf.append(oldStr.substring(0,index));
		  retBuf.append(insertStr);
		  retBuf.append(oldStr.substring(index,oldStr.length()));
		  
		  return retBuf.toString();
	  }
	  public static Object[] insertArr(Object[] orign,Object[] newArr,int index){
		  if(orign == null){
			  return newArr;
		  }else{
			  if(orign.length < index){
				  return orign;
			  }
			  if(newArr == null){
				  return orign;
			  }
			  int len1 = orign.length;
			  int len2 = newArr.length;
			  Object[] retObjArr = new Object[len1+len2];
			  for(int i = 0; i < retObjArr.length; i++){
				  if(i<index){
					  retObjArr[i] = orign[i];
					  continue;
				  }
				  if(i >= index && i < index+len2){
					  retObjArr[i] = newArr[i-index];
					  continue;
				  }
			      retObjArr[i] = orign[i-len2];
			  }
			  return retObjArr;
		  }
	  }
	  public static String PadLeft(String str,int width,char addChar){
		  int lenStr = 0;
		  if(str == null || str.equals("")){
			  lenStr = 0;
			  str = "";
		  }else{
			  lenStr = str.length();
		  }
		  StringBuffer retBuf = new StringBuffer("");
		  for(int i = 0; i < width-lenStr; i++){
			  retBuf.append(addChar+"");
		  }
		  retBuf.append(str);
		  
		  return retBuf.toString();
	  }

	  public static String PadRight(String str,int width,char addChar){
		  int lenStr = 0;
		  if(str == null || str.equals("")){
			  lenStr = 0;
			  str = "";
		  }else{
			  lenStr = str.length();
		  }
		  StringBuffer retBuf = new StringBuffer(str);
		  for(int i = 0; i < width-lenStr; i++){
			  retBuf.append(addChar+"");
		  }
		  
		  return retBuf.toString();
	  }

	  public static String showArr(Object[] arr){
		  if(arr == null){
			  return "";
		  }
		  StringBuffer retStr = new StringBuffer("");
		  
		  for(int i = 0; i < arr.length; i++){
			  if(arr[i] == null){
				 retStr.append("NULL-Object");
				 retStr.append(",");
			  }else{
				 retStr.append(fillString(arr[i],""));
				 retStr.append(",");
			  }
		  }
		  return retStr.toString();
	  }
	  
	  public static String toHexString(String s){       
		  String   str= " ";       
		  for(int i = 0; i <s.length(); i++){       
			  int ch = (int)s.charAt(i);       
			  String s4 = Integer.toHexString(ch);       
			  str = str + s4;   
		  }       
		  return str;       
	  }   

	   

	  public static String toStringHex(String s){ 
	      byte[] baKeyword = new byte[s.length()/2]; 
	      for(int i = 0; i < baKeyword.length; i++){ 
	        try{ 
	            baKeyword[i] = (byte)(0xff & Integer.parseInt(s.substring(i*2,i*2+2),16)); 
	        }catch(Exception e){ 
	          e.printStackTrace(); 
	        } 
	      } 
	      
	      try{ 
	          s = new String(baKeyword,"utf-8 ");//UTF-16le:Not 
	      }catch(Exception e1){ 
	          e1.printStackTrace(); 
	      }   
	      return s; 
	  }
	  
	  private static String hexString= "0123456789ABCDEF "; 

	  public static String encode(String str){ 
	      byte[] bytes=str.getBytes(); 
	      StringBuilder sb=new StringBuilder(bytes.length*2); 
	      for(int i=0;i < bytes.length; i++){ 
			  sb.append(hexString.charAt((bytes[i]&0xf0)>> 4)); 
			  sb.append(hexString.charAt((bytes[i]&0x0f)>> 0)); 
	      } 
	      return sb.toString(); 
	  } 

	  public static String decode(String bytes) { 
		  ByteArrayOutputStream baos=new ByteArrayOutputStream(bytes.length()/2); 
	      for(int i=0;i < bytes.length(); i+=2){
	          baos.write((hexString.indexOf(bytes.charAt(i)) <<4  | hexString.indexOf(bytes.charAt(i+1)))); 
	      }
	      return new String(baos.toByteArray()); 
	  }
	  
	  public static String jdkPath(){
		  return "";
	  }
	  
	  public static String fromUnicode(String theString){   
	      char aChar;   
	      int len = theString.length();
	      StringBuffer outBuffer = new StringBuffer(len);   

	      for(int x = 0; x < len;){   
	          aChar = theString.charAt(x++);   
	          if(aChar == '\\'){   
	             aChar = theString.charAt(x++);   
	             if(aChar == 'u'){   
	                //Read   the   xxxx   
	                int value=0;   
	                for(int i = 0; i < 4; i++){   
	                    aChar = theString.charAt(x++);   
	                    switch(aChar){   
	                        case '0':   
	                        case '1':   
	                        case '2':   
	                        case '3':   
	                        case '4':  
	                        case '5':   
	                        case '6':   
	                        case '7':   
	                        case '8':   
	                        case '9':   
	                                 value = (value << 4) + aChar - '0';
	                                 break;   
	                        case 'a': 
	                        case 'b':   
	                        case 'c':   
	                        case 'd':   
	                        case 'e':  
	                        case 'f':   
	                                 value = (value << 4) + 10 + aChar - 'a';   
	                                 break;   
	                        case 'A':  
	                        case 'B':   
	                        case 'C':   
	                        case 'D':   
	                        case 'E':   
	                        case 'F': 
	                                 value = (value << 4) + 10 + aChar - 'A';   
	                                 break;   
	                        default:   
	                                 throw new IllegalArgumentException(   
	                                       "Malformed   \\uxxxx   encoding.");   
	                    }   
	                }   
	                outBuffer.append((char)value);   
	            }else{   
	                if(aChar == 't'){
	                   aChar = '\t';   
	                }else if(aChar == 'r'){
	                   aChar = '\r';   
	            }else if(aChar == 'n'){
	                aChar = '\n';   
	            }else if(aChar == 'f'){
	                aChar = '\f';   
	            }
	            outBuffer.append(aChar);   
	         }   
	      }else{   
	            outBuffer.append(aChar);  
	      }
	    }   
	    return outBuffer.toString();   
	  }
	  
	  public static String toUnicode(String theString, boolean escapeSpace){
	      int len = theString.length();
	      int bufLen = len * 2;
	      if (bufLen < 0){
	          bufLen = Integer.MAX_VALUE;
	      }
	      StringBuffer outBuffer = new StringBuffer(bufLen);
	      
	      for(int x=0; x<len; x++){
	          char aChar = theString.charAt(x);
	          // Handle common case first, selecting largest block that
	          // avoids the specials below
	          if ((aChar > 61) && (aChar < 127)){
	              if(aChar == '\\'){
	                  outBuffer.append('\\'); outBuffer.append('\\');
	                  continue;
	              }
	              outBuffer.append(aChar);
	              continue;
	          }
	          switch(aChar){
	              case '\t':
	                  if(x == 0 || escapeSpace){
	                      outBuffer.append('\\');
	                  }
	                  outBuffer.append(' ');
	                  break;
	              default:
	                  if ((aChar < 0x0020) || (aChar > 0x007e)){
	                      outBuffer.append('\\');
	                      outBuffer.append('u');
	                      outBuffer.append(toHex((aChar >> 12) & 0xF));
	                      outBuffer.append(toHex((aChar >>  8) & 0xF));
	                      outBuffer.append(toHex((aChar >>  4) & 0xF));
	                      outBuffer.append(toHex( aChar        & 0xF));
	                  }else{
	                      outBuffer.append(aChar);
	                  }
	          }
	      }
	      return outBuffer.toString();
	  }
	  private static final char[] hexDigit = {
	      '0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'
	  };
	  
	  private static char toHex(int nibble) {
	      return hexDigit[(nibble & 0xF)];
	  }

		/** 
		 瀛楃涓叉浛鎹�(涓嶅尯鍒嗗ぇ灏忓啓)
		 
		 @param sourcestr
		 @param oldstr
		 @param newstr
		 @return 
		*/
		public static String Replace(String sourcestr, String oldstr, String newstr)
		{
			if (oldstr.equals(""))
			{
				return sourcestr;
			}
			String newvalue = "";
			int i = 0;
			while (i < sourcestr.length())
			{
				if ((i+oldstr.length())<=sourcestr.length())
				{
					String tmpstr = sourcestr.substring(i, i + oldstr.length()).toLowerCase();
					if (tmpstr.equals(oldstr.toLowerCase()))
					{
						newvalue = newvalue + newstr;
						i = i+ oldstr.length();
					}
					else
					{
						newvalue = newvalue + sourcestr.substring(i, i + 1);
						i = i+ 1;
					}
				}
				else
				{
					newvalue = newvalue + sourcestr.substring(i, i + 1);
					i = i+ 1;
				}
			}
			return newvalue;
		}
		
		/**
		 * 鏇挎崲瀛楃涓�
		 * @param strExpression
		 * @param strSearch
		 * @param strReplace
		 * @param intMode 0=鍖哄垎澶у皬鍐�,1=涓嶅尯鍒嗗ぇ灏忓啓
		 * @return
		 */
		public static String Replace(String strExpression, String strSearch, String strReplace, int intMode)
		{
			String strReturn;
			int intPosition;
			String strTemp;

			if (strSearch.equals(""))
			{
				return strExpression;
			}

			if(intMode == 1)
			{
				strReturn = Replace(strExpression,strSearch,strReplace);
				
			}
			else
			{
				strReturn = "";
				strTemp = strExpression;
				intPosition = strTemp.indexOf(strSearch);
				while(intPosition >= 0)
				{
					strReturn = strReturn + strExpression.substring(0,intPosition) + strReplace;
					strExpression = strExpression.substring(intPosition+strSearch.length());
					strTemp = strTemp.substring(intPosition+strSearch.length());
					intPosition = strTemp.indexOf(strSearch);
				}
				strReturn = strReturn + strExpression;
			}

			return strReturn;
		}
		
		/**
		 * 鍒囧垎瀛楃涓�
		 * @param SubStr
		 * @param S
		 * @param S1
		 * @param S2
		 */
		public static void CutStr(String SubStr, String S, StringBuffer S1, StringBuffer S2)
		{
			int i = 0;

			i = S.indexOf(SubStr);
			if (i >= 0)
			{
				S1.append(S.substring(0, i).trim());
				//S2.append(S.substring(0, 0) + S.substring(0 + i + SubStr.length()));
				S2.append(S.substring(i + SubStr.length()).trim()); //鍘烠SharpToJava浠ｇ爜闂    閽辨捣鍏�    2011-03-29
			}
			else
			{
				S1.append(S.trim());
				S2.delete(0, S2.length());
			}
		}


		/**
		 * 鍙栧垏鍒嗗瓧绗︿覆
		 * @param SrcStr
		 * @param ApartStr
		 * @param Index
		 * @return
		 */
		public static String GetApartStr(String SrcStr, String ApartStr, int Index)
		{
			String Str = "";
			int i = 0;
			int j = 0;
			String TmpStr1 = "";
			String TmpStr2 = "";

			if ((Index < 0) || (SrcStr.equals("")))
			{
				return Str;
			}

			TmpStr1 = SrcStr.trim();
			TmpStr2 = TmpStr1;
			i = TmpStr1.indexOf(ApartStr);
			while (i >= 0)
			{
				TmpStr2 = TmpStr1.substring(0, i);
				TmpStr1 = TmpStr1.substring(0, 0) + TmpStr1.substring(0 + i + ApartStr.length());
				j = j + 1;
				if (j == Index)
				{
					break;
				}
				i = TmpStr1.indexOf(ApartStr);
			}
			if (j == Index)
			{
				Str = TmpStr2.trim();
			}
			if ((j == (Index - 1)) && (!TmpStr1.equals("")))
			{
				Str = TmpStr1.trim();
			}

			return Str;
		}
		

		
		/**
		 * 鏍囪瘑绗︽浛鎹�     閽辨捣鍏�      2011-07-13
		 * @param source    鏇挎崲婧�
		 * @param keyword   鏌ユ壘鍏抽敭瀛�
		 * @param keyset    鏇挎崲瀛楃涓�
		 * @param ignoreCase鏄惁璺宠繃澶у皬鍐�
		 * @return
		 */
		public static String ReplaceIdentify(String source,String keyword,String keyset,Boolean ignoreCase){
			//澹版槑鍖归厤鍙橀噺
			Pattern p = null;
			//Identify鏍囪瘑绗�
			Pattern ptnIdentify = Pattern.compile("[A-Za-z0-9_#\\$]*+",Pattern.DOTALL);

			//缂栬瘧鍖归厤淇℃伅
			if (ignoreCase){
				p = Pattern.compile(".?"+keyword+".?",Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
				//淇涓�涓ぇ灏忓啓瀵艰嚧涓嶈兘姝ｅ父鏌ヨ鍒板巻鍙插崟鎹殑闂 Kyo 20110721
				keyword=keyword.toLowerCase();
				keyset=keyset.toLowerCase();
			} else {
				p = Pattern.compile(".?"+keyword+".?",Pattern.DOTALL);
			}
			//姝ｅ垯鍖归厤瑙ｆ瀽
			Matcher m = p.matcher(source); 
			//澹版槑缁撴灉瀛樺偍
			StringBuffer sb = new StringBuffer();

			//寰幆鍖归厤澶勭悊
			while(m.find()){
				//淇涓�涓ぇ灏忓啓瀵艰嚧涓嶈兘姝ｅ父鏌ヨ鍒板巻鍙插崟鎹殑闂 Kyo 20110721
				String fielsDesc  =ignoreCase?m.group().toLowerCase():m.group();
				String sReplaceSet=fielsDesc.replace(keyword, "");

//				System.out.println("find:"+fielsDesc+" replace:"+sReplaceSet);
				if (sReplaceSet.isEmpty()){
	         	m.appendReplacement(sb, keyset); 
				} else {
					//濡傛灉涓嶅惈鏍囪瘑绗︼紝鍒欒涓烘槸璇�
					Boolean mFound = false;
					Matcher mChild = ptnIdentify.matcher(sReplaceSet);
					while(mChild.find()){
						if (mChild.start()!=mChild.end()){
							mFound=true;
							break;
						}
					}
					if (!mFound){
						m.appendReplacement(sb, fielsDesc.replace(keyword, keyset)); 
					}
				}
	        }
			//娣诲姞鍚庨潰瀛楃
			m.appendTail(sb); 
			return sb.toString();			
		}
		
		/**
		 * 鍙栨寚瀹氬瓧绗﹀垎闅斿彸杈圭殑鎵�鏈夊瓧绗︺�備笉瀛樺湪鍒欏彇鏁翠釜瀛楃銆�
		 * @param SourceStr
		 * @param Split
		 * @return
		 */
	    public static String GetRightStr(String SourceStr, String Split)
	    {
	        int iPos = SourceStr.lastIndexOf(Split);
	        if (iPos < 0)
	            return SourceStr;
	        else
	            return SourceStr.substring(iPos + Split.length(), SourceStr.length());

	    }
}

