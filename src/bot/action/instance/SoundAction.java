package bot.action.instance;

import bot.audio.PlayableMP3;
import bot.audio.PlayableSound;

public class SoundAction extends AbstractAction {

	final PlayableSound sound;
	
	public SoundAction(Long delay, Long expire, String whatToPlay) {
		super(delay, expire, "Play " + whatToPlay);
		sound = new PlayableMP3 (whatToPlay);
	}

	@Override
	public void execute() {
		sound.play();
	}

}
