package ar.edu.itba.montu.abstraction;

import java.util.UUID;

public abstract class Agent {

	private UUID uid;

	public Agent() {
		uid = UUID.randomUUID();
	}

	public UUID getUid() {
		return uid;
	}

	abstract public void tick(final long timeElapsed);
}
