package me.vowed.api.pets.ai;

import net.minecraft.server.v1_8_R3.PathfinderGoal;

/**
 * Created by JPaul on 11/23/2015.
 */
public abstract class AIGoal extends PathfinderGoal {
    public abstract boolean shouldStart();

    public boolean shouldFinish() {
        return !shouldStart();
    }

    public void start() {
    }

    public void finish() {
    }

    public void tick() {
    }
}