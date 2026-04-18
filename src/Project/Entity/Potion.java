package Project.Entity;

public class Potion extends Items {

	public Potion() {
		setName("Potion");
	}

	@Override
	public void use(Combatant user) {
		int before = user.getCurrentHp();
		user.setCurrentHp(user.getCurrentHp() + 100);
		int healed = user.getCurrentHp() - before;
		System.out.println(user.getName() + " used Potion and restored " + healed + " HP! (Updated HP: " + before + " → " + user.getCurrentHp() + ")");
	}

}
