package com.techelevator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.sql.SQLException;
import java.time.LocalDate;

import javax.sql.DataSource;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import Campground.Campground;
import Campground.JDBCCampgroundDAO;
import Park.JDBCParkDAO;
import Park.Park;
import Reservation.JDBCReservationDAO;
import Reservation.Reservation;
import Site.JDBCSiteDAO;
import Site.Site;

public class DAOIntegrationTest {
	private static SingleConnectionDataSource dataSource;
	
	private JDBCParkDAO parkDAO;
	private JDBCCampgroundDAO campgroundDAO;
	private JDBCSiteDAO siteDAO;
	private JDBCReservationDAO reservationDAO;
	
	private Park millcreek;
	private Campground lantermansMill;
	private Site site1;
	private Reservation reservation1;
	
	@BeforeClass
	public static void  setupDataSource() {
		dataSource = new SingleConnectionDataSource();
		dataSource.setUrl("jdbc:postgresql://localhost:5432/campground");
		dataSource.setUsername("postgres");
		dataSource.setPassword("postgres1");
		/* The following line disables autocommit for connections
		 * returned by this DataSource. This allows us to rollback
		 * any changes after each test */
		dataSource.setAutoCommit(false);		
	}
	
	/* After all tests have finished running, this method will close the DataSource */
	@AfterClass
	public static void closeDataSource() throws SQLException {
		dataSource.destroy();
	}

	/* After each test, we rollback any changes that were made to the database so that
	 * everything is clean for the next test */
	@After
	public void rollback() throws SQLException {
		dataSource.getConnection().rollback();
	}
	/* This method provides access to the DataSource for subclasses so that
	 * they can instantiate a DAO for testing */
	protected DataSource getDataSource() {
		return dataSource;
	}
	
	@Before
	public void setup(){
		parkDAO = new JDBCParkDAO(dataSource);
		campgroundDAO = new JDBCCampgroundDAO(dataSource);
		siteDAO = new JDBCSiteDAO(dataSource);
		reservationDAO = new JDBCReservationDAO(dataSource);
		
		String sqlDeleteReservations = "DELETE FROM Reservations ";
		String sqlDeleteSites = "DELETE FROM site ";
		String sqlDeleteCampgrounds = "DELETE FROM campground";
		String sqlDeleteParks = "DELETE FROM parks";
		
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		jdbcTemplate.update(sqlDeleteReservations);
		jdbcTemplate.update(sqlDeleteSites);
		jdbcTemplate.update(sqlDeleteCampgrounds);
		jdbcTemplate.update(sqlDeleteParks);
		
		millcreek.setName("Millcreek");
		millcreek.setLocation("Youngstown, Ohio");
		millcreek.setEstablish_date(LocalDate.of(1891,1,1));
		millcreek.setArea(4500);
		millcreek.setVisitors(25031);
		millcreek.setDescription("The trust for Public Land ranks one part of Mill Creek as the 142nd largest park located within the limits of a US city.");
		lantermansMill.setName("Lanterman's Mill");
		lantermansMill.setOpen_from_mm(5);
		lantermansMill.setOpen_to_mm(10);
		lantermansMill.setDaily_fee(25);
		site1.setMax_occupancy(75);
		site1.setSite_number(1);
		site1.setAccessible(true);
		site1.setMax_rv_length(0);
		site1.setUtilities(true);
		reservation1.setName("Kassie Militello");
		reservation1.setFrom_date(LocalDate.of(2019,9,16));
		reservation1.setTo_date(LocalDate.of(2019,12,20));
		reservation1.setCreate_date(LocalDate.of(2019,2,24));
		
		parkDAO.addPark(millcreek);
		SqlRowSet result = jdbcTemplate.queryForRowSet("SELECT park_id FROM park WHERE name =  'Millcreek' ");
		int park_id = result.getInt("park_id");
		lantermansMill.setPark_id(park_id);
		campgroundDAO.addCampgroundDAO(lantermansMill);
		result = jdbcTemplate.queryForRowSet("SELECT campground_id FROM campground WHERE name =  'Lanterman's Mill' ");
		int campground_id = result.getInt("campground_id");
		site1.setCampground_id(campground_id);
		siteDAO.addSite(site1);
		result = jdbcTemplate.queryForRowSet("SELECT site_id FROM site WHERE name = campground_id = ? and max_occupancy = 75", campground_id);
		int site_id = result.getInt("site_id");
		reservation1.setSite_id(site_id);
		reservationDAO.addReservation(reservation1);
	}
	@Test
	public void save_and_read_park() {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		SqlRowSet result = jdbcTemplate.queryForRowSet("SELECT campground_id FROM campground WHERE name =  'Millcreek' ");
		int park_id = result.getInt("park_id");
		millcreek.setPark_id(park_id);
		Park savedPark = parkDAO.getParkById(park_id);
		assertNotEquals("Park was not saved", null, millcreek.getPark_id());
		assertParksAreEqual(millcreek, savedPark);
	}
	@Test
	public void save_and_read_campground() {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		SqlRowSet result = jdbcTemplate.queryForRowSet("SELECT park_id FROM park WHERE name =  'Millcreek' ");
		int park_id = result.getInt("park_id");
		lantermansMill.setPark_id(park_id);
		result = jdbcTemplate.queryForRowSet("SELECT campground_id FROM campground WHERE name =  'Lanterman's Mill' ");
		int campground_id = result.getInt("campground_id");
		lantermansMill.setCampground_id(campground_id);
		Campground savedCampground = campgroundDAO.getCampgroundByCampgroundId(campground_id);
		assertNotEquals("Campground was not saved", null, lantermansMill.getCampground_id());
		assertCampgroundsAreEqual(lantermansMill, savedCampground);
	}
	@Test 
	public void save_and_read_site() {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		SqlRowSet result = jdbcTemplate.queryForRowSet("SELECT campground_id FROM campground WHERE name =  'Lanterman's Mill' ");
		int campground_id = result.getInt("campground_id");
		site1.setCampground_id(campground_id);
		result = jdbcTemplate.queryForRowSet("SELECT site_id FROM site WHERE campground_id = ? and site_number = 1 ", campground_id);
		int site_id = result.getInt("site_id");
		site1.setSite_id(site_id);
		Site savedSite = siteDAO.getSiteById(site_id);	
		assertNotEquals("Sites was not saved", null, site1.getSite_id());
		assertSitesAreEqual(site1, savedSite);
	}
	@Test
	public void save_and_read_reservation() {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		SqlRowSet result = jdbcTemplate.queryForRowSet("SELECT campground_id FROM campground WHERE name =  'Lanterman's Mill' ");
		int campground_id = result.getInt("campground_id");
		result = jdbcTemplate.queryForRowSet("SELECT site_id FROM site WHERE campground_id = ? and site_number = 1 ", campground_id);
		int site_id = result.getInt("site_id");
		reservation1.setSite_id(site_id);
		result = jdbcTemplate.queryForRowSet("SELECT reservation_id FROM reservation WHERE name = 'Kassie Militello'");
		int reservation_id = result.getInt("reservation_id");
		reservation1.setReservation_id(reservation_id);
		Reservation savedReservation = reservationDAO.getReservationByReservationId(reservation_id);
		assertNotEquals("Reservation was not saved", null, reservation1);
		assertReservationsAreEqual(reservation1, savedReservation);
		
	}
	
