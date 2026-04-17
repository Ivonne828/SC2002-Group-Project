package Project.Boundary;

import java.util.ArrayList;
import java.util.Scanner;

import Project.Control.BattleEngine;
import Project.Control.RandomTurnStrategy;
import Project.Control.SpeedBasedStrategy;
import Project.Control.TurnOrderStrategy;
import Project.Entity.Enemy;
import Project.Entity.FreezeBomb;
import Project.Entity.Items;
import Project.Entity.Player;
import Project.Entity.Potion;
import Project.Entity.PowerStone;
import Project.Entity.SmokeBomb;
import Project.Entity.Warrior;
import Project.Entity.Wizard;

public class GameUI {

	private static final Scanner sc = new Scanner(System.in);

	public static void main(String[] args) {
		boolean running = true;
		boolean sameSettings = false;

		int savedPlayerChoice = 0;
		int savedDifficultyChoice = 0;
		int savedTurnOrderChoice = 0;
		ArrayList<String> savedItemNames = new ArrayList<>();

		while (running) {
			Player player;
			int difficultyChoice;
			int turnOrderChoice;

			if (!sameSettings) {
				System.out.println("\n———————— Welcome to the Combat Arena! ————————");

				System.out.println("\n————————  Choose Your Character ————————");
				System.out.println("1. Warrior");
				System.out.println("   HP: 260 | Attack: 40 | Defense: 20 | Speed: 30");
				System.out.println("   Special Skill: Shield Bash");
				System.out.println("   Effect: Deal Basic Attack damage to selected enemy.");
				System.out.println("           Selected enemy is stunned for current + next turn.");
				System.out.println("2. Wizard");
				System.out.println("   HP: 200 | Attack: 50 | Defense: 10 | Speed: 20");
				System.out.println("   Special Skill: Arcane Blast");
				System.out.println("   Effect: Deal Basic Attack damage to ALL enemies.");
				System.out.println("           Each kill adds +10 Attack until end of level.");

				savedPlayerChoice = readValidInput(1, 2);
				player = (savedPlayerChoice == 1) ? new Warrior() : new Wizard();

				System.out.println("\n———————— Available Items ———————— ");
				System.out.println("1. Potion      - Heal 100 HP");
				System.out.println("2. Smoke Bomb  - Enemy attacks do no damage for 2 turns");
				System.out.println("3. Power Stone - Trigger special skill once for FREE (no cooldown change)");
				System.out.println("4. Freeze Bomb - Freezes ALL enemies for one turn");

				for (int i = 1; i <= 2; i++) {
					System.out.println("\nChoose item " + i + " of 2:");
					int choice = readValidInput(1, 4);
					BattleEngine.itemSelection(player, choice);
				}

				System.out.println("\n" + player.getName() + "'s Inventory:");
				for (Items item : player.getInventory()) {
					System.out.println(" - " + item.getName());
				}

				savedItemNames.clear();
				for (Items item : player.getInventory()) {
					savedItemNames.add(item.getName());
				}

				System.out.println("\n———————— Enemy Attributes ————————");
				System.out.println("Goblin  | HP: 55  | Attack: 35 | Defense: 15 | Speed: 25");
				System.out.println("Wolf    | HP: 40  | Attack: 45 | Defense: 5  | Speed: 35");

				System.out.println("\n———————— Choose Difficulty ————————");
				System.out.println("1. Easy   | Initial Wave: 3 Goblins");
				System.out.println("2. Medium | Initial Wave: 1 Goblin + 1 Wolf   | Backup Wave: 2 Wolves");
				System.out.println("3. Hard   | Initial Wave: 2 Goblins           | Backup Wave: 1 Goblin + 2 Wolves");

				savedDifficultyChoice = readValidInput(1, 3);
				difficultyChoice = savedDifficultyChoice;

				System.out.println("\n———————— Choose Turn Order Strategy ————————");
				System.out.println("1. Speed-Based (higher speed goes first)");
				System.out.println("2. Random Order");

				savedTurnOrderChoice = readValidInput(1, 2);
				turnOrderChoice = savedTurnOrderChoice;

			} else {
				player = (savedPlayerChoice == 1) ? new Warrior() : new Wizard();
				for (String itemName : savedItemNames) {
					switch (itemName) {
						case "Potion":
							player.addItem(new Potion());
							break;
						case "Smoke Bomb":
							player.addItem(new SmokeBomb());
							break;
						case "Power Stone":
							player.addItem(new PowerStone());
							break;
						case "Freeze Bomb":
							player.addItem(new FreezeBomb());
					}
				}
				difficultyChoice = savedDifficultyChoice;
				turnOrderChoice = savedTurnOrderChoice;
				System.out.println("\nReplaying as " + player.getName() + " | Difficulty: " + difficultyChoice + " | Items: " + String.join(", ", savedItemNames));
			}

			System.out.println("\nYour Stats — HP: " + player.getMaxHp() + " | Attack: " + player.getAttack() + " | Defense: " + player.getDefense() + " | Speed: " + player.getSpeed());

			TurnOrderStrategy strategy;
			if (turnOrderChoice == 1)
				strategy = new SpeedBasedStrategy();
			else
				strategy = new RandomTurnStrategy();
			
			BattleEngine.setTurnOrderStrategy(strategy);

			ArrayList<Enemy[]> waves = BattleEngine.buildWaves(difficultyChoice);
			Enemy[] initialEnemies = waves.get(0);
			Enemy[] backupEnemies = (waves.size() > 1) ? waves.get(1) : new Enemy[0];

			BattleEngine.battle(player, initialEnemies, backupEnemies);

			System.out.println("\n———————— What would you like to do next? ————————");
			System.out.println("1. Replay with the same settings");
			System.out.println("2. Start a new game");
			System.out.println("3. Exit");

			switch (readValidInput(1, 3)) {
				case 1:
					sameSettings = true;
					break;
				case 2:
					sameSettings = false;
					break;
				case 3:
					running = false;
					break;
			}
		}

		System.out.println("\n———————— Thanks for playing! ————————");
	}

	private static int readValidInput(int min, int max) {
		while (true) {
			try {
				if (sc.hasNextInt()) {
					int value = sc.nextInt();
					if (value >= min && value <= max) return value;
				} else {
					sc.nextLine();
				}
				System.out.println("Invalid input. Please enter a number from " + min + " to " + max + ".");
			} catch (Exception e) {
				sc.nextLine();
				System.out.println("Invalid input. Try again.");
			}
		}
	}
}