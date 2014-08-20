package inPhazeWallsPlugin.georgep.pokuit;

import org.bukkit.event.EventHandler;
import org.bukkit.plugin.Plugin;

import uapi.georgep.pokuit.Events.Arena.PlayerJoinArenaEvent;
import uapi.georgep.pokuit.Objects.Arena;

public class InPhazeWallsArena extends Arena {

	public static final String type = "InPhazeWallsArena";
	
	private InPhazeWallsArenaConfig c = new InPhazeWallsArenaConfig();
	
	public InPhazeWallsArena(Plugin pl) {
		super(pl, type);
		
		InPhazeWallsPlugin.uapi.getArenaManager().registerArena(this, true);
		
	}

	@Override
	public void setup() {
		// TODO Auto-generated method stub

	}

	@Override
	public String countDownMessages(int i) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void __start() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setDisplayName(String dName) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setId(String id) {
		// TODO Auto-generated method stub

	}
	
	@EventHandler
	public void onPlayerJoinArenaEvent(PlayerJoinArenaEvent e) {
		
	}

}
