package Site;

public interface SiteDAO {

	//create
	public void addSite(Site newSite);
	
	
	//read 
	public Site getSiteById(int site_id);
	
	public Site getSiteByCampgroundId (int campground_id);
	
	//update
	public void changeSiteInfo(Site updatedSite);
	
	//delete
	
	public void deleteSitebySiteId(int site_id);
	public void deleteSitesByCampgroundId(int campgrond_id);
	
	
	
}
