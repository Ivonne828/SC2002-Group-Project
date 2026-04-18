package Project.Entity;

public abstract class StatusEffect {
	private int duration;
	private int cooldown;
	private String name;

	public StatusEffect(){

	}

	public String getName(){ 
		return name; 
	}

	public void setName(String name){ 
		this.name = name; 
	}

	public int getDuration(){ 
		return duration; 
	}

	public void setDuration(int d){ 
		this.duration = Math.max(0, d); 
	}

	public int getCooldown(){ 
		return cooldown; 
	}

	public void setCooldown(int c){ 
		this.cooldown = c; 
	}

	public abstract void applyStatus(Combatant user, Combatant target, int d);
	public abstract void checkStatus(Combatant user);
}
