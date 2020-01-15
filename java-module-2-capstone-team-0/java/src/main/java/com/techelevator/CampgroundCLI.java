package com.techelevator;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import Campground.Campground;
import Campground.JDBCCampgroundDAO;
import Park.JDBCParkDAO;
import Park.Park;
import Park.ParkDAO;
import Reservation.JDBCReservationDAO;
import Reservation.Reservation;
import Reservation.ReservationDAO;
import Site.JDBCSiteDAO;
import Site.Site;

public class CampgroundCLI {

	private JDBCParkDAO parkDAO;
	private JDBCCampgroundDAO campgroundDAO;
	private JDBCSiteDAO siteDAO;
	private JDBCReservationDAO reservationDAO;
	private JdbcTemplate jdbcTemplate;
	private ArrayList<Park> parks = new ArrayList<Park>();
	private Menu parkMenu;
	private Scanner theKeyboard = new Scanner(System.in);

	private static final String VIEW = "View Campgrounds";
	private static final String SEARCH = "Search Reservations";
	private static final String RETURN ="Return to Previous Screen";
	private static final String [] PARK_MENU_OPTIONS = {VIEW, SEARCH, RETURN};

	private static final String SEARCH_AVAILIBLE ="Search for Availible Reservation";
	private static final String [] CAMPGROUND_MENU_OPTIONS = {SEARCH_AVAILIBLE, RETURN};

	private static final String NAME ="Search reservation by Name.";
	private static final String ID = "Search reservation by Reservation Id.";
	private static final String [] RESERVATION_SEARCH = {NAME, ID, RETURN};
	
	public static void main(String[] args) { //Do not touch 
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setUrl("jdbc:postgresql://localhost:5432/campground");
		dataSource.setUsername("postgres");
		dataSource.setPassword("postgres1");

		CampgroundCLI application = new CampgroundCLI(dataSource);
		application.run();
	}

	public CampgroundCLI(DataSource datasource) { // do not touch
		this.parkDAO = new JDBCParkDAO(datasource);
		this.campgroundDAO = new JDBCCampgroundDAO(datasource);
		this.siteDAO = new JDBCSiteDAO(datasource);
		this.reservationDAO = new JDBCReservationDAO(datasource);
		this.jdbcTemplate =new JdbcTemplate(datasource);
	}

	public void run() {// first menu
		parkMenu = new Menu(System.in, System.out);
		String [] parks = parkDAO.getAllParkNames(); // Array of park names based on the data
		Boolean shouldProcess =true; // Allow us to come back to this menu unless we choose the last option to quit application
		while(shouldProcess) {

			String choice = (String)parkMenu.getChoiceFromOptions(parks);
			if (choice.equals("Quit Application")){
				System.out.println("Thank you for visiting the parks, see you next time."); //Graceful exit 
				shouldProcess= false; //application ends
			}
			else {
				System.out.println();

				parkScreen(choice);	// Any other option we send the park name to the next menu
			}
		}


	}

	public void parkScreen(String parkNameFromUserChoice) {

		Park aPark = new Park();
		aPark = parkDAO.getParkByName(parkNameFromUserChoice); // Gets the whole park
		System.out.println ("_______________________________");
		System.out.println(parkNameFromUserChoice +" Information");
		aPark.displayParkScreen(aPark); //Displays park information
		parkMenu(aPark); //calls menu for view campground/search reservations

	}
	public void parkMenu(Park aPark){
		Boolean shouldProcess= true;
		while (shouldProcess) {
			String choice = (String)parkMenu.getChoiceFromOptions(PARK_MENU_OPTIONS);
			switch (choice) {
			case VIEW:
				viewCampground(aPark); // Displays campground information for this park
				break;
			case SEARCH:
				searchReservations(); // Calls method to find existing reservations
				break;
			case RETURN:
				shouldProcess =false;
				break;
			}
		}
	}

