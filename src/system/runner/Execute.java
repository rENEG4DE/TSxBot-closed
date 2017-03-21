package system.runner;

import java.text.SimpleDateFormat;

import system.core.Context;
import utility.log.Log;

public class Execute {
	public static void main(String[] args) {
		Log log = Context.getSharedInstance().getLog();
		log.printBlock("Starting TSxBot[" + Context.getSharedInstance().getRevString() + "] at " + new SimpleDateFormat("HH:mm:ss").format(System.currentTimeMillis()));
		new system.core.Core ();
	}
}
