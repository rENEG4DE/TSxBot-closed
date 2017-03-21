package bot.audio;

public abstract class PlayableSound {
	protected final String resource;
	
	PlayableSound (String rsc) {
		resource = rsc;
	}
	
	public abstract void play ();
}
