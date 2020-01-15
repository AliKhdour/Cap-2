package Campground;

import java.util.List;

import Park.Park;

public interface CampgroundDAO {
	
	//Create
	public void addCampgroundDAO(Campground newCampground);
	
	//Read
	public Campground getCampgroundByCampgroundId( int campground_id);
	
	public List<Campground> getAllCampgrounds ();

	public String[] getCampgroundNamesByParkIdArray(int park_id);
	
	public List<String> getCampgroundNamesByParkIdList(int park_id);
	
	public List<Campground> getCampgroundsByParkId(int park_id);
	
	public Campground getCampgroundByParkAndName(Park aPark, String campgroundName);
	

	//Update
	public void changeCampgroundInfo (Campground updatedCampground);
	
	//Delete 
	public void deleteCampgroundByCampgroundId (int campground_id);
	
	public void deleteCampgroundsByParkId(int park_id);
}
