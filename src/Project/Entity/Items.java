package Project.Entity;

public abstract class Items {

	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) { 
		this.name = name; 
	}

	public abstract void use(Combatant user);

	public void use(Combatant user, Combatant[] enemies) {
		
	}

	public boolean requiresEnemyTargets() {
		return false; 
	}

	public boolean targetsAllEnemies() {
        return false;
    }
}
