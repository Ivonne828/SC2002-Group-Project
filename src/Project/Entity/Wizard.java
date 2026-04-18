package Project.Entity;

import java.util.ArrayList;

import Project.Control.Action;
import Project.Control.ArcaneBlast;
import Project.Control.BasicAttack;
import Project.Control.Defend;
import Project.Control.UseItem;

public class Wizard extends Player {

    public Wizard() {
        super("Wizard", 50, 200, 10, 20, 200, 10, 50);
    }


    @Override
    public ArrayList<Action> getActions() {
        ArrayList<Action> actions = new ArrayList<>();
        actions.add(new BasicAttack());
        actions.add(new Defend());
        actions.add(new UseItem());
        actions.add(new ArcaneBlast(this));  
        return actions;
    }

    @Override
    public boolean requiresSingleTarget() {
        return false;
    }

    @Override
    public <T> void specialSkill(T enemies) {
        ArrayList<Enemy> targets = new ArrayList<>();

        if (enemies instanceof Combatant[]) {
            for (Combatant c : (Combatant[]) enemies) {
                if (c instanceof Enemy) {
                    targets.add((Enemy) c);
                }
            }
        }

        new ArcaneBlast(this).execute(this, targets);
    }
}
