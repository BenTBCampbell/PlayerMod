package com.playermod.entity.ai;

import net.minecraft.entity.ai.EntityAIBase;

public class EntityAIPlayerMob extends EntityAIBase {

	// /**
	// * A bitmask telling which other tasks may not run concurrently. The test
	// is a simple bitwise AND - if it yields
	// * zero, the two tasks may run concurrently, if not - they must run
	// exclusively from each other.
	// */
	// private int mutexBits;
	// private static final String __OBFID = "CL_00001587";

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	@Override
	public boolean shouldExecute() {
		return false;
	}

	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	@Override
	public boolean continueExecuting() {
		return this.shouldExecute();
	}

	/**
	 * Determine if this AI Task is interruptible by a higher (= lower value)
	 * priority task. All vanilla AITask have this value set to true.
	 */
	@Override
	public boolean isInterruptible() {
		return true;
	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	@Override
	public void startExecuting() {
	}

	/**
	 * Resets the task
	 */
	@Override
	public void resetTask() {
	}

	/**
	 * Updates the task
	 */
	@Override
	public void updateTask() {
	}

	// /**
	// * Sets a bitmask telling which other tasks may not run concurrently. The
	// test is a simple bitwise AND - if it
	// * yields zero, the two tasks may run concurrently, if not - they must run
	// exclusively from each other.
	// */
	// public void setMutexBits(int p_75248_1_)
	// {
	// this.mutexBits = p_75248_1_;
	// }
	//
	// /**
	// * Get a bitmask telling which other tasks may not run concurrently. The
	// test is a simple bitwise AND - if it yields
	// * zero, the two tasks may run concurrently, if not - they must run
	// exclusively from each other.
	// */
	// public int getMutexBits()
	// {
	// return this.mutexBits;
	// }

}
