package com.playermod.util;

import java.util.Random;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityWaterMob;
import net.minecraft.item.ItemStack;

public class PlayerModUtil {

	public static int getExperiencePoints(EntityLivingBase killed_entity) {

		int experience_value = 0;

		if (killed_entity instanceof EntityAnimal
				|| killed_entity instanceof EntityWaterMob)
			experience_value = 1 + killed_entity.worldObj.rand.nextInt(3);

		if (killed_entity instanceof EntityMob
				|| killed_entity instanceof EntityGhast)
			experience_value = 5;

		if (experience_value > 0) {
			int i = experience_value;
			ItemStack[] aitemstack = killed_entity.getInventory();

			for (int j = 0; j < aitemstack.length; ++j) {
				if (aitemstack[j] != null) {
					i += 1 + new Random().nextInt(3);
				}
			}

			System.out.println("Util " + i);
			return i;
		} else {
			return experience_value;
		}
	}

}