	public void assertParksAreEqual(Park expected, Park actual) {
		assertEquals("park_ids do not match", expected.getPark_id(), actual.getPark_id());
		assertEquals("park names do not match", expected.getName(), actual.getName());
		assertEquals("park locations do not match", expected.getLocation(), actual.getLocation());
		assertEquals("park establish_dates do not match", expected.getEstablish_date(), actual.getEstablish_date());
		assertEquals("park areas do not match", expected.getArea(), actual.getArea());
		assertEquals("park visitors do not match", expected.getVisitors(), actual.getVisitors());
		assertEquals("park descriptions do not match", expected.getDescription(), actual.getDescription());
	}
	public void assertCampgroundsAreEqual(Campground expected, Campground actual) {
		assertEquals("campground_ids do not match", expected.getCampground_id(), actual.getCampground_id());
		assertEquals("park_ids do not match", expected.getPark_id(), actual.getPark_id());
		assertEquals("campground names do not match", expected.getName(), actual.getName());
		assertEquals("Open from dates do not match", expected.getOpen_from_mm(), actual.getOpen_from_mm());
		assertEquals("Open to dates do not match", expected.getOpen_to_mm(), actual.getOpen_to_mm());
		assertEquals("daily_fees do not match", expected.getDaily_fee(), actual.getDaily_fee(), .005);
		
	}
	public void assertSitesAreEqual(Site expected, Site actual) {
		assertEquals("site_ids do not match", expected.getSite_id(), actual.getSite_id());
		assertEquals("campground_ids do not match", expected.getCampground_id(), actual.getCampground_id());
		assertEquals("site_numbers do not match", expected.getSite_number(), actual.getSite_number());
		assertEquals("max_occupancys do not match", expected.getMax_occupancy(), actual.getMax_occupancy());
		assertEquals("accessibile does not match", expected.isAccessible(),actual.isAccessible());
		assertEquals("max_rv_lengths do not match", expected.getMax_rv_length(), actual.getMax_rv_length());
		assertEquals("utilites do not match", expected.isUtilities(), actual.isUtilities());
	}
	public void assertReservationsAreEqual (Reservation expected, Reservation actual) {
		assertEquals("reservation_ids do not match", expected.getReservation_id(), actual.getReservation_id());
		assertEquals("site_ids do not match", expected.getSite_id(), actual.getReservation_id());
		assertEquals("names do not match", expected.getName(), actual.getName());
		assertEquals("from_dates do not match", expected.getFrom_date(), actual.getFrom_date());
		assertEquals("to_dates do not match", expected.getFrom_date(), actual.getFrom_date());
		assertEquals("create_dates do not match", expected.getCreate_date(), actual.getCreate_date());
	}
}