	public void viewCampground(Park aPark){
		List <Campground> theCampgrounds = campgroundDAO.getCampgroundsByParkId(aPark.getPark_id());
		System.out.println(aPark.getName() +" National Park Campgrounds");
		for (Campground acampground: theCampgrounds) {
			System.out.println(acampground.displayCampScreen());
		}
		Boolean shouldProcess= true;
		while (shouldProcess) {

			String choice = (String)parkMenu.getChoiceFromOptions(CAMPGROUND_MENU_OPTIONS);

			switch (choice) {
			case SEARCH_AVAILIBLE: // Search available reservations for this park first prompt is to choose campground
				searchAvailible(aPark);
				break;
			case RETURN:
				shouldProcess=false;
				break;
			}
		}	
	}
	//////////////////SEARCHES///////////////////////////
	public void searchReservations(){
		Boolean shouldProcess= true;
		while (shouldProcess) {
			String choice = (String)parkMenu.getChoiceFromOptions(RESERVATION_SEARCH);
			switch (choice) {
			case NAME:
			searchReservationsByName();
			break;
			
			case ID:
			searchReservationById();
			break;
			
			case RETURN:
			shouldProcess= false;
			break;
			}
		}
	}
	public void searchReservationById() {
		int userReservationId = getReservationId();
		if(userReservationId != 0) {
			Reservation aReservation = reservationDAO.getReservationByReservationId(userReservationId);
			aReservation.showReservation();
		}
	}
	public void searchReservationsByName() {
		String name = getReservationName();
		List<Reservation> reservations = reservationDAO.getReservationsByName(name);
		if(reservations.size()==0) {
			System.out.println("I'm sorry you we do not have a reservation under that name.");
			System.out.println("Please try another name, or view Campgrounds to make a reservation.");
		}
		else if (reservations.size()==1) {
			System.out.println("Your reservation is:");
			String reservation= "";
			reservation = reservations.get(0).showReservation();
			System.out.print(reservation);
		}
		else if (reservations.size()>=2) {
			System.out.println("Your reservations are:");
			for (Reservation aReservation: reservations) {
				String reservation= "";
				reservation = aReservation.showReservation();
				System.out.print(reservation);
			}
		}
	}
	public void searchAvailible(Park aPark) { 
		String choosenCampgroundName =getCampgroundChoice(aPark);
		Campground choosenCampground = campgroundDAO.getCampgroundByParkAndName(aPark, choosenCampgroundName);
		System.out.println("What date would you like to arrive?");
		LocalDate today = LocalDate.now();
		LocalDate from_date = getUserDate();
		List <Integer> closedMonths = choosenCampground.getClosedMonths();
		while ((from_date.compareTo(today)<0) || (closedMonths.contains(from_date.getDayOfMonth()))) {
			if(from_date.compareTo(today)<0) {
			System.out.println("I'm sorry you can not make a reservation for a day that has already passed.");
			}
			else if (closedMonths.contains(from_date.getDayOfMonth())) {
			System.out.println("I'm sorry " +choosenCampground.getName() +" is closed in " +from_date.getMonth() +".");
			}
			System.out.println("What date would you like to arrive?");
			from_date = getUserDate();
		}
		System.out.println("What date would you like to end your end you stay?");
		LocalDate to_date = getUserDate();
		
		while((to_date.compareTo(from_date)<0) || (closedMonths.contains(to_date.getMonthValue()))
				|| ((from_date.getMonthValue()<choosenCampground.getOpen_to_mm()) && (to_date.getMonthValue()<choosenCampground.getOpen_from_mm()))){
			if (to_date.compareTo(from_date)<0) {
			System.out.println("You cannot leave before you arrive.");				
			}
			else if (closedMonths.contains(to_date.getMonthValue())) {
			System.out.println("I'm sorry " +choosenCampground.getName() +" is closed in " +to_date.getMonth() +".");
			}
			else if (((from_date.getMonthValue()<choosenCampground.getOpen_to_mm()) && (to_date.getMonthValue()<choosenCampground.getOpen_from_mm()))) {
			System.out.println("I'm sorry your date range extends into " +choosenCampground.getName() +"'s off season.");
			}
			System.out.println("What date would you like to end your end you stay?");
			to_date = getUserDate();			
		}
		
		List <Integer> site_ids =reservationDAO.getAvailibleFiveSiteIds(choosenCampground.getCampground_id(), from_date, to_date);
		if (site_ids.size()==0) {
			System.out.println("I'm sorry. There are no availible sites in that time frame here");
			System.out.println("Please choose another date or Campground.");
		}
		else {
			int choosenSiteId = getSiteChoice(site_ids);
			reservereSite(choosenSiteId, from_date, to_date);
		}
	}
	
/////////////////////////Get information from user/////////////////////////////////	
	public String getReservationName() {
		System.out.println("What name is your reservation under?");
		String aLine = theKeyboard.nextLine();
		aLine =aLine.trim();
		return aLine;
	}
	public int getReservationId() {

		int userReservationId = 0;
		List <Integer> reservationIds = reservationDAO.getAllReservationIds();

		String aLine = theKeyboard.nextLine();
		try {
			aLine= aLine.trim();
			userReservationId = Integer.parseInt(aLine);
			if (reservationIds.contains(userReservationId)) {
			}
			else {
				System.out.println(aLine +" is not a valid Reservation Id please try agian or enter.");
			}
		}
		catch(NumberFormatException ex){
			System.out.println(aLine +" is not a valid Reservation Id please try agian.");
		
		}
		return userReservationId;
	}
	public String getCampgroundChoice(Park aPark) {
		String [] camps = campgroundDAO.getCampgroundNamesByParkIdArray(aPark.getPark_id());
		String choice = (String)parkMenu.getChoiceFromOptions(camps);
		return choice;	
	}
	public int getSiteChoice(List <Integer> availibleSiteIds) {
		boolean getChoice=true;
		int choice = 0;
		while(getChoice) {
			int counter = 1;
			System.out.println("________________________________________");
			for (int siteId : availibleSiteIds){
				System.out.println("Option #" +counter +" " +siteDAO.getSiteById(siteId).displaySite());
				counter++;
			}
			System.out.println("Which campground would you like to reserve?");
			System.out.println("Please enter the Option Number");
			String aLine = theKeyboard.nextLine();

			try {
				aLine= aLine.trim();
				choice = Integer.parseInt(aLine);
				if (choice >0 && choice<=availibleSiteIds.size()) {
					getChoice= false;
				}
				else {
					System.out.println(aLine +" is not a valid option please try agian.");
				}
			}
			catch(NumberFormatException ex){
				System.out.println(aLine +" is not a valid option please try agian.");
			}
		}	
		int choosenSiteId = availibleSiteIds.get(choice-1);
		return choosenSiteId;
	}
	
