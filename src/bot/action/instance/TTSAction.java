package bot.action.instance;

import bot.audio.PlayableSound;
import bot.audio.PlayableTTS;

public class TTSAction extends AbstractAction {

	final PlayableSound sound;
	
	public TTSAction(Long delay, Long expire, String whatToSay) {
		super(delay, expire, "Say " + whatToSay);
		sound = new PlayableTTS (whatToSay);
	}

	@Override
	public void execute() {
		sound.play();
	}

}
