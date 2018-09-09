package ar.edu.itba.montu.interfaces;

import ar.edu.itba.montu.WarStrategy;
import ar.edu.itba.montu.abstraction.WarAgent;

public interface IKingdom extends WarAgent {
	
	public void attachToSceneIfNotAttached(final IScene scene);
	public void enforceStrategy(final WarStrategy strategy);
}
