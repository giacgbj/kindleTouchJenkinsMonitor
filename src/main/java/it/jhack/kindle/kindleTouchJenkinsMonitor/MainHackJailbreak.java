package it.jhack.kindle.kindleTouchJenkinsMonitor;

import ixtab.jailbreak.Jailbreak;

import java.security.AllPermission;

public class MainHackJailbreak extends Jailbreak {
	
	public boolean requestPermissions() {
		final boolean ok = getContext().requestPermission(new AllPermission());
		return ok;
	}
	
}
