package com.gk.controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ControllerUtilities { 
	
	public static Date stringToDate(String dateAsString) {
		if (dateAsString == null || dateAsString.trim().length() == 0) return null;
		
		DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
		Date date;
		try {
			date = df.parse(dateAsString);
			return date;
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static boolean isInteger(String s) {
	    try { 
	        Integer.parseInt(s); 
	    } catch(NumberFormatException e) { 
	        return false; 
	    } catch(NullPointerException e) {
	        return false;
	    }
	    
	    return true;
	}
	
	public static boolean isDouble(String s) {
		 try { 
	        Double.parseDouble(s); 
	    } catch(NumberFormatException e) { 
	        return false; 
	    } catch(NullPointerException e) {
	        return false;
	    }
		    
	    return true;
	}
	
	public static boolean isYear(String year) {
		boolean isYearValid = isInteger(year);
		if (isYearValid) {
			int integerYear = Integer.parseInt(year);
			int currentYear = Calendar.getInstance().get(Calendar.YEAR);
			if (integerYear > 0 && integerYear <= currentYear)
				isYearValid = true;
			
		}
		
		return isYearValid;
	}
	
	public static boolean isMonth(String month) {
		boolean isMonthValid = isInteger(month);
		if (isMonthValid) {
			int integerMonth = Integer.parseInt(month);
			if (integerMonth >= 1 && integerMonth <= 12)
				isMonthValid = true;
		}
		
		return isMonthValid;
	}
	
	public static java.util.Date createDate(int year, int month) {
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month-1);
		Date date = calendar.getTime();
		return date;
	}
}
