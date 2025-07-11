package com.snackpirate.aeromancy.network;

import com.snackpirate.aeromancy.Aeromancy;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class AASyncPlayerDataPacket implements CustomPacketPayload {
	AeromancySpellData data;
	public static final CustomPacketPayload.Type<AASyncPlayerDataPacket> TYPE = new CustomPacketPayload.Type<>(Aeromancy.id("sync_magic_data"));
	public static final StreamCodec<RegistryFriendlyByteBuf, AASyncPlayerDataPacket> STREAM_CODEC = CustomPacketPayload.codec(AASyncPlayerDataPacket::write, AASyncPlayerDataPacket::new);

	public AASyncPlayerDataPacket(FriendlyByteBuf buf) {
		data = AeromancySpellData.read(buf);
	}
	public AASyncPlayerDataPacket(AeromancySpellData data) {
		this.data = data;
	}
	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

	public void write(FriendlyByteBuf buf) {
		AeromancySpellData.write(buf, data);
	}

	public static void handle(AASyncPlayerDataPacket packet, IPayloadContext context) {
		context.enqueueWork(() -> {
			AAClientData.handlePlayerSyncedData(packet.data);
		});
	}
}
