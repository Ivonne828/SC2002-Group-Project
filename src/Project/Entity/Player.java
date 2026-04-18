package Project.Entity;

import java.util.ArrayList;

import Project.Control.Action;

public abstract class Player extends Combatant {

    public Player(String name, int atk, int maxHp, int defense, int speed,
                  int curHp, int baseDefense, int baseAtk) {
        super(name, atk, maxHp, defense, speed, curHp, baseDefense, baseAtk);
        this.setInventory(new ArrayList<Items>());
    }

    public abstract ArrayList<Action> getActions();

    public abstract <T> void specialSkill(T enemies);

    @Override
    public boolean requiresSingleTarget() {
        return true;
    }

    @Override
    public void checkAbilityCooldown() {
        setAbilityCooldown(getAbilityCooldown() - 1);
    }
}
