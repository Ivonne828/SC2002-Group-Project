package Project.Control;

import java.util.ArrayList;
import java.util.Scanner;

import Project.Entity.Combatant;
import Project.Entity.Enemy;
import Project.Entity.Player;
import Project.Entity.StunEffect;


public class ShieldBash implements Action {

    private static final Scanner sc = new Scanner(System.in);

    private final Player owner;

    public ShieldBash(Player owner) {
        this.owner = owner;
    }

    @Override
    public String getName() {
        int cd = owner.getAbilityCooldown();
        return "Special Skill: Shield Bash" + (cd > 0 ? " (Cooldown: " + cd + " turn(s))" : " (Ready)");
    }

    @Override
    public boolean isAvailable() {
        return owner.getAbilityCooldown() == 0;
    }

    @Override
    public void execute(Combatant user, ArrayList<Enemy> targets) {
        if (!isAvailable()) {
            System.out.println("Shield Bash is on cooldown for " + owner.getAbilityCooldown() + " more turn(s). Choose a different action.");
            throw new IllegalStateException("ON_COOLDOWN");
        }

        Enemy target;
        if (targets.size() == 1) {
            target = targets.get(0);
        } else {
            target = chooseTarget(targets);
        }
        if (target == null) return;

        int damage = Math.max(0, user.getAttack() - target.getDefense());
        int originalHp = target.getCurrentHp();
        if (!target.isInvulnerable()) {
            target.setCurrentHp(target.getCurrentHp() - damage);
        }
        System.out.println(user.getName() + " uses Shield Bash on " + target.getName() + " for " + damage + " damage!" + " (HP: " + originalHp + " → " + target.getCurrentHp() + ")");

        StunEffect stun = new StunEffect();
        stun.applyStatus(user, target, 2);

        if (!target.isAlive()) {
            System.out.println(target.getName() + " has been eliminated!");
        }

        user.setAbilityCooldown(3);
    }

    private Enemy chooseTarget(ArrayList<Enemy> targets) {
        if (targets.isEmpty()) return null;
        System.out.println("Choose a target for Shield Bash:");
        for (int i = 0; i < targets.size(); i++) {
            System.out.println((i + 1) + ". " + targets.get(i).getName() + " (HP: " + targets.get(i).getCurrentHp() + "/" + targets.get(i).getMaxHp() + ")");
        }
        return targets.get(readValidInput(1, targets.size()) - 1);
    }

    private int readValidInput(int min, int max) {
        while (true) {
            try {
                if (sc.hasNextInt()) {
                    int v = sc.nextInt();
                    if (v >= min && v <= max) return v;
                } else {
                    sc.nextLine();
                }
                System.out.println("Enter a number from " + min + " to " + max + ".");
            } catch (Exception e) {
                sc.nextLine();
            }
        }
    }
}
