package ar.edu.itba.montu.abstraction;

import java.util.Objects;
import java.util.UUID;

public abstract class Agent {

	private UUID uid;

	public Agent() {
		uid = UUID.randomUUID();
	}

	public UUID uid() {
		return uid;
	}

	abstract public void tick(final long timeElapsed);

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Agent agent = (Agent) o;
		return Objects.equals(uid, agent.uid);
	}

	@Override
	public int hashCode() {
		return Objects.hash(uid);
	}
}
