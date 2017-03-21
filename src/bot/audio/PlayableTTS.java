package bot.audio;

import com.sun.speech.freetts.VoiceManager;

public final class PlayableTTS extends PlayableSound {
	static final com.sun.speech.freetts.Voice voice = VoiceManager.getInstance().getVoice("kevin16");
	static {
		voice.allocate();
	}
	
	public PlayableTTS(String rsc) {
		super(rsc);
	}

	@Override
	public void play() {
		voice.speak(resource);
	}

}
