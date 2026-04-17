package Project.Control;

import Project.Entity.Combatant;

public interface TurnOrderStrategy {
	Combatant[] determineOrder(Combatant[] combatants);
}
