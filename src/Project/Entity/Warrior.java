package Project.Entity;

import java.util.ArrayList;

import Project.Control.Action;
import Project.Control.BasicAttack;
import Project.Control.Defend;
import Project.Control.ShieldBash;
import Project.Control.UseItem;


public class Warrior extends Player {

    public Warrior() {
        super("Warrior", 40, 260, 20, 30, 260, 20, 40);
    }

    @Override
    public ArrayList<Action> getActions() {
        ArrayList<Action> actions = new ArrayList<>();
        actions.add(new BasicAttack());
        actions.add(new Defend());
        actions.add(new UseItem());
        actions.add(new ShieldBash(this)); 
        return actions;
    }

    @Override
    public boolean requiresSingleTarget() {
        return true;
    }

    @Override
    public <T> void specialSkill(T enemies) {
        ArrayList<Enemy> targets = new ArrayList<>();
        targets.add((Enemy) enemies);
        new ShieldBash(this).execute(this, targets);
    }
}
