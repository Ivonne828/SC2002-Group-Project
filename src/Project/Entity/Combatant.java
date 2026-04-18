package Project.Entity;

import java.util.ArrayList;

public abstract class Combatant {

	private int abilityCooldown = 0;
	private boolean invulnerable = false;

	private int atk, defense, speed, curHp;
	private final int maxHp, baseDefense, baseAtk;
	private final String name;
	private ArrayList<Items> inventory;
	private ArrayList<StatusEffect> activeStatusEffects;

	public Combatant(String name, int atk, int maxHp, int defense, int speed, int curHp, int baseDefense, int baseAtk) {
		this.name = name;
		this.atk = atk;
		this.maxHp = maxHp;
		this.defense = defense;
		this.speed = speed;
		this.curHp = curHp;
		this.baseDefense = baseDefense;
		this.baseAtk = baseAtk;
		this.activeStatusEffects = new ArrayList<>();
		this.inventory = new ArrayList<>();
	}

	public boolean isAlive() {
		return curHp > 0;
	}

	public boolean canAct() {
		for (StatusEffect effect: activeStatusEffects) {
			if (effect instanceof StunEffect) return false;
		}
		return true;
	}

	public int getMaxHp() {
		return maxHp;
	}

	public int getCurrentHp() {
		curHp = Math.min(maxHp, curHp);
		return Math.max(curHp, 0);
	}

	public void setCurrentHp(int hp) {
		this.curHp = Math.max(0, Math.min(hp, maxHp));
	}

	public int getAttack() {
		return atk;
	}

	public int getDefense() {
		return defense;
	}

	public int getSpeed() {
		return speed;
	}

	public int getBaseDefense() {
		return baseDefense;
	}

	public int getBaseAtk() {
		return baseAtk;
	}

	public void setAttack(int attack) {
		this.atk = attack;
	}

	public void setDefense(int defense) {
		this.defense = defense;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public int getAbilityCooldown() {
		return abilityCooldown;
	}

	public void setAbilityCooldown(int cd) {
		this.abilityCooldown = Math.max(0, cd);
	}

	public void checkAbilityCooldown() {
	}

	public boolean isInvulnerable() {
		return invulnerable;
	}

	public void setInvulnerable(boolean val) {
		this.invulnerable = val;
	}

	public String getName() {
		return name;
	}

	public ArrayList<Items> getInventory() {
		return inventory;
	}

	public void setInventory(ArrayList<Items> inv) {
		this.inventory = inv;
	}

	public void addItem(Items item) {
		inventory.add(item);
	}

	public void removeItem(Items item) {
		inventory.remove(item);
	}

	public ArrayList<StatusEffect> getActiveStatusEffects() {
		return activeStatusEffects;
	}

	public boolean requiresSingleTarget() {
		return true;
	}

	public String getStatusTag() {
		StringBuilder sb = new StringBuilder();
		for (StatusEffect e : activeStatusEffects) {
			sb.append(" [").append(e.getName().toUpperCase()).append("]");
		}
		return sb.toString();
	}

}
