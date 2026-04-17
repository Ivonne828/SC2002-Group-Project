package Project.Control;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import Project.Entity.Combatant;
import Project.Entity.Enemy;
import Project.Entity.FreezeBomb;
import Project.Entity.Goblin;
import Project.Entity.Items;
import Project.Entity.Player;
import Project.Entity.Potion;
import Project.Entity.PowerStone;
import Project.Entity.SmokeBomb;
import Project.Entity.StatusEffect;
import Project.Entity.Wolf;

public class BattleEngine {

    private static final Scanner sc = new Scanner(System.in);

    private static TurnOrderStrategy turnOrderStrategy;

    public static void setTurnOrderStrategy(TurnOrderStrategy strategy) {
        turnOrderStrategy = strategy;
    }

    public static void itemSelection(Player player, int choice) {
        switch (choice) {
            case 1: player.addItem(new Potion());     System.out.println("Potion added.");      break;
            case 2: player.addItem(new SmokeBomb());  System.out.println("Smoke Bomb added.");  break;
            case 3: player.addItem(new PowerStone()); System.out.println("Power Stone added."); break;
            case 4: player.addItem(new FreezeBomb()); System.out.println("Freeze Bomb added."); break;
        }
    }

    public static void battle(Player player, Enemy[] initialEnemies, Enemy[] backupEnemies) {
        int round = 1;
        boolean backupSpawned = (backupEnemies == null || backupEnemies.length == 0);
        ArrayList<Enemy> activeEnemies = new ArrayList<>(Arrays.asList(initialEnemies));

        do {
            System.out.println("\n—————— Round " + round + " ——————");
            displayBattleState(player, activeEnemies);

            ArrayList<Combatant> alive = new ArrayList<>();
            if (player.isAlive()) alive.add(player);
            for (Enemy e : activeEnemies) if (e.isAlive()) alive.add(e);

            Combatant[] turnOrder = turnOrderStrategy.determineOrder(alive.toArray(new Combatant[0]));

            System.out.print("Turn Order: ");
            for (int i = 0; i < turnOrder.length; i++) {
                System.out.print(turnOrder[i].getName());
                if (i < turnOrder.length - 1) System.out.print(" → ");
            }
            System.out.println();

            checkStatusEffects(turnOrder);

            for (Combatant combatant: turnOrder) {
                if (endCondition(player, activeEnemies)) break;
                if (!combatant.isAlive()) continue;

                if (combatant instanceof Player) {
                    handlePlayerTurn(player, activeEnemies);
                    player.checkAbilityCooldown();
                } else {
                    if (!combatant.canAct()) {
                        System.out.println(combatant.getName() + " is stunned and skips their turn.");
                        continue;
                    }
                    int damage = Math.max(0, combatant.getAttack() - player.getDefense());
                    if (player.isInvulnerable()) {
                        System.out.println(combatant.getName() + " attacks " + player.getName() + " but they are invulnerable! No damage.");
                    } else {
                        int originalHp = player.getCurrentHp();
                        player.setCurrentHp(player.getCurrentHp() - damage);
                        System.out.println(combatant.getName() + " attacks " + player.getName() + " for " + damage + " damage!" + " (HP: " + originalHp + " → " + player.getCurrentHp() + ")");
                    }
                    if (!player.isAlive()) break;
                }
            }

            if (!backupSpawned && activeEnemies.stream().noneMatch(Enemy::isAlive)) {
                System.out.println("\nInitial wave defeated, backup enemies have spawned!");
                for (Enemy e: backupEnemies) {
                    activeEnemies.add(e);
                    System.out.println("  " + e.getName() + " has entered the battle!");
                }
                backupSpawned = true;
            }

            displayEndOfRound(player, activeEnemies, round);
            round++;

        } while (!endCondition(player, activeEnemies));

        if (!player.isAlive()) {
            long remaining = activeEnemies.stream().filter(Enemy::isAlive).count();
            System.out.println("\n—————— DEFEAT ——————");
            System.out.println("Defeated. Don't give up, try again!");
            System.out.println("Enemies Remaining: " + remaining + " | Total Rounds Survived: " + (round - 1));
        } else {
            System.out.println("\n—————— VICTORY ——————");
            System.out.println("Congratulations, you won!");
            System.out.println("Remaining HP: " + player.getCurrentHp() + "/" + player.getMaxHp() + " | Total Rounds: " + (round - 1));
            if (player.getInventory().isEmpty()) {
                System.out.println("Remaining Items: None");
            } else {
                StringBuilder items = new StringBuilder("Remaining Items: ");
                for (int i = 0; i < player.getInventory().size(); i++) {
                    items.append(player.getInventory().get(i).getName());
                    if (i < player.getInventory().size() - 1) items.append(", ");
                }
                System.out.println(items);
            }
        }
    }

