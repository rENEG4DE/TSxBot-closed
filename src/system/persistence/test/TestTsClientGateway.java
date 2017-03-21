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
import tsxdk.entity.TsClient;
import tsxdk.entity.TsEntity;

public class TestTsClientGateway {

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
			AbstractEntityGateway gateway = GatewayManager.getSharedInstance().getClientGateway();
			TsClient client = new TsClient();
			
			client.setClid(1);
			client.setCid(2);

			assertTrue("Can not insert entity", gateway.insertEntity(client) != 0);
			gateway.removeEntity(client);
		}

		@Test
		public final void testGetAll() {
			AbstractEntityGateway gateway = GatewayManager.getSharedInstance().getClientGateway();
			TsClient client = new TsClient();

			client.setClid(1);
			client.setCid(2);
			gateway.insertEntity(client);
			
			TsClient entity = null;
			for (TsEntity current : gateway.getAll()) {
				entity = (TsClient) current;
				
			}
			assertNotNull("No Entity has been found", entity);
			
			gateway.removeEntity(client);
		}
}
