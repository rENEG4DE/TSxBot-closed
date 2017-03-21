package system.resource;

import java.io.InputStreamReader;

public interface AbstractResource {
	InputStreamReader getReader ();
	boolean isAvailable ();
	String getDescription ();
	void close ();
}
