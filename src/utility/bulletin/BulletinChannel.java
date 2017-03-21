package utility.bulletin;

public class BulletinChannel {
	public Switch Debug;
	public Switch Info;
	public Switch Verbose;
	public Switch Warn;
	public Switch Error;
	
	public BulletinChannel (AdvancedGlobalBulletin superBulletin) {
		Debug = superBulletin.Debug;
		Info = superBulletin.Info;
		Verbose = superBulletin.Verbose;
		Warn = superBulletin.Warn;
		Error = superBulletin.Error;
	}

	public void disableAll() {
		Debug = Info = Verbose = Warn = Error = Switch.Disabled;		
	}
	
	public void enableAll() {
		Debug = Info = Verbose = Warn = Error = Switch.Enabled;		
	}
	
	
}