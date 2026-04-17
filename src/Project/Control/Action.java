package Project.Control;

import java.util.ArrayList;

import Project.Entity.Combatant;
import Project.Entity.Enemy;

public interface Action {

    String getName();

    boolean isAvailable();

    void execute(Combatant user, ArrayList<Enemy> targets);
}
