package no.ntnu.imt3281.ludo.logic;
/**
 * Listen for a player state in a ludo game.
 * @author Kristian
 *
 */
public interface PlayerListener {

	public void playerStateChanged(PlayerEvent event);

}
