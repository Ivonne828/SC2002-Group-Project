package Project.Entity;

public class StunEffect extends StatusEffect {

	public StunEffect() {
		setName("Stun");
		setDuration(0);
		setCooldown(0);
	}

	@Override
	public void applyStatus(Combatant user, Combatant target, int d) {
		System.out.println(target.getName() + " is stunned!");
		setDuration(d);
		target.getActiveStatusEffects().add(this);
	}

	@Override
	public void checkStatus(Combatant user) {
		setDuration(Math.max(0, getDuration()-1));
		if (getDuration() == 0) {
			user.getActiveStatusEffects().remove(this);
			System.out.println(user.getName() + "'s stun has worn off.");
		} else {
			System.out.println(user.getName() + " is stunned! Remaining Duration: " + getDuration() + " turn(s).");
		}
	}

}
