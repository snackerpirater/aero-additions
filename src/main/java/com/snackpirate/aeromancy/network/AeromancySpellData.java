//package com.snackpirate.aeromancy.network;
//
////structured like ISS' SyncedSpellData
//
//import io.redspace.ironsspellbooks.api.entity.IMagicEntity;
//import io.redspace.ironsspellbooks.capabilities.magic.SyncedSpellData;
//import net.minecraft.core.HolderLookup;
//import net.minecraft.nbt.CompoundTag;
//import net.minecraft.network.FriendlyByteBuf;
//import net.minecraft.server.level.ServerPlayer;
//import net.minecraft.world.entity.LivingEntity;
//import net.neoforged.neoforge.network.PacketDistributor;
//import org.jetbrains.annotations.Nullable;
//
//public class AeromancySpellData {
//	public static final long ASPHYXIATING = 1;
//	public static final long WIND_SHIELD = 2;
//	public static final long AIR_STEP = 4;
//
//	private final int serverPlayerId;
//	private @Nullable LivingEntity livingEntity;
//
//	private long syncedEffectFlags;
//	private int airStepHitsRemaining;
//
//	public AeromancySpellData(int serverPlayerId) {
//		this.serverPlayerId = serverPlayerId;
//		this.syncedEffectFlags = 0;
//		this.airStepHitsRemaining = 0;
//	}
//
//
//	public static void write(FriendlyByteBuf buffer, AeromancySpellData data) {
//		buffer.writeInt(data.serverPlayerId);
//		buffer.writeLong(data.syncedEffectFlags);
//		buffer.writeInt(data.airStepHitsRemaining);
//	}
//
//	public static AeromancySpellData read(FriendlyByteBuf buffer) {
//		var data = new AeromancySpellData(buffer.readInt());
//		data.syncedEffectFlags = buffer.readLong();
//		data.airStepHitsRemaining = buffer.readInt();
//		return data;
//	}
//
//	public AeromancySpellData(LivingEntity livingEntity) {
//		this(livingEntity == null ? -1 : livingEntity.getId());
//		this.livingEntity = livingEntity;
//	}
//	public void saveNBTData(CompoundTag compound, HolderLookup.Provider provider) {
//		compound.putLong("effectFlags", this.syncedEffectFlags);
//		compound.putFloat("airStepHitsRemaining", this.airStepHitsRemaining);
//	}
//
//	public void loadNBTData(CompoundTag compound, HolderLookup.Provider provider) {
//		this.syncedEffectFlags = compound.getLong("effectFlags");
//		this.airStepHitsRemaining = compound.getInt("airStepHitsRemaining");
//	}
//
//
//	public int getServerPlayerId() {
//		return serverPlayerId;
//	}
//
//
//	public boolean hasEffect(long effectFlags) {
//		return (this.syncedEffectFlags & effectFlags) == effectFlags;
//	}
//
//
//	public int getAirStepHitsRemaining() {
//		return airStepHitsRemaining;
//	}
//
//	public void subtractAirStepHits() {
//		airStepHitsRemaining--;
//		doSync();
//	}
//
//	public void setAirStepHitsRemaining(int hitsRemaining) {
//		airStepHitsRemaining = hitsRemaining;
//		doSync();
//	}
//	public void doSync() {
//		if (livingEntity instanceof ServerPlayer serverPlayer) {
//			PacketDistributor.sendToPlayersTrackingEntityAndSelf(serverPlayer, new AASyncPlayerDataPacket(this));
//		} else if (livingEntity instanceof IMagicEntity abstractSpellCastingMob) {
//			PacketDistributor.sendToPlayersTrackingEntity(livingEntity, new AASyncEntityDataPacket(this, abstractSpellCastingMob));
//		}
//	}
//
//	public void syncToPlayer(ServerPlayer serverPlayer) {
//		PacketDistributor.sendToPlayer(serverPlayer, new AASyncPlayerDataPacket(this));
//	}
//
//
//	@Override
//	protected SyncedSpellData clone() {
//		return new SyncedSpellData(this.livingEntity);
//	}
//
//	public AeromancySpellData getPersistentData(ServerPlayer serverPlayer) {
//		//This updates the reference while keeping the id the same (because we are in the middle of cloning logic, where id has not been set yet)
//		AeromancySpellData persistentData = new AeromancySpellData(livingEntity);
//		persistentData.livingEntity = serverPlayer;
//		return persistentData;
//	}
//
//}
