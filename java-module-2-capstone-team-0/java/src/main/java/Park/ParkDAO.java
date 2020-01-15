package Park;

import java.util.List;

public interface ParkDAO {

	//Create
	public void addPark(Park newPark);
	
	//Read
	public Park getParkById(int park_id);
	
	public List<Park> getAllParks ();
	
	public Park getParkByName (String parkName);

	public String[] getAllParkNames();
	
	//Update Method 
	public void changeParkInfo(Park updatedPark);
	
	//Delete method
	public void deleteParkById (int park_id);
	
	public void deleteParksByName (String parkName);


}
