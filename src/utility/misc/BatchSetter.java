package utility.misc;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public final class BatchSetter {
	final PreparedStatement statement;
	int i;
	public BatchSetter(PreparedStatement statement) {
		this.statement = statement;
		i = 1;
	}
	
	public void injectArgs(Object... args) {
		for (Object arg : args) {
			setVal(arg);
			i++;
		}
	}

	private void setVal(Object arg) {
		try {
		if (arg instanceof Integer) {
			statement.setInt(i, (Integer)arg);
		} else if (arg instanceof String) {
			statement.setString(i, (String)arg);
		} else if (arg instanceof Long) {
			statement.setLong(i, (Long)arg);
		} else if (arg instanceof Double) {
			statement.setDouble(i, (Double)arg);
		} else if (arg instanceof Boolean) {
			statement.setBoolean(i, (Boolean)arg);
		} else {
			throw new UnsupportedOperationException("No valid type found for " + arg.getClass().toString());
		}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
