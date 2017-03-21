package system.persistence.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.ResultSet;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import system.persistence.DBConnection;
import system.persistence.DBIO;

public class TestPersistence {
	
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void testGetConnection() {
		Connection con = DBConnection.getConnection();

		assertNotNull(con);
	}

	@Test
	public final void testExecuteStatement() {
		DBIO io = DBIO.getSharedInstance();
		
		assertTrue(io.executeStatement("SELECT * FROM sqlite_master WHERE type='table';"));
	}
	
	@Test
	public final void testExecuteQuery() {
		DBIO io = DBIO.getSharedInstance();
		ResultSet rs = io.executeQuery("SELECT * FROM TypeMeta");
		assertNotNull(rs);
		
//		System.out.println(DBIO.ResultSetToString(rs));
	}
	
	@Test
	public final void testExecuteQuery2() {
		DBIO io = DBIO.getSharedInstance();
		ResultSet rs = io.executeQuery("SELECT * FROM EntityMeta");
		assertNotNull(rs);
		
//		System.out.println(DBIO.ResultSetToString(rs));
	}
}
