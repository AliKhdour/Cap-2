package Reservation;

import java.time.LocalDate;

import Site.Site;

public class Reservation {
	private int reservation_id;
	private int site_id;
	private String name;
	private LocalDate from_date;
	private LocalDate to_date;
	private LocalDate create_date;
	private double dailyCost;
	private int numberOfDays;
	private double totalCost;
	private String totalCostFormatted;
	
	public String getTotalCostFormatted() {
		String result = String.format("$%.2f", totalCost);
		return result;
	}
	
	
	public void setDailyCost(double dailyCost) {
		this.dailyCost = dailyCost;
	}
	public int getNumberOfDays() {
		setNumberOfDays();
		return numberOfDays;
	}
	public void setNumberOfDays() {
		this.numberOfDays =to_date.compareTo(from_date) +1;
	}
	public double getTotalCost() {
		setTotalCost();
		return totalCost;
	}
	public void setTotalCost() {
		int numberOfDays = getNumberOfDays();
		totalCost = numberOfDays *dailyCost;
	}
	public Reservation () {
		
	}
	public Reservation (int site_id,String name, LocalDate from_date, LocalDate to_date) {
		this.site_id = site_id;
		this.name= name;
		this.from_date= from_date;
		this.to_date= to_date;
		create_date=LocalDate.now();
		
	}
	
	public int getReservation_id() {
		return reservation_id;
	}
	public int getSite_id() {
		return site_id;
	}
	public String getName() {
		return name;
	}
	public LocalDate getFrom_date() {
		return from_date;
	}
	public LocalDate getTo_date() {
		return to_date;
	}
	public LocalDate getCreate_date() {
		return create_date;
	}
	public void setReservation_id(int reservation_id) {
		this.reservation_id = reservation_id;
	}
	public void setSite_id(int site_id) {
		this.site_id = site_id;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setFrom_date(LocalDate from_date) {
		this.from_date = from_date;
	}
	public void setTo_date(LocalDate to_date) {
		this.to_date = to_date;
	}
	public void setCreate_date(LocalDate create_date) {
		this.create_date = create_date;
	}
	public String showReservation() {
		String result= "_______________________________________ \n"+
						"Reservation id: " +reservation_id +"\n"+
						"Site id: " +site_id +"\n"+
						"Name: " +name +"\n"+
						"Arrival Date: " +from_date +"\n" +
						"Departure Date: " +to_date +"\n" +
						"Reserved on : " +create_date +"\n";
		return result;
	}
	public String reservationConfirmation() {
		String result= "_______________________________________ \n"+
						"Reservation id: " +reservation_id +"\n"+
						"Site id: " +site_id +"\n"+
						"Name: " +name +"\n"+
						"Arrival Date: " +from_date +"\n" +
						"Departure Date: " +to_date +"\n" +
						"Reserved on : " +create_date +"\n"+
						"Total cost: " +getTotalCostFormatted();
		return result;
	}
	@Override
	public String toString() {
		return "Reservation [reservation_id=" + reservation_id + ", site_id=" + site_id + ", name=" + name
				+ ", from_date=" + from_date + ", to_date=" + to_date + ", create_date=" + create_date + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((create_date == null) ? 0 : create_date.hashCode());
		result = prime * result + ((from_date == null) ? 0 : from_date.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + reservation_id;
		result = prime * result + site_id;
		result = prime * result + ((to_date == null) ? 0 : to_date.hashCode());
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
		Reservation other = (Reservation) obj;
		if (create_date == null) {
			if (other.create_date != null)
				return false;
		} else if (!create_date.equals(other.create_date))
			return false;
		if (from_date == null) {
			if (other.from_date != null)
				return false;
		} else if (!from_date.equals(other.from_date))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (reservation_id != other.reservation_id)
			return false;
		if (site_id != other.site_id)
			return false;
		if (to_date == null) {
			if (other.to_date != null)
				return false;
		} else if (!to_date.equals(other.to_date))
			return false;
		return true;
	}
	
}