    static void handlePlayerTurn(Player player, ArrayList<Enemy> activeEnemies) {
        ArrayList<Enemy> aliveEnemies = new ArrayList<>();
        for (Enemy e : activeEnemies) if (e.isAlive()) aliveEnemies.add(e);

        while (true) {
            System.out.println("\n—————— " + player.getName() + "'s Turn ——————");

            ArrayList<Action> actions = player.getActions();
            for (int i = 0; i < actions.size(); i++) {
                System.out.println((i + 1) + ". " + actions.get(i).getName());
            }

            int choice = readValidInput(1, actions.size());
            Action chosen = actions.get(choice - 1);

            try {
                chosen.execute(player, aliveEnemies);
                return; 
            } catch (IllegalStateException e) {
                System.out.println("Please choose a different action.");
            }
        }
    }

    public static boolean endCondition(Player player, ArrayList<Enemy> activeEnemies) {
        if (!player.isAlive()) return true;
        return activeEnemies.stream().noneMatch(Enemy::isAlive);
    }

    public static void checkStatusEffects(Combatant[] combatants) {
        for (Combatant combatant : combatants) {
            ArrayList<StatusEffect> effects = combatant.getActiveStatusEffects();
            for (int i = effects.size() - 1; i >= 0; i--) {
                effects.get(i).checkStatus(combatant);
            }
        }
    }

    private static void displayBattleState(Player player, ArrayList<Enemy> activeEnemies) {
        System.out.println("\nPlayer: " + player.getName() + " | HP: " + player.getCurrentHp() + "/" + player.getMaxHp() + " | Attack: " + player.getAttack() + " | Defense: " + player.getDefense() + " | Speed: " + player.getSpeed() + " | Skill Cooldown: " + player.getAbilityCooldown() + " turn(s)");

        ArrayList<Items> inv = player.getInventory();
        if (!inv.isEmpty()) {
            System.out.print("Items: ");
            for (int i = 0; i < inv.size(); i++) {
                System.out.print(inv.get(i).getName());
                if (i < inv.size() - 1) System.out.print(", ");
            }
            System.out.println();
        } else {
            System.out.println("Items: None");
        }

        System.out.println("Enemies:");
        for (Enemy e : activeEnemies) {
            if (e.isAlive()) {
                System.out.println("  - " + e.getName() + " | HP: " + e.getCurrentHp() + "/" + e.getMaxHp() + e.getStatusTag());
            }
        }
    }

    private static void displayEndOfRound(Player player, ArrayList<Enemy> activeEnemies, int round) {
        System.out.print("\nEnd of Round " + round + ": " + player.getName() + " HP: " + player.getCurrentHp() + "/" + player.getMaxHp());
        for (Enemy e : activeEnemies) {
            System.out.print(" | " + e.getName() + " HP: ");
            System.out.print(e.isAlive() ? (e.getCurrentHp() + e.getStatusTag()) : "X");
        }
        System.out.print(" | Items: ");
        if (player.getInventory().isEmpty()) {
            System.out.print("None");
        } else {
            for (int i = 0; i < player.getInventory().size(); i++) {
                System.out.print(player.getInventory().get(i).getName());
                if (i < player.getInventory().size() - 1) System.out.print(", ");
            }
        }
        System.out.println(" | Skill CD: " + player.getAbilityCooldown() + " round(s)");
    }

    public static ArrayList<Enemy[]> buildWaves(int difficultyChoice) {
        ArrayList<Enemy[]> waves = new ArrayList<>();
        switch (difficultyChoice) {
            case 1:
                waves.add(new Enemy[]{ new Goblin("Goblin A"), new Goblin("Goblin B"), new Goblin("Goblin C") });
                waves.add(new Enemy[0]);
                break;
            case 2:
                waves.add(new Enemy[]{ new Goblin("Goblin"), new Wolf("Wolf") });
                waves.add(new Enemy[]{ new Wolf("Wolf A"), new Wolf("Wolf B") });
                break;
            case 3:
                waves.add(new Enemy[]{ new Goblin("Goblin A"), new Goblin("Goblin B") });
                waves.add(new Enemy[]{ new Goblin("Goblin C"), new Wolf("Wolf A"), new Wolf("Wolf B") });
                break;
            default:
                waves.add(new Enemy[]{ new Goblin("Goblin A"), new Goblin("Goblin B"), new Goblin("Goblin C") });
                waves.add(new Enemy[0]);
        }
        return waves;
    }

    private static int readValidInput(int min, int max) {
        while (true) {
            try {
                if (sc.hasNextInt()) {
                    int v = sc.nextInt();
                    if (v >= min && v <= max) return v;
                } else {
                    sc.nextLine();
                }
                System.out.println("Invalid input. Enter a number from " + min + " to " + max + ".");
            } catch (Exception e) {
                sc.nextLine();
            }
        }
    }
}
