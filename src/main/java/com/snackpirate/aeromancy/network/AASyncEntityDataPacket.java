package com.snackpirate.aeromancy.network;

import com.snackpirate.aeromancy.Aeromancy;
import io.redspace.ironsspellbooks.api.entity.IMagicEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class AASyncEntityDataPacket implements CustomPacketPayload {
	AeromancySpellData data;
	int entityId;
	public static final CustomPacketPayload.Type<AASyncEntityDataPacket> TYPE = new CustomPacketPayload.Type<>(Aeromancy.id("sync_entity_data"));
	public static final StreamCodec<RegistryFriendlyByteBuf, AASyncEntityDataPacket> STREAM_CODEC = CustomPacketPayload.codec(AASyncEntityDataPacket::write, AASyncEntityDataPacket::new);

	public AASyncEntityDataPacket(AeromancySpellData data, IMagicEntity entity) {
		this.data = data;
		this.entityId = ((Entity) entity).getId();
	}

	public AASyncEntityDataPacket(FriendlyByteBuf buf) {
		entityId = buf.readInt();
		data = AeromancySpellData.read(buf);
	}
	public void write(FriendlyByteBuf buf) {
		buf.writeInt(entityId);
		AeromancySpellData.write(buf, data);
	}
	public static void handle(AASyncEntityDataPacket packet, IPayloadContext ctx) {
		ctx.enqueueWork(() -> {

		});
	}
	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
