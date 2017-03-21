package bot.audio;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

public final class PlayableMP3 extends PlayableSound {

	public PlayableMP3(String rsc) {
		super(rsc);		
	}

	@Override
	public void play() {
		try {
			final InputStream is = new FileInputStream (resource);
			new Player (is).play();
		} catch (FileNotFoundException | JavaLayerException e) {
			e.printStackTrace();
		}
	}

}
