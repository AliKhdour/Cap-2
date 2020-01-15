package Reservation;

import java.time.LocalDate;
import java.util.List;

public interface ReservationDAO {


	//CREATE
	public void addReservation(Reservation newReservation);

	//SELECT
	public Reservation getReservationByReservationId(int reservation_id);

	public List<Reservation> getReservationsBySiteId(int site_id);
	
	public List<Reservation> getReservationsByName(String name);
	
	public List<String> getAllReservationNames();
	
	public List<Integer> getAllReservationIds();
	
	public List <Integer> getReservationConflictsSiteIds(int campground_id, LocalDate from_date, LocalDate to_date);
	
	public List<Integer> getAvailibleFiveSiteIds(int campground_id, LocalDate from_date, LocalDate to_date);
	
	public List<Integer> getAllSiteIdsByCampgroundId(int campground_id);
	//UPDATE
	public void changeReservationInfo(Reservation updatedReservation); 
	
	//DELETE
	public void deleteReservationByReservationId(int reservation_id);
	
	public void deleteReservationsByReservationSiteId(int site_id);

}
