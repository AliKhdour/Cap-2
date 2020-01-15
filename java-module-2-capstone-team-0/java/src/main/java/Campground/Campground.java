package Campground;

import java.util.ArrayList;
import java.util.List;

import Park.Park;

public class Campground {
	
	private int campground_id;
	private int park_id;
	private String name;
	private int open_from_mm;
	private int open_to_mm;
	private double daily_fee;
	private String daily_fee_formated;
	private List <Integer> closedMonths;
	
	public List <Integer> getClosedMonths(){
		List <Integer> closedMonths = new ArrayList <Integer>();
		for (int i =0; i <open_from_mm; i++) {
			closedMonths.add(i);
		}
		for (int i =12; i>open_to_mm; i--) {
			closedMonths.add(i);
		}
		return closedMonths;
	}
	
	
	public int getCampground_id() {
		
	return campground_id;
	}
	public void setCampground_id(int campground_id) {
		this.campground_id = campground_id;
	}
	public int getPark_id() {
		return park_id;
	}
	public void setPark_id(int park_id) {
		this.park_id = park_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getOpen_from_mm() {
		return open_from_mm;
	}
	public void setOpen_from_mm(int open_from_mm) {
		this.open_from_mm = open_from_mm;
	}
	public int getOpen_to_mm() {
		return open_to_mm;
	}
	public void setOpen_to_mm(int open_to_mm) {
		this.open_to_mm = open_to_mm;
	}
	public double getDaily_fee() {
		return daily_fee;
	}
	public void setDaily_fee(double daily_fee) {
		this.daily_fee = daily_fee;
	}
	public String getDaily_fee_formated() {
		daily_fee_formated = "$" +String.format("%.2f",daily_fee);
		return daily_fee_formated;
	}
	public void setDaily_fee_formated(String daily_fee_formated) {
		this.daily_fee_formated = daily_fee_formated;
	}
	@Override
	public String toString() {
		return "Campground [campground_id=" + campground_id + ", park_id=" + park_id + ", name=" + name
				+ ", open_from_mm=" + open_from_mm + ", open_to_mm=" + open_to_mm + ", daily_fee=" + daily_fee
				+ ", daily_fee_formated=" + daily_fee_formated + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + campground_id;
		long temp;
		temp = Double.doubleToLongBits(daily_fee);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((daily_fee_formated == null) ? 0 : daily_fee_formated.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + open_from_mm;
		result = prime * result + open_to_mm;
		result = prime * result + park_id;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Campground other = (Campground) obj;
		if (campground_id != other.campground_id)
			return false;
		if (Double.doubleToLongBits(daily_fee) != Double.doubleToLongBits(other.daily_fee))
			return false;
		if (daily_fee_formated == null) {
			if (other.daily_fee_formated != null)
				return false;
		} else if (!daily_fee_formated.equals(other.daily_fee_formated))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (open_from_mm != other.open_from_mm)
			return false;
		if (open_to_mm != other.open_to_mm)
			return false;
		if (park_id != other.park_id)
			return false;
		return true;
	}
	public String getToDateAsString(int date) {
		String toMonth = "";
		switch(date) {                  
		
		case 1:
		toMonth = "January";
			break;                    
		case 2:
			toMonth = "Febuary";					  
			break;                   
		case 3: 
			toMonth = "March";
			break;                    
		case 4:
			toMonth = "April";						
			break; 
		case 5: 
			toMonth = "May";
			break;                    
		case 6:
			toMonth = "June";						
			break;
		case 7:
			toMonth = "July";
			break;                   
		case 8:
			toMonth = "August";						  
			break;                   
		case 9: 
			toMonth = "September";
			break;                    
		case 10:
			toMonth = "October";							
			break; 
		case 11: 
			toMonth = "November";
			break;                    
		case 12:
			toMonth = "December";
			break;}
		return toMonth;
			
	
	}
	public String getFromdateAsStrin(int date) {
		String fromMonth = "";
		switch(date) {                  
		
		case 1:
		fromMonth = "January";
			break;                   
		case 2:
			fromMonth = "Febuary";					  
			break;                   
		case 3: 
			fromMonth = "March";
			break;                    
		case 4:
			fromMonth = "April";						
			break; 
		case 5: 
			fromMonth = "May";
			break;                    
		case 6:
			fromMonth = "June";						
			break;
		case 7:
			fromMonth = "July";
			break;                   
		case 8:
			fromMonth = "August";						  
			break;                   
		case 9: 
			fromMonth = "September";
			break;                    
		case 10:
			fromMonth = "October";							
			break; 
		case 11: 
			fromMonth = "November";
			break;                    
		case 12:
			fromMonth = "December";
			break;}
		return fromMonth;
			
	}
	public String displayCampScreen() {					
		String result = "_________________________________ \n"+ 
						name +" " +getDaily_fee_formated() +"/day \n"+
						"Open: " +getFromdateAsStrin(getOpen_from_mm()) +" to " +getToDateAsString(getOpen_to_mm());
		return result;
		}
		
		
		
	
	
	
	
}
