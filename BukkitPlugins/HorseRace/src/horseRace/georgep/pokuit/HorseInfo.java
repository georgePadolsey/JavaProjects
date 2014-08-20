package horseRace.georgep.pokuit;

import org.bukkit.entity.Horse;

public class HorseInfo {
	
	private Horse.Style HorseStyle;
	private Horse.Color HorseColor;
	
	public  HorseInfo(Horse.Style HorseStyle, Horse.Color HorseColor) {
		this.HorseStyle = HorseStyle;
		this.HorseColor = HorseColor;
	}
	
	public Horse.Color getHorseColor() {
		return this.HorseColor;
	}
	
	public Horse.Style getHorseStyle() {
		return this.HorseStyle;
	}
	
	public void setHorseColor(Horse.Color HorseColor) {
		this.HorseColor = HorseColor;
	}
	
	public void setHorseStyle(Horse.Style HorseStyle) {
		this.HorseStyle = HorseStyle;
	}
}