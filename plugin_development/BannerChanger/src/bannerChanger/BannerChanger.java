package bannerChanger;
import api.data.*;
import api.plugin.Plugin;
import api.plugin.EventClient;
import api.service.BotServices;

/**
 * This class controls the channel control
 * 
 * @author Jochen Schupp
 *
 */
public class BannerChanger extends Plugin implements EventClient{
	String DefaultBannerURL="http://www.international-freaks.de/Images/banner_small_one.png";
	String OnClientJoinBanner="http://www.international-freaks.de/Images/banner_small_one_welcome.png";
	int default_Countdown=5;
	int Countdown=default_Countdown;

	public BannerChanger(BotServices service) {
		super ("BannerChanger", service);
	}
	
	@Override
	public EventClient[] getEventClients() {
		return new EventClient[] {this};
	}
	
	@Override
	protected void initConfig() {
		// Nothing to init here yet
	}
	
	@Override
	public void onBrainBeat(EventDTI data) {
       if(Countdown>1){
    	   Countdown -=1; 
       }
       if(Countdown<=1){
    	 //  this.service.addAction(this.service.createAction(1L).); 
    	   queryUtil.pushCustomQuery("serveredit virtualserver_hostbanner_gfx_url="+DefaultBannerURL);
    	   
       }
	}
	
	@Override
	public void onClientJoined(EventDTI data) {
		queryUtil.pushCustomQuery("serveredit virtualserver_hostbanner_gfx_url="+OnClientJoinBanner);
		Countdown=default_Countdown;
	}
	
}
