package com.snackpirate.aeromancy.network;

import io.redspace.ironsspellbooks.api.entity.IMagicEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import java.util.HashMap;

public class AAClientData {

	private static final AeromancySpellData playerMagicData = new AeromancySpellData();

	private static final HashMap<Integer, AeromancySpellData> playerSyncedDataLookup = new HashMap<>();
	private static final AeromancySpellData emptySyncedData = new AeromancySpellData(-999);

	public static AeromancySpellData getAeroSpellData(LivingEntity livingEntity) {
		if (livingEntity instanceof Player) {
			return playerSyncedDataLookup.getOrDefault(livingEntity.getId(), emptySyncedData);
		}
		return new AeromancySpellData(null);

	}

	public static void handlePlayerSyncedData(AeromancySpellData playerSyncedData) {

		playerSyncedDataLookup.put(playerSyncedData.getServerPlayerId(), playerSyncedData);
	}

	public static void handleAbstractCastingMobSyncedData(int entityId, AeromancySpellData syncedSpellData) {
		var level = Minecraft.getInstance().level;


		if (level == null) {
			return;
		}

		var entity = level.getEntity(entityId);
		if (entity instanceof IMagicEntity abstractSpellCastingMob) {
		}
	}
}
