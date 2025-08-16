package com.snackpirate.aeromancy.spells.airstep;

import com.snackpirate.aeromancy.Aeromancy;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;
//TODO: write player inputs to server for directional airstep
//client to server
public class AirstepPacket implements CustomPacketPayload {
	public static final CustomPacketPayload.Type<AirstepPacket> TYPE = new CustomPacketPayload.Type<>(Aeromancy.id("airstep"));
	public static final StreamCodec<RegistryFriendlyByteBuf, AirstepPacket> STREAM_CODEC = CustomPacketPayload.codec(AirstepPacket::write, AirstepPacket::new);
	public final int deltaX;
	public final int deltaZ;

	public AirstepPacket(FriendlyByteBuf buf) {
		deltaX = buf.readInt();
		deltaZ = buf.readInt();
	}

	@Override
	public @NotNull Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

	public AirstepPacket(int x, int z) {
		deltaX = x;
		deltaZ = z;
	}
	private void write(FriendlyByteBuf buf) {
		buf.writeInt(deltaX);
		buf.writeInt(deltaZ);
	}
	public static void handle(AirstepPacket packet, IPayloadContext ctx) {
		ctx.enqueueWork(() -> AirstepSpell.airstepJump(packet, ctx.player()));
	}
}
