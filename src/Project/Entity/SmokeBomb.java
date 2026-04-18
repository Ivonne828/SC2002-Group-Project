package Project.Entity;

public class SmokeBomb extends Items {

	public SmokeBomb() {
		setName("Smoke Bomb");
	}

	@Override
	public void use(Combatant user) {
		SmokeEffect smoke = new SmokeEffect();
		smoke.applyStatus(user, null, 2);
	}

}
