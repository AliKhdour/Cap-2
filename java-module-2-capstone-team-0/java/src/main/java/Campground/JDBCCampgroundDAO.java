package Campground;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import Park.Park;

public class JDBCCampgroundDAO implements CampgroundDAO{

	private JdbcTemplate jdbcTemplate;
	public JDBCCampgroundDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);  
	}

	@Override 
	public void addCampgroundDAO(Campground newCampground) {
		String sqlAddCampGround = "INSERT INTO campground (park_id, name, open_from_mm, open_to_mm, daily_fee) \n" + 
				" VALUES (?,?,?,?,?)";
		jdbcTemplate.update(sqlAddCampGround,newCampground.getPark_id(),
				newCampground.getName(), newCampground.getOpen_from_mm(), 
				newCampground.getOpen_to_mm(), newCampground.getDaily_fee());
	}

	@Override
	public Campground getCampgroundByCampgroundId(int campground_id) {
		Campground theCampground= null;
		String sqlFindCampgroundById = "SELECT * FROM campground WHERE campground_id = ?";
		
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlFindCampgroundById,campground_id);
		while(results.next()) {
			theCampground = mapRowToCampground(results);
		}
		return theCampground;
	}
	@Override
	public List<Campground> getAllCampgrounds() {
		List<Campground> theCampgrounds = new ArrayList <Campground>();
		String sqlGetAllCampground ="SELECT * FROM campground";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetAllCampground);
		while (results.next()) {
			Campground aCampground = mapRowToCampground(results);
			theCampgrounds.add(aCampground);
		}
		return theCampgrounds;
	}
	
	@Override
	public String[] getCampgroundNamesByParkIdArray(int park_id) {
		String campgroundNamesSQL = "SELECT name FROM campground WHERE park_id = ? ORDER BY name ASC";
		List<String>results = jdbcTemplate.queryForList(campgroundNamesSQL, String.class, park_id);
		String [] campgroundNames = (String [])results.toArray(new String[results.size()]);
		return campgroundNames;
	}



	@Override
	public List<String> getCampgroundNamesByParkIdList(int park_id) {
		List<String> theCampgrounds = new ArrayList <String>();
		String sqlGetAllCampground ="SELECT name FROM campground WHERE park_id = ?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetAllCampground,park_id);
		while (results.next()) {
			String campName = results.getString("name");
			theCampgrounds.add(campName);
		}
		return theCampgrounds;
	}
	@Override
	public List<Campground> getCampgroundsByParkId(int park_id) {
	List<Campground> theCampgrounds = new ArrayList <Campground>();
	String sqlGetAllCampground ="SELECT * FROM campground WHERE park_id = ?";
	SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetAllCampground,park_id);
	while (results.next()) {
	Campground aCampground = mapRowToCampground(results);
	theCampgrounds.add(aCampground);
	}
	return theCampgrounds;
	}

	@Override
	public Campground getCampgroundByParkAndName(Park aPark, String campgroundName) {
		String sqlGetAllCampground ="SELECT * FROM campground WHERE park_id = ? and name= ?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetAllCampground,aPark.getPark_id(),campgroundName);
		Campground aCampground=null;
		while (results.next()) {
			aCampground = mapRowToCampground(results);
		}
		return aCampground;
	}

	@Override
	public void changeCampgroundInfo(Campground updatedCampground) {
		String sqlChangeInfo = "UPDATE campground "+
				" set park_id = ? set name = ? set open_from_mm = ? set open_to_mm = ? " +
				" daily_fee = ? WHERE campground_id = ?";
		jdbcTemplate.update(sqlChangeInfo,updatedCampground.getPark_id(),
				updatedCampground.getName(), updatedCampground.getOpen_from_mm(), 
				updatedCampground.getOpen_to_mm(), updatedCampground.getDaily_fee(), updatedCampground.getCampground_id());
	}

	@Override
	public void deleteCampgroundByCampgroundId(int campground_id) {
		String sqlDeleteCampground = "DELETE FROM campground WHERE campground_id = ?";
		jdbcTemplate.update(sqlDeleteCampground, campground_id);

	}

	@Override
	public void deleteCampgroundsByParkId(int park_id) {
		String sqlDeleteCampground = "DELETE FROM campground WHERE park_id = ?";
		jdbcTemplate.update(sqlDeleteCampground, park_id);
	}
	private Campground mapRowToCampground(SqlRowSet rowsReturned) {
		Campground aCampground = new Campground();
		aCampground.setCampground_id(rowsReturned.getInt("campground_id"));
		aCampground.setPark_id(rowsReturned.getInt("park_id"));
		aCampground.setName(rowsReturned.getString("name"));
		aCampground.setOpen_from_mm(rowsReturned.getInt("open_from_mm"));
		aCampground.setOpen_to_mm(rowsReturned.getInt("open_to_mm"));
		aCampground.setDaily_fee(rowsReturned.getDouble("daily_fee"));
		return aCampground;
	}

}
