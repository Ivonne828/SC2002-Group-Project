package Project.Entity;

public class DefenseEffect extends StatusEffect {

	public DefenseEffect() {
		setName("Defense");
		setDuration(0);
		setCooldown(0);
	}

	@Override
	public void applyStatus(Combatant user, Combatant target, int d) {
		setDuration(d);
		user.setDefense(user.getDefense() + 10);
		user.getActiveStatusEffects().add(this);
		System.out.println(user.getName() + " takes up a defensive stance! Defense: " + user.getDefense() + " (+10) for " + getDuration() + " turn(s).");
	}

	@Override
	public void checkStatus(Combatant user) {
		setDuration(Math.max(0, getDuration()-1));
		if (getDuration() == 0) {
			user.setDefense(user.getDefense() - 10);
			user.getActiveStatusEffects().remove(this);
			System.out.println(user.getName() + "'s Defense boost has worn off. Defense: " + user.getDefense());
		} else {
			System.out.println(user.getName() + "'s Defense boost active for " + getDuration() + " more turn(s). Defense: " + user.getDefense());
		}
	}

}
