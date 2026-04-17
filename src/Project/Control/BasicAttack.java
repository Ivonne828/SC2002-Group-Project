package Project.Control;

import java.util.ArrayList;
import java.util.Scanner;

import Project.Entity.Combatant;
import Project.Entity.Enemy;

public class BasicAttack implements Action {

    private static final Scanner sc = new Scanner(System.in);

    @Override
    public String getName() {
        return "Basic Attack";
    }

    @Override
    public boolean isAvailable() {
        return true;
    }

    @Override
    public void execute(Combatant user, ArrayList<Enemy> targets) {
        Enemy target = chooseTarget(targets);
        if (target == null) return;

        int originalHp = target.getCurrentHp();
        int damage = Math.max(0, user.getAttack() - target.getDefense());

        if (target.isInvulnerable()) {
            System.out.println(user.getName() + " attacks " + target.getName() + " but they are invulnerable! No damage dealt.");
        } else {
            target.setCurrentHp(target.getCurrentHp() - damage);
            System.out.println(user.getName() + " attacks " + target.getName() + " for " + damage + " damage!" + " (HP: " + originalHp + " → " + target.getCurrentHp() + ")");
        }

        if (!target.isAlive()) {
            System.out.println(target.getName() + " has been eliminated!");
        }
    }

    private Enemy chooseTarget(ArrayList<Enemy> targets) {
        if (targets.isEmpty()) {
            System.out.println("No valid targets.");
            return null;
        }
        System.out.println("Choose a target:");
        for (int i = 0; i < targets.size(); i++) {
            System.out.println((i + 1) + ". " + targets.get(i).getName() + " (HP: " + targets.get(i).getCurrentHp() + "/" + targets.get(i).getMaxHp() + ")" + targets.get(i).getStatusTag());
        }
        int choice = readValidInput(1, targets.size());
        return targets.get(choice - 1);
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
