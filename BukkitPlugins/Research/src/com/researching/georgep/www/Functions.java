package com.researching.georgep.www;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class Functions {
	
	
	public static Boolean isRegisteredEnchantingTable(Location loc) {
		if(Main.registeredResearchTable.containsKey(loc) && Main.registeredResearchTable.get(loc)) {
			return true;
		} else {
			return false;
		}
	}
	public static Boolean itemInHandIsA(Material m,Player s) {
		if(s.getItemInHand().getType() == m) {
			return true;
		} else {
			return false;
		}
	}
	public static Boolean tableHasAllThatIsNeeded(Location loc) {
		if(Main.registeredResearchTableCounter.containsKey(loc)&&Main.registeredResearchTableCounter.get(loc) >= 2) {
			return true;
		} else {
			return false;
		}
	}
}
