package Park;

import java.time.LocalDate;

public class Park {
	
	private int park_id;
	private String name;
	private String location;
	private LocalDate establish_date;
	private int area;
	private int visitors;
	private String description;
	
	
	public int getVisitors() {
		return visitors;
	}
	public void setVisitors(int visitors) {
		this.visitors = visitors;
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
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public LocalDate getEstablish_date() {
		return establish_date;
	}
	public void setEstablish_date(LocalDate establish_date) {
		this.establish_date = establish_date;
	}
	public int getArea() {
		return area;
	}
	public void setArea(int area) {
		this.area = area;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	@Override
	public String toString() {
		return "Park [park_id=" + park_id + ", name=" + name + ", location=" + location + ", establish_date="
				+ establish_date + ", area=" + area + ", visitors=" + visitors + ", description=" + description + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + area;
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((establish_date == null) ? 0 : establish_date.hashCode());
		result = prime * result + ((location == null) ? 0 : location.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + park_id;
		result = prime * result + visitors;
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
		Park other = (Park) obj;
		if (area != other.area)
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (establish_date == null) {
			if (other.establish_date != null)
				return false;
		} else if (!establish_date.equals(other.establish_date))
			return false;
		if (location == null) {
			if (other.location != null)
				return false;
		} else if (!location.equals(other.location))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (park_id != other.park_id)
			return false;
		if (visitors != other.visitors)
			return false;
		return true;
	}
	public void displayParkScreen(Park name) {
		System.out.println("Location:        "+ name.getLocation());
		System.out.println("Established:     "+name.getEstablish_date());
		System.out.println("Area:            " + name.getArea() + " sq km");
		System.out.println("Annual Visitors: " + name.getVisitors());
		System.out.println();
		System.out.println(name.getDescription());
		
	}
	
	
}
