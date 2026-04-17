package Project.Control;

import java.util.Arrays;
import java.util.Comparator;

import Project.Entity.Combatant;

public class SpeedBasedStrategy implements TurnOrderStrategy {

	@Override
	public Combatant[] determineOrder(Combatant[] combatants) {
		Combatant[] ordered = combatants.clone();
		Arrays.sort(ordered, Comparator.comparingInt(Combatant::getSpeed).reversed());
		return ordered;
	}

}
