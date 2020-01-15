package Park;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import Campground.Campground;

public class JDBCParkDAO  implements ParkDAO{
	
	private JdbcTemplate jdbcTemplate;
	public JDBCParkDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);  
	}
	@Override
	public void addPark(Park newPark) {
		String sqlAddPark = "Insert into park(name,location,establish_date,area,visitors,description)values (?,?,?,?,?,?);";
				
		jdbcTemplate.update(sqlAddPark,
				newPark.getName(),
				newPark.getLocation(),
				newPark.getEstablish_date(),
				newPark.getArea(),
				newPark.getVisitors(),	
				newPark.getDescription());		
	}

	@Override
	public Park getParkById(int park_id) {
		Park thePark= null;
		String sqlFindParkdById = "SELECT * FROM park WHERE park_ id = ?";
		
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlFindParkdById,park_id);
		while(results.next()) {
			thePark = mapRowToPark(results);
		}
		return thePark;
		
	}

	@Override
	public Park getParkByName(String parkName) {
		// TODO Auto-generated method stub
		Park thePark = null;
		parkName = "%" +parkName +"%";
		String sqlFindParkByName = "SELECT * FROM park WHERE name ilike ? ";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlFindParkByName, parkName);        
		while(results.next()) {
		thePark = mapRowToPark(results);
		}
		return thePark;
	}
	@Override
	public List<Park> getAllParks() {
		List<Park> theParks = new ArrayList <Park>();
		String sqlGetAllParks ="SELECT name FROM park ORDER BY name ASC";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetAllParks);
		while (results.next()) {
			Park aPark = mapRowToPark(results);
			theParks.add(aPark);
		}
		return theParks;
	}
	@Override
	public String[] getAllParkNames() {
					
			String parkNamesSQL = "SELECT name FROM park ORDER BY name ASC";
				
	        List<String> results =  jdbcTemplate.queryForList(parkNamesSQL, String.class);
	        results.add("Quit Application");
	        
	        String[] parkNames = (String [])results.toArray(new String[results.size()]);

	        return parkNames;      

		}
	@Override
	public void changeParkInfo(Park updatedPark) {
		String sqlChangeParkInfo = "update park SET name = ?, location = ?, establish_date = ?,area = ? visitors = ?, description = ? where park_id = ?;";
		jdbcTemplate.update(sqlChangeParkInfo,
				updatedPark.getName(),
				updatedPark.getLocation(),
				updatedPark.getEstablish_date(),
				updatedPark.getArea(),
				updatedPark.getVisitors(),
				updatedPark.getDescription());	
	}

	@Override
	public void deleteParkById(int park_id) {
		String sqlDeletePark = "DELETE FROM park WHERE Park_id = ?";
		jdbcTemplate.update(sqlDeletePark, park_id);
		
	}

	@Override
	public void deleteParksByName(String parkName) {
		String sqlDeleteParkByName = "Delete from park where name = ?;?";
		jdbcTemplate.update(sqlDeleteParkByName, parkName);
				
	}

	private Park mapRowToPark (SqlRowSet rowsReturned) {
		Park aPark = new Park();
		aPark.setPark_id(rowsReturned.getInt("park_id"));
		aPark.setName(rowsReturned.getString("name"));
		aPark.setLocation(rowsReturned.getString("location"));
		aPark.setEstablish_date((rowsReturned.getDate("establish_date").toLocalDate()));
		aPark.setArea(rowsReturned.getInt("area"));
		aPark.setVisitors(rowsReturned.getInt("visitors"));
		aPark.setDescription(rowsReturned.getString("description"));
		return aPark;
	}
}
