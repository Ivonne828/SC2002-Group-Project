package Project.Entity;

public class SmokeEffect extends StatusEffect {

	public SmokeEffect() {
		setName("Smoke");
		setDuration(0);
		setCooldown(0);
	}

	@Override
	public void applyStatus(Combatant user, Combatant target, int d) {
		user.setInvulnerable(true);
		setDuration(d);
		user.getActiveStatusEffects().add(this);
		System.out.println(user.getName() + " is now invulnerable for " + d + " turn(s)!");
	}

	@Override
	public void checkStatus(Combatant user) {
		setDuration(Math.max(0, getDuration()-1));
		if (getDuration() == 0) {
			user.setInvulnerable(false);
			user.getActiveStatusEffects().remove(this);
			System.out.println(user.getName() + "'s Smoke Bomb effect has worn off.");
		} else {
			System.out.println(user.getName() + " is invulnerable for " + getDuration() + " more turn(s).");
		}
	}

}
