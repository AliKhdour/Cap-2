package Reservation;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

public class JDBCReservationDAO implements ReservationDAO{

	private JdbcTemplate jdbcTemplate;
	public JDBCReservationDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);  
	}
	
	@Override 
	public void addReservation(Reservation newReservation) {
	String sqlAddReservation = "INSERT INTO reservation (site_id, name, from_date, to_date, create_date)"+
			" VALUES (?,?,?,?,?)";
	jdbcTemplate.update(sqlAddReservation, newReservation.getSite_id(), newReservation.getName(),
			newReservation.getFrom_date(), newReservation.getTo_date(), newReservation.getCreate_date());
	}

	@Override
	public Reservation getReservationByReservationId(int reservation_id) {
	Reservation theReservation = null;
	String sqlFindReservationById = "SELECT * FROM reservation where reservation_id = ?";
	SqlRowSet results =jdbcTemplate.queryForRowSet(sqlFindReservationById,reservation_id);
	
	while (results.next()) {
		theReservation = mapRowToReservation(results);
	}
	
	return theReservation;
	}
	

	@Override
	public List<Reservation> getReservationsBySiteId(int site_id) {
		List<Reservation> theReservations = new ArrayList <Reservation>();
		Reservation theReservation = null;
		String sqlFindReservationById = "SELECT * FROM reservation where site_id = ?";
		SqlRowSet results =jdbcTemplate.queryForRowSet(sqlFindReservationById,site_id);
		
		while (results.next()) {
			theReservation = mapRowToReservation(results);
			theReservations.add(theReservation);
		}
		return theReservations;
	}
	
	@Override
	public List<Reservation> getReservationsByName(String name) {
		List<Reservation> theReservations = new ArrayList <Reservation>();
		Reservation theReservation = null;
		name = "%" +name +"%";
		String sqlFindReservationByName = "SELECT * FROM reservation WHERE name ilike ?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlFindReservationByName,name);
		while (results.next()) {
			theReservation = mapRowToReservation(results);
			theReservations.add(theReservation);
		}
		
		return theReservations;
	}
	
	@Override
	public List<String> getAllReservationNames() {
		List<String> reservationNames = new ArrayList<String>();
		String sqlFindReservationName = "SELECT name FROM reservation";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlFindReservationName);
		String currentName= "";
		while (results.next()) {
			currentName = results.getString("name");
			reservationNames.add(currentName);
		}
		return reservationNames;
	}

	@Override
	public List<Integer> getAllReservationIds() {
		List<Integer> reservationIds = new ArrayList<Integer>();
		String sqlFindReservationId = "SELECT reservation_id FROM reservation";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlFindReservationId);
		int currentId=0;
		while (results.next()) {
			currentId = results.getInt("reservation_id");
			reservationIds.add(currentId);
		}
		return reservationIds;
	}
	@Override
	public List<Integer> getReservationConflictsSiteIds(int campground_id, LocalDate from_date, LocalDate to_date) {
		List<Integer> site_ids = new ArrayList <Integer>();
		String sqlFindReservationByName = "Select reservation.site_id \n" + 
				" FROM reservation \n" + 
				" JOIN site ON reservation.site_id = site.site_id" +
				" where (to_date >= (DATE '" +from_date +"') and to_date <= (DATE '" +from_date +"')) or \n" + 
				" (from_date >= (DATE '" +to_date +"') and from_date <= (DATE '" +to_date +"')) \n" + 
				" or (from_date<(DATE '" +to_date +"') and to_date>(DATE '" +to_date +"'))" +
				" and campground_id = " +campground_id;
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlFindReservationByName);
		while (results.next()) {
			int thisSite_id = results.getInt("site_id");
			site_ids.add(thisSite_id);
		}
		return site_ids;
	}
	
	@Override
	public List<Integer> getAllSiteIdsByCampgroundId(int campground_id) {
		List <Integer> siteIds = new ArrayList <Integer>();		
		Set <Integer> siteIdsNoRepeats = new HashSet <Integer>();
		String sqlSiteIds ="SELECT site_id FROM site WHERE campground_id = ?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlSiteIds, campground_id);
		while (results.next()) {
			int thisSite_id = results.getInt("site_id");
			siteIdsNoRepeats.add(thisSite_id);
		}
		for (int id : siteIdsNoRepeats) {
			siteIds.add(id);
		}
		return siteIds;
	}
	
	@Override
	public List<Integer> getAvailibleFiveSiteIds(int campground_id, LocalDate from_date, LocalDate to_date) {
		List <Integer> availibleSiteIds = new ArrayList <Integer>();
		List <Integer> notAvailibleSiteIds = getReservationConflictsSiteIds(campground_id, from_date, to_date);
		List <Integer> allSiteIdsForCampground = getAllSiteIdsByCampgroundId(campground_id);
		int counter =0;
		for (int siteId : allSiteIdsForCampground) {
			if ((!notAvailibleSiteIds.contains(siteId)) && counter<5) {
				availibleSiteIds.add(siteId);
				counter++;
			}
		}
		
 		return availibleSiteIds;
	}
	
	@Override
	public void changeReservationInfo(Reservation updatedReservation) {
		String sqlChangeInfo = "UPDATE reservation "+
			" set site_id = ? set name = ? set from_date = ? set to_date = ? set create_date = ? " +
			" WHERE reservation_id = ?";
		jdbcTemplate.update(sqlChangeInfo, updatedReservation.getSite_id(), updatedReservation.getName(), updatedReservation.getFrom_date(),
				 updatedReservation.getTo_date(), updatedReservation.getCreate_date(), updatedReservation.getReservation_id());
	}

	@Override
	public void deleteReservationByReservationId(int reservation_id) {
		String sqlDeleteReservation = "DELETE FROM reservation WHERE reservation_id = ?";
		jdbcTemplate.update(sqlDeleteReservation, reservation_id);
	}

	@Override
	public void deleteReservationsByReservationSiteId(int site_id) {
		String sqlDeleteReservation = "DELETE FROM reservation WHERE site_id = ?";
		jdbcTemplate.update(sqlDeleteReservation, site_id);
	}
	
	private Reservation mapRowToReservation (SqlRowSet rset) {
		Reservation aReservation = new Reservation();
		aReservation.setReservation_id(rset.getInt("reservation_id"));
		aReservation.setSite_id(rset.getInt("site_id"));
		aReservation.setName(rset.getString("name"));
		aReservation.setFrom_date((rset.getDate("from_date")).toLocalDate());
		aReservation.setTo_date((rset.getDate("to_date")).toLocalDate());
		aReservation.setCreate_date((rset.getDate("create_date")).toLocalDate());
		return aReservation;
	}
	

}
