package Project.Control;

import java.util.ArrayList;

import Project.Entity.Combatant;
import Project.Entity.Enemy;
import Project.Entity.Player;

public class ArcaneBlast implements Action {

    private final Player owner;

    public ArcaneBlast(Player owner) {
        this.owner = owner;
    }

    @Override
    public String getName() {
        int cd = owner.getAbilityCooldown();
        return "Special Skill: Arcane Blast" + (cd > 0 ? " (Cooldown: " + cd + " turn(s))" : " (Ready)");
    }

    @Override
    public boolean isAvailable() {
        return owner.getAbilityCooldown() == 0;
    }

    @Override
    public void execute(Combatant user, ArrayList<Enemy> targets) {
        if (!isAvailable()) {
            System.out.println("Arcane Blast is on cooldown for " + owner.getAbilityCooldown() + " more turn(s). Choose a different action.");
            throw new IllegalStateException("ON_COOLDOWN");
        }

        System.out.println(user.getName() + " unleashes Arcane Blast on all enemies!");

        for (Enemy enemy : targets) {
            if (!enemy.isAlive()) continue;

            int damage = Math.max(0, user.getAttack() - enemy.getDefense());
            int originalHp = enemy.getCurrentHp();

            if (enemy.isInvulnerable()) {
                System.out.println("  " + enemy.getName() + " is invulnerable! No damage.");
            } else {
                enemy.setCurrentHp(enemy.getCurrentHp() - damage);
                System.out.println("  " + enemy.getName() + " takes " + damage + " damage!" + " (HP: " + originalHp + " → " + enemy.getCurrentHp() + ")");
            }

            if (!enemy.isAlive()) {
                int oldAtk = user.getAttack();
                user.setAttack(user.getAttack() + 10);
                System.out.println("  " + enemy.getName() + " eliminated! " + user.getName() + " grows stronger!" + " (Attack: " + oldAtk + " → " + user.getAttack() + ")");
            }
        }

        user.setAbilityCooldown(3);
    }
}
