package com.snackpirate.aeromancy.spells.airstep;

import com.snackpirate.aeromancy.Aeromancy;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;
//client to server
public class AirstepPacket implements CustomPacketPayload {
	public static final CustomPacketPayload.Type<AirstepPacket> TYPE = new CustomPacketPayload.Type<>(Aeromancy.id("airstep"));
	public static final StreamCodec<RegistryFriendlyByteBuf, AirstepPacket> STREAM_CODEC = CustomPacketPayload.codec(AirstepPacket::write, AirstepPacket::new);

	public AirstepPacket(FriendlyByteBuf buf) {
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

	public AirstepPacket() {
	}
	private void write(FriendlyByteBuf buf) {
	}
	public static void handle(AirstepPacket packet, IPayloadContext ctx) {
		ctx.enqueueWork(() -> AirstepSpell.airstepJump(ctx.player()));
	}
}
