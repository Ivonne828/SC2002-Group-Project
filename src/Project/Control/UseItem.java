package Project.Control;

import java.util.ArrayList;
import java.util.Scanner;

import Project.Entity.Combatant;
import Project.Entity.Enemy;
import Project.Entity.Items;

public class UseItem implements Action {

    private static final Scanner sc = new Scanner(System.in);

    @Override
    public String getName() {
        return "Use Item";
    }

    @Override
    public boolean isAvailable() {
        return true;
    }

    @Override
    public void execute(Combatant user, ArrayList<Enemy> targets) {
        if (user.getInventory().isEmpty()) {
            System.out.println("No items in inventory. Choose a different action.");

            throw new IllegalStateException("NO_ITEM");
        }

        System.out.println("Choose an item:");
        for (int i = 0; i < user.getInventory().size(); i++) {
            System.out.println((i + 1) + ". " + user.getInventory().get(i).getName());
        }

        int itemChoice = readValidInput(1, user.getInventory().size());
        Items itemToUse = user.getInventory().get(itemChoice - 1);

        System.out.println(user.getName() + " uses " + itemToUse.getName() + ".");

        if (itemToUse.requiresEnemyTargets()) {
            if (itemToUse.targetsAllEnemies()) {
                itemToUse.use(user, targets.toArray(new Combatant[0]));
            } else if (user.requiresSingleTarget()) {
                Enemy target = chooseTarget(targets);
                if (target != null) {
                    itemToUse.use(user, new Combatant[]{ target });
                }
            } else {
                itemToUse.use(user, targets.toArray(new Combatant[0]));
            }
        } else {
            itemToUse.use(user);
        }

        user.removeItem(itemToUse);
    }

    private Enemy chooseTarget(ArrayList<Enemy> targets) {
        if (targets.isEmpty()) return null;
        System.out.println("Choose a target:");
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
