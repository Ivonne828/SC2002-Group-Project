package Project.Control;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import Project.Entity.Combatant;

public class RandomTurnStrategy implements TurnOrderStrategy {

    private Random random = new Random();

    @Override
	public Combatant[] determineOrder(Combatant[] combatants) {
        List<Combatant> alive = new ArrayList<>();

        for (Combatant c: combatants) {
            if (c.isAlive()) {
                alive.add(c);
            }
        }

        Collections.shuffle(alive, random);

        return alive.toArray(new Combatant[0]);
    }
}
