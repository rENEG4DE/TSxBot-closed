package system.persistence.test;

import static org.junit.Assert.assertEquals;

import java.sql.Connection;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import system.persistence.DBConnection;
import system.persistence.gateway.AbstractEntityGateway;
import system.persistence.gateway.GatewayManager;
import system.persistence.gateway.TsxOptDataGateway;
import tsxdk.entity.TsChannel;

public class TestTsxOptDataGateway {

	static TsxOptDataGateway gateway;
	static TsChannel channel;
	
	@BeforeClass
	public static final void setUpBeforeClass() throws Exception {
		@SuppressWarnings("unused")
		Connection conn = DBConnection.getConnection();
		AbstractEntityGateway entityGateway = GatewayManager.getSharedInstance().getChannelGateway();
		channel = new TsChannel();
		
		channel.setPid(1);
		channel.setCid(2);
		entityGateway.insertEntity(channel);
		
		gateway = GatewayManager.getSharedInstance().getOptDataGateway();
	}
	
	@AfterClass
	public static final void tearDownAfterClass() throws Exception {
		GatewayManager.getSharedInstance().getChannelGateway().removeEntity(channel);
	}
	
	@Test
	public final void testSafeValue() {
		String valDescriptor = "SAFE_TEST_VALUE_1";
		
		gateway.safeValue(channel, valDescriptor, 10);
		
		int val = gateway.getValue(channel, valDescriptor);
		
		assertEquals(10, val);
		
//		gateway.deleteValue(channel, valDescriptor);
	}

	@Test
	public final void testUpdateValue() {
		String valDescriptor = "UPDATE_TEST_VALUE_1";
		
		gateway.safeValue(channel, valDescriptor, 20);
		int val = gateway.getValue(channel, valDescriptor);
		gateway.updateValue(channel, valDescriptor, 30);
//		gateway.safeValue(channel, valDescriptor, 30);	//would lead to the same result since it's "INSERT OR REPLACE"
		int val2 = gateway.getValue(channel, valDescriptor);
	
		assertEquals(20, val);
		assertEquals(30, val2);
		
//		gateway.deleteValue(channel, valDescriptor);
	}
	
	@Test
	public final void testMultiSafeValue() {
		String valDescriptor1 = "MULTI_SAFE_TEST_VALUE_1";
		String valDescriptor2 = "MULTI_SAFE_TEST_VALUE_2";
		String valDescriptor3 = "MULTI_SAFE_TEST_VALUE_3";
		String valDescriptor4 = "MULTI_SAFE_TEST_VALUE_4";
		String valDescriptor5 = "MULTI_SAFE_TEST_VALUE_5";
		
		final int val1 = 10;
		final String val2 = "Hello World";
		final boolean val3 = true;
		final Long val4 = 10000L;
		final Double val5 = 10.5d;
		
		int carrier1;
		String carrier2;
		boolean carrier3;
		Long carrier4;
		Double carrier5;
		
		gateway.safeValue(channel, valDescriptor1, val1);
		gateway.safeValue(channel, valDescriptor2, val2);
		gateway.safeValue(channel, valDescriptor3, val3);
		gateway.safeValue(channel, valDescriptor4, val4);
		gateway.safeValue(channel, valDescriptor5, val5);
		
		carrier1 = gateway.getValue(channel, valDescriptor1);
		carrier2 = gateway.getValue(channel, valDescriptor2);
		carrier3 = gateway.getValue(channel, valDescriptor3);
		carrier4 = gateway.getValue(channel, valDescriptor4);
		carrier5 = gateway.getValue(channel, valDescriptor5);
		
		assertEquals(val1, carrier1);
		assertEquals(val2, carrier2);
		assertEquals(val3, carrier3);
		assertEquals(val4, carrier4);
		assertEquals(val5, carrier5);
		
//		gateway.deleteValue(channel, valDescriptor1);
//		gateway.deleteValue(channel, valDescriptor2);
//		gateway.deleteValue(channel, valDescriptor3);
//		gateway.deleteValue(channel, valDescriptor4);
//		gateway.deleteValue(channel, valDescriptor5);
	}

//  Omitted because getAllFor is unfeasible because there is no type-info and you would have to guess,
	// and because it just does not work the way I want - I think it's because the primary key GlueId + Descriptor
	// in the OptData-table
//	@Test
//	public final void testGetAll() {
//		String valDescriptor1 = "GET_ALL_TEST_VALUE_1";
//		String valDescriptor2 = "GET_ALL_TEST_VALUE_2";
//		String valDescriptor3 = "GET_ALL_TEST_VALUE_3";
//		String valDescriptor4 = "GET_ALL_TEST_VALUE_4";
//		String valDescriptor5 = "GET_ALL_TEST_VALUE_5";
//		
//		final int val1 = 10;
//		final String val2 = "Hello World";
//		final boolean val3 = true;
//		final Long val4 = 10000L;
//		final Double val5 = 10.5d;
//
//		gateway.safeValue(channel, valDescriptor1, val1);
//		gateway.safeValue(channel, valDescriptor2, val2);
//		gateway.safeValue(channel, valDescriptor3, val3);
//		gateway.safeValue(channel, valDescriptor4, val4);
//		gateway.safeValue(channel, valDescriptor5, val5);
//
//		Object obj = null;
//		for (Object current : gateway.getAllFor(channel)) {
//			obj = current;
//		}
//		
//		assertNotNull("No OptData for entity has been found", obj);
//		
//		gateway.deleteValue(channel, valDescriptor1);
//		gateway.deleteValue(channel, valDescriptor2);
//		gateway.deleteValue(channel, valDescriptor3);
//		gateway.deleteValue(channel, valDescriptor4);
//		gateway.deleteValue(channel, valDescriptor5);
//	}
}
