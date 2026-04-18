package Project.Entity;

public class PowerStone extends Items {

    public PowerStone() {
        setName("Power Stone");
    }

    @Override
    public void use(Combatant user) {
    }

    @Override
    public void use(Combatant user, Combatant[] enemies) {
        if (!(user instanceof Player)) {
            System.out.println("Power Stone can only be used by a player.");
            return;
        }

        Player player = (Player) user;
        int savedCooldown = player.getAbilityCooldown();

        player.setAbilityCooldown(0);

        if (player.requiresSingleTarget()) {
            if (enemies.length > 0) {
                player.specialSkill(enemies[0]);
                if (!enemies[0].isAlive()) {
                    System.out.println(enemies[0].getName() + " has been eliminated!");
                }
            }
        } else {
            player.specialSkill(enemies);
        }

        player.setAbilityCooldown(savedCooldown);

        System.out.println(player.getName() + " used Power Stone! Special skill triggered!");
    }

	@Override
	public boolean requiresEnemyTargets() { 
		return true; 
	}

}