package Project.Entity;

public abstract class Enemy extends Combatant {

	public Enemy(String name, int atk, int max_health, int defense, int speed, int curhp, int base_defense, int base_atk) {
		super(name, atk, max_health, defense, speed, curhp, base_defense, base_atk);
	}

}
