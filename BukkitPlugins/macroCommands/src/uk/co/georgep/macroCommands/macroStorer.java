package uk.co.georgep.macroCommands;

import java.util.ArrayList;

public class macroStorer {
	
	
	private ArrayList<String> macros= new ArrayList<String>();
	
	public macroStorer() {
	}
	
	public boolean contains(String s) {
		return macros.contains(s);
	}
	public void add(String s) {
		macros.add(s);
	}
	public void remove(String s) {
		macros.remove(s);
	}
	public ArrayList<String> getStorage() {
		return macros;
	}
}
