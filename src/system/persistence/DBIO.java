package system.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class DBIO {
	private final static class SingletonHolder {
		private final static DBIO INSTANCE = new DBIO();
	}

	private Statement statement; 
	private final Map<String, PreparedStatement> preparedStatements = new HashMap<>();
	private final Connection dbConn;
	
	private DBIO () {
		statement = null;
		dbConn = DBConnection.getConnection();
	}
	
	public static DBIO getSharedInstance () {
		return SingletonHolder.INSTANCE;
	}
	
	public ResultSet executeQuery (String statement) {
		try {
			validateCachedStatement();
			return this.statement.executeQuery(statement);
		} catch (final SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	//Do not make this available in API !
	public boolean executeStatement(String statement) {
		try {
			validateCachedStatement();
			return this.statement.execute(statement);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	public static final String resultSetToString (ResultSet set) {
		StringBuilder builder = new StringBuilder("ResultSet:{\n");
		
		try {
			builder.append("\t");
			for (int i = 1; set.getMetaData().getColumnCount() != i - 1; ++i)
				builder.append(set.getMetaData().getColumnName(i) + " ");
			
			builder.append("\n");
			while (set.next()) {
				builder.append("\t");
				for (int i = 1; set.getMetaData().getColumnCount() != i - 1; ++i)
					builder.append(set.getString(i) + " ");
				builder.append("\n");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		builder.append("}");
		
		return builder.toString();
	}
	
	private final void validateCachedStatement () {
		if (statement == null) {
			try {
				statement = dbConn.createStatement();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public PreparedStatement prepareStatement(String name, String content) {
		try {
			PreparedStatement statement = dbConn.prepareStatement(content);
			preparedStatements.put(name.toUpperCase(),statement);
			return statement;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public final ResultSet executeQuery(PreparedStatement stmt) {
		try {
			return stmt.executeQuery();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public final boolean executeStatement(PreparedStatement stmt) {
		try {
			return stmt.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	public PreparedStatement getStatement(String name) {
		return preparedStatements.get(name.toUpperCase());
	}
}
