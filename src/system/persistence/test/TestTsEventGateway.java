package system.persistence.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import system.persistence.DBConnection;
import system.persistence.DBIO;
import system.persistence.gateway.GatewayManager;
import system.persistence.gateway.TsEventGateway;
import tsxdk.entity.LibTsEvent;

public class TestTsEventGateway {

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
	public final void testInsertEntity() {
		@SuppressWarnings("unused")
		DBIO io = DBIO.getSharedInstance();
		TsEventGateway gateway = GatewayManager.getSharedInstance()
				.getEventGateway();
		LibTsEvent event = LibTsEvent.CLIENTLEFT;

		assertTrue("Can not insert entity", gateway.insertEntity(event) != 0);

		gateway.removeEntity(event);
	}
}
