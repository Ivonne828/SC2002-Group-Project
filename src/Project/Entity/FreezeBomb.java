package Project.Entity;

public class FreezeBomb extends Items {
	public FreezeBomb() {
		setName("Freeze Bomb");
	}

    @Override
    public void use(Combatant user) {
        System.out.println("Freeze Bomb requires enemies to target.");
    }

    @Override
    public void use(Combatant user, Combatant[] enemies) {
        System.out.println(user.getName() + " throws a Freeze Bomb! All enemies are frozen for 1 turn!");
        for (Combatant e: enemies) {
            if (e.isAlive()) {
                new StunEffect().applyStatus(user, e, 1);
            }
        }
    }

    @Override
	public boolean requiresEnemyTargets() {
		return true; 
	}

    @Override
    public boolean targetsAllEnemies() {
        return true; 
    }
}
