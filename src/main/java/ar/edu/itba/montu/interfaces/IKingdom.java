package ar.edu.itba.montu.interfaces;

import java.util.List;

import ar.edu.itba.montu.WarStrategy;
import ar.edu.itba.montu.abstraction.WarAgent;
import ar.edu.itba.montu.war.kingdom.KingdomStatus;

public interface IKingdom extends WarAgent {
	
	public void attachToSceneIfNotAttached(final IScene scene);
	public void enforceStrategy(final WarStrategy strategy);
	
	public KingdomStatus getCurrentStatus();
	
	public void actOnTurn(final long timeEllapsed, final IScene scene, final List<IKingdom> otherKingdoms);
}
