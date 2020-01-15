package Site;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import Park.Park;

public class JDBCSiteDAO implements SiteDAO {
	
	private JdbcTemplate jdbcTemplate;
	public JDBCSiteDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);  
	}

	@Override
	public void addSite(Site newSite) {
		String sqlAddSite = "Insert into site(campground_id,site_number,max_occupancy,accessible,max_rv_length,utilities)values (?,?,?,?,?,?);";
		jdbcTemplate.update(sqlAddSite,
				newSite.getCampground_id(),
				newSite.getSite_number(),
				newSite.getMax_occupancy(),
				newSite.isAccessible(), 
				newSite.getMax_rv_length(),
				newSite.isUtilities());
	}

	@Override
	public Site getSiteById(int site_id) {
		Site theSite= null;
		String sqlFindSiteById = "SELECT * FROM site WHERE site_id = ?";
		
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlFindSiteById,site_id);
		while(results.next()) {
			theSite = mapRowToSite(results);
		}
		return theSite;
	}

	@Override
	public Site getSiteByCampgroundId(int campground_id) {
		Site theSite= null;
		String sqlFindSiteByCompoundId = "SELECT * FROM site WHERE campground_id = ?";
		
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlFindSiteByCompoundId,campground_id);
		while(results.next()) {
			theSite = mapRowToSite(results);
		}
		return theSite;
	
	}

	@Override
	public void changeSiteInfo(Site updatedSite) {
		String sqlChangeSiteInfo = "update site SET campground_id = ?, site_number = ?, max_occupancy = ?,accessible = ? max_rv_length = ?, utilities = ?;";
		jdbcTemplate.update(sqlChangeSiteInfo,
				updatedSite.getCampground_id(),
				updatedSite.getSite_number(),
				updatedSite.getMax_occupancy(),
				updatedSite.isAccessible(),
				updatedSite.getMax_rv_length(),
				updatedSite.isUtilities());
				
		
	}

	@Override
	public void deleteSitebySiteId(int site_id) {
		String sqlDeleteSite = "DELETE FROM site WHERE site_id = ?";
		jdbcTemplate.update(sqlDeleteSite, site_id);
		
			
	}

	@Override
	public void deleteSitesByCampgroundId(int campgrond_id) {
		String sqlDeleteSiteByCampID = "DELETE FROM site WHERE campground_id = ?";
		jdbcTemplate.update(sqlDeleteSiteByCampID, campgrond_id);
		
	}
	private Site mapRowToSite (SqlRowSet rset) {
		Site aSite = new Site();
		aSite.setSite_id(rset.getInt("site_id"));
		aSite.setCampground_id(rset.getInt("campground_id"));
		aSite.setSite_number(rset.getInt("site_number"));
		aSite.setMax_rv_length(rset.getInt("max_rv_length"));
		aSite.setMax_occupancy(rset.getInt("max_occupancy"));
		aSite.setAccessible(rset.getBoolean("accessible"));
		aSite.setUtilities(rset.getBoolean("utilities"));
		
		String sqlFindSiteById = "SELECT daily_fee, name FROM campground WHERE campground_id = ?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlFindSiteById,aSite.getCampground_id());
		while(results.next()) {
			double cost = results.getDouble("daily_fee");
			aSite.setCost(cost);
			aSite.setCostFormated();
			String campgroundName = results.getString("name");
			aSite.setCampgroundName(campgroundName);
		}
		
		return aSite;
	}


}