	public LocalDate getUserDate() {
		Boolean getDate= true;
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
		String userDateString = "";
		LocalDate userDate = null;
		while(getDate) {
			try {
				System.out.println("Please enter the date in the as MM/dd/yyyy");
				userDateString= theKeyboard.nextLine();
				userDate = LocalDate.parse(userDateString, formatter);
				getDate=false;
			}
			catch(DateTimeParseException e){
				System.out.println("**********************************************");
				System.out.println("I'm sorry " +userDateString +" is not a date Please try again");
				System.out.println("**********************************************");

			}
		}
		return userDate;
	}
///////////////////////////////////////////////////////////////////////////////////////////////	
	public void reservereSite(int site_id, LocalDate from_date, LocalDate to_date) {
		System.out.println("What is your name?");
		String name = theKeyboard.nextLine();
		Reservation yourReservation = new Reservation(site_id, name, from_date, to_date);

		String sqlCampgroundCost ="SELECT daily_fee FROM campground JOIN site ON campground.campground_id = site.campground_id WHERE site_id = ?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlCampgroundCost,site_id);
		while (results.next()) {
			yourReservation.setDailyCost(results.getDouble("daily_fee"));
		}
		double totalCost =yourReservation.getTotalCost();
		System.out.printf("Your reservation will cost $%.2f do you want to confirm you reservation? (Y)es or (N)o",totalCost);
		String aLine = theKeyboard.nextLine();
		aLine=aLine.toUpperCase();
		if (aLine.contains("Y")){
			reservationDAO.addReservation(yourReservation);
			String sqlGetReservationId= "SELECT reservation_id FROM reservation WHERE site_id = ? and name = ? and from_date = ? and to_date = ?";
			SqlRowSet result = jdbcTemplate.queryForRowSet(sqlGetReservationId, site_id, name, from_date, to_date);
			int reservation_id =0;
			while (result.next()) {
				reservation_id= result.getInt("reservation_id");
			}
			if (reservation_id == 0) {
				System.out.println("I'm sorry your reservation has not been created please try again");
			}
			else {
				System.out.println("Congratulations! Your reservation has been created successfully.");
				yourReservation.setReservation_id(reservation_id);
				System.out.println(yourReservation.reservationConfirmation());
			}
		}
		else  {
			System.out.println("Thank you for considering our park.");
		}
	}
}
