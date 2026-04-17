package Project.Control;

import java.util.ArrayList;

import Project.Entity.Combatant;
import Project.Entity.DefenseEffect;
import Project.Entity.Enemy;


public class Defend implements Action {

    @Override
    public String getName() {
        return "Defend";
    }

    @Override
    public boolean isAvailable() {
        return true;
    }

    @Override
    public void execute(Combatant user, ArrayList<Enemy> targets) {
        DefenseEffect d = new DefenseEffect();
        d.applyStatus(user, null, 2);
    }
}
