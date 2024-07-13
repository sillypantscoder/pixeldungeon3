package com.sillypantscoder.pixeldungeon3.entity.type;

import java.util.ArrayList;

import com.sillypantscoder.pixeldungeon3.Game;
import com.sillypantscoder.pixeldungeon3.Random;
import com.sillypantscoder.pixeldungeon3.entity.Action;
import com.sillypantscoder.pixeldungeon3.entity.Entity;
import com.sillypantscoder.pixeldungeon3.entity.LivingEntity;
import com.sillypantscoder.pixeldungeon3.entity.LivingEntity.EntityCreator;

public class EnemySpawner extends Entity {
	public EnemySpawner(Game game) {
		super(game);
	}
	public void requestAction() {
		this.setAction(new Action<EnemySpawner>(this) {
			public void initiate() {
				// Spawn
				target.spawnEntity();
				// Continue
				target.time += 100;
				game.canContinue = true;
			}
			public void onTick() {}
			public boolean canBeRemoved() { return true; }
		});
	}
	public void spawnEntity() {
		ArrayList<EntityCreator<LivingEntity>> types = new ArrayList<EntityCreator<LivingEntity>>();
		types.add(Rat::new);
		EntityCreator<LivingEntity> type = Random.choice(types);
		game.spawnPos(type);
	}
}
