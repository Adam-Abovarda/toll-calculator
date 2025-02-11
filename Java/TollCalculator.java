

import java.util.*;
import java.util.concurrent.*;

public class TollCalculator {

	/**
	 * Calculate the total toll fee for one day
	 *
	 * @param vehicle - the vehicle
	 * @param dates   - date and time of all passes on one day
	 * @return - the total toll fee for that day
	 */

	
	  public int getTollFee(Vehicle vehicle, Date... dates) {
	
		  Date intervalStart = dates[0];
		    int totalFee = 0 ;
		    
		    if (isTollFreeVehicle(vehicle)) {return totalFee;} // to test if the vehicle belongs to a type that can pass free of charge
		    else   {
		       int tempFee = getTollFee(intervalStart, vehicle); // fixed here by moving this line out of the for loop, to refer to the first vehicle passing only.
		       for (Date date : dates) {
		        int nextFee = getTollFee(date, vehicle);
		     
	  	  	  
	  
	  TimeUnit timeUnit = TimeUnit.MINUTES; long diffInMillies = date.getTime() - //calculate the time period between vehicle passings 
	  intervalStart.getTime(); long minutes = timeUnit.convert(diffInMillies,
	  TimeUnit.MILLISECONDS); 
	  
	  	  
	  if (minutes <= 60 && minutes !=0) {
		  if (totalFee > 0) totalFee -= tempFee;
	      if (nextFee >= tempFee) tempFee = nextFee;
	      totalFee += tempFee; 
	  } 
	  else if (minutes ==0) { totalFee += nextFee;} //for the first passing
	  else {
		  if (nextFee >= tempFee) tempFee = nextFee;
	      totalFee += nextFee;
	      intervalStart = date; // to change the first passing to the be the current one
	 }
				 
		       
	  } if (totalFee > 60) totalFee = 60; return totalFee;
		  }
	  
	 
	  }
	 


	private boolean isTollFreeVehicle(Vehicle vehicle) {
		if (vehicle == null)
			return false;
		String vehicleType = vehicle.getType();
		//System.out.println(vehicleType);
		return vehicleType.equals(TollFreeVehicles.MOTORBIKE.getType())
				|| vehicleType.equals(TollFreeVehicles.TRACTOR.getType())
				|| vehicleType.equals(TollFreeVehicles.EMERGENCY.getType())
				|| vehicleType.equals(TollFreeVehicles.DIPLOMAT.getType())
				|| vehicleType.equals(TollFreeVehicles.FOREIGN.getType())
				|| vehicleType.equals(TollFreeVehicles.MILITARY.getType());
	}

	public int getTollFee(final Date date, Vehicle vehicle) {
		if (isTollFreeDate(date) || isTollFreeVehicle(vehicle))
			return 0;
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.setTime(date);
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);

		/* System.out.println(hour + "hour"); System.out.println(minute + "minute"); */

		if (hour == 6 && minute >= 0 && minute <= 29)
			return 8;
		else if (hour == 6 && minute >= 30 && minute <= 59)
			return 13;
		else if (hour == 7 && minute >= 0 && minute <= 59)
			return 18;
		else if (hour == 8 && minute >= 0 && minute <= 29)
			return 13;
		else if (hour >= 8 && hour <= 14 && minute >= 0 && minute <= 59)
			return 8; // problem with first 29 minutes of each hour (Fixed)
		else if (hour == 15 && minute >= 0 && minute <= 29)
			return 13;
		else if (hour == 15 && minute >= 0 || hour == 16 && minute <= 59)
			return 18;
		else if (hour == 17 && minute >= 0 && minute <= 59)
			return 13;
		else if (hour == 18 && minute >= 0 && minute <= 29)
			return 8;
		else
			return 0;
	}

	private Boolean isTollFreeDate(Date date) {
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.setTime(date);
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);

		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
		if (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY)
			return true;

		if (year == 2022) {
			if (month == Calendar.JANUARY && day == 1 || month == Calendar.MARCH && (day == 28 || day == 29)
					|| month == Calendar.APRIL && (day == 1 || day == 30)
					|| month == Calendar.MAY && (day == 1 || day == 8 || day == 9)
					|| month == Calendar.JUNE && (day == 5 || day == 6 || day == 21) || month == Calendar.JULY
					|| month == Calendar.NOVEMBER && day == 1
					|| month == Calendar.DECEMBER && (day == 24 || day == 25 || day == 26 || day == 31)) {
				return true;
			}
		}
		return false;
	}

	public enum TollFreeVehicles {
		MOTORBIKE("Motorbike"), TRACTOR("Tractor"), EMERGENCY("Emergency"), DIPLOMAT("Diplomat"), FOREIGN("Foreign"),
		MILITARY("Military");

		private final String type;

		TollFreeVehicles(String type) {
			this.type = type;
		}

		public String getType() {
			return type;
		}
	}

}
