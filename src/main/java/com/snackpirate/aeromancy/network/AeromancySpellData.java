package com.snackpirate.aeromancy.network;

//structured like ISS' SyncedSpellData

import io.redspace.ironsspellbooks.api.entity.IMagicEntity;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;

public class AeromancySpellData {
	public static final long ASPHYXIATING = 1;
	public static final long WIND_SHIELD = 2;
	public static final long AIR_STEP = 4;

	private final int serverPlayerId;
	private @Nullable LivingEntity livingEntity;

	private boolean isMob = false;
	private long syncedEffectFlags;
	private int airStepHitsRemaining;
	private ResourceLocation shapeshiftedEntityId;

	public AeromancySpellData(int serverPlayerId) {
		this.serverPlayerId = serverPlayerId;
		this.syncedEffectFlags = 0;
		this.airStepHitsRemaining = 0;
		this.shapeshiftedEntityId = ResourceLocation.withDefaultNamespace("pig");
	}
	public AeromancySpellData(boolean isMob) {
		this.serverPlayerId = -999;
		this.isMob = isMob;
	}
	public AeromancySpellData() {
		this.serverPlayerId = -999;
	}

	public static void write(FriendlyByteBuf buffer, AeromancySpellData data) {
		buffer.writeInt(data.serverPlayerId);
		buffer.writeLong(data.syncedEffectFlags);
		buffer.writeInt(data.airStepHitsRemaining);
		buffer.writeResourceLocation(data.shapeshiftedEntityId);
	}

	public static AeromancySpellData read(FriendlyByteBuf buffer) {
		var data = new AeromancySpellData(buffer.readInt());
		data.syncedEffectFlags = buffer.readLong();
		data.airStepHitsRemaining = buffer.readInt();
		data.shapeshiftedEntityId = buffer.readResourceLocation();
		return data;
	}

	public AeromancySpellData(LivingEntity livingEntity) {
		this(livingEntity == null ? -1 : livingEntity.getId());
		this.livingEntity = livingEntity;
	}
	public void saveNBTData(CompoundTag compound, HolderLookup.Provider provider) {
		compound.putLong("effectFlags", this.syncedEffectFlags);
		compound.putFloat("airStepHitsRemaining", this.airStepHitsRemaining);
		compound.putString("shapeshiftId", this.shapeshiftedEntityId.toString());
	}

	public void loadNBTData(CompoundTag compound, HolderLookup.Provider provider) {
		this.syncedEffectFlags = compound.getLong("effectFlags");
		this.airStepHitsRemaining = compound.getInt("airStepHitsRemaining");
		this.shapeshiftedEntityId = ResourceLocation.parse(compound.getString("shapeshiftId"));
	}


	public int getServerPlayerId() {
		return serverPlayerId;
	}


	public void addEffects(long effectFlags) {
		this.syncedEffectFlags |= effectFlags;
		doSync();
	}


	public void removeEffects(long effectFlags) {
		this.syncedEffectFlags &= ~effectFlags;
		doSync();
	}


	public boolean hasEffect(long effectFlags) {
		return (this.syncedEffectFlags & effectFlags) == effectFlags;
	}


	public int getAirStepHitsRemaining() {
		doSync();
		return airStepHitsRemaining;
	}

	public void subtractAirStepHits() {
		airStepHitsRemaining--;
		doSync();
	}

	public void setAirStepHitsRemaining(int hitsRemaining) {
		airStepHitsRemaining = hitsRemaining;
		doSync();
	}
	public void setShapeshiftId(ResourceLocation loc) {
		this.shapeshiftedEntityId = loc;
		doSync();
	}
	public ResourceLocation getShapeshiftedEntityId() {
		doSync();
		return this.shapeshiftedEntityId;
	}
	public static AeromancySpellData getAeromancyData(LivingEntity entity) {
		return entity.getData(AADataAttachments.AEROMANCY_DATA);
	}
	public void doSync() {
		if (livingEntity instanceof ServerPlayer serverPlayer) {
			PacketDistributor.sendToPlayersTrackingEntityAndSelf(serverPlayer, new AASyncPlayerDataPacket(this));
		} else if (livingEntity instanceof IMagicEntity abstractSpellCastingMob) {
			PacketDistributor.sendToPlayersTrackingEntity(livingEntity, new AASyncEntityDataPacket(this, abstractSpellCastingMob));
		}
	}

	public void syncToPlayer(ServerPlayer serverPlayer) {
		PacketDistributor.sendToPlayer(serverPlayer, new AASyncPlayerDataPacket(this));
	}


	@Override
	protected AeromancySpellData clone() {
		return new AeromancySpellData(this.livingEntity);
	}

	public AeromancySpellData getPersistentData(ServerPlayer serverPlayer) {
		//This updates the reference while keeping the id the same (because we are in the middle of cloning logic, where id has not been set yet)
		AeromancySpellData persistentData = new AeromancySpellData(livingEntity);
		persistentData.livingEntity = serverPlayer;
		return persistentData;
	}

}
