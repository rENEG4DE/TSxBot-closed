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
import tsxdk.entity.TsChannel;
import tsxdk.entity.TsEntity;

public class TestTsChannelGateway {

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
		AbstractEntityGateway gateway = GatewayManager.getSharedInstance().getChannelGateway();
		TsChannel channel = new TsChannel();
		
		channel.setPid(1);
		channel.setCid(2);

		assertTrue(gateway.insertEntity(channel) != 0);	
		
		gateway.removeEntity(channel);
	}

	@Test
	public final void testGetAll() {
		AbstractEntityGateway gateway = GatewayManager.getSharedInstance().getChannelGateway();
		TsChannel channel = new TsChannel();
		
		channel.setPid(1);
		channel.setCid(2);

		gateway.insertEntity(channel);	
		
		TsChannel entity = null;
		for (TsEntity current : gateway.getAll()) {
			entity = (TsChannel) current;
		}
		
		assertNotNull("No Entity has been found", entity);
		
		gateway.removeEntity(channel);
	}
}
