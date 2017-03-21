package system.persistence.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import system.persistence.DBConnection;
import system.persistence.DBIO;
import system.persistence.gateway.AbstractEntityGateway;
import system.persistence.gateway.GatewayManager;
import tsxdk.entity.TsComplain;
import tsxdk.entity.TsEntity;

public class TestTsComplainGateway {

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
		AbstractEntityGateway gateway = GatewayManager.getSharedInstance().getComplainGateway();
		TsComplain complain = new TsComplain();
		
		complain.setTcldbid(1);
		complain.setTname("tname");
		complain.setFcldbid(2);
		complain.setFname("fname");
		complain.setMessage("TName molested FName");
		complain.setTimestamp(System.currentTimeMillis());
		
		assertTrue(gateway.insertEntity(complain) != 0);
		
		gateway.removeEntity(complain);
	}

	@Test
	public final void testGetAll() {
		AbstractEntityGateway gateway = GatewayManager.getSharedInstance().getComplainGateway();
		TsComplain complain = new TsComplain();
		
		complain.setTcldbid(1);
		complain.setTname("tname");
		complain.setFcldbid(2);
		complain.setFname("fname");
		complain.setMessage("TName molested FName");
		complain.setTimestamp(System.currentTimeMillis());
		
		assertTrue(gateway.insertEntity(complain) != 0);
		
		TsComplain entity = null;
		for (TsEntity current : gateway.getAll()) {
			entity = (TsComplain) current;
		}
		assertTrue(entity != null);
		
		gateway.removeEntity(complain);
	}
}
