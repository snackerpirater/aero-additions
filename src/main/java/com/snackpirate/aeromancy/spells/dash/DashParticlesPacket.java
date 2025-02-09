package com.snackpirate.aeromancy.spells.dash;

import com.snackpirate.aeromancy.Aeromancy;
import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.player.ClientSpellCastHelper;
import io.redspace.ironsspellbooks.spells.ender.TeleportSpell;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.IPayloadContext;

//Contains:
//Starting position: Vec3
//Direction/velocity: Vec3
public class DashParticlesPacket implements CustomPacketPayload {
private final Vec3 pos1;
private final Vec3 pos2;
public static final CustomPacketPayload.Type<DashParticlesPacket> TYPE = new CustomPacketPayload.Type<>(Aeromancy.id("dash_particles"));
public static final StreamCodec<RegistryFriendlyByteBuf, DashParticlesPacket> STREAM_CODEC = CustomPacketPayload.codec(DashParticlesPacket::write, DashParticlesPacket::new);

public DashParticlesPacket(Vec3 pos1, Vec3 pos2) {
	this.pos1 = pos1;
	this.pos2 = pos2;
}

public DashParticlesPacket(FriendlyByteBuf buf) {
	pos1 = buf.readVec3();
	pos2 = buf.readVec3();
}

public void write(FriendlyByteBuf buf) {
	buf.writeVec3(pos1);
	buf.writeVec3(pos2);
}

public static void handle(DashParticlesPacket packet, IPayloadContext context) {
	context.enqueueWork(() -> {
//		ClientSpellCastHelper.handleClientboundTeleport(packet.pos1, packet.pos2); particle work goes here

		var player = Minecraft.getInstance().player;
		var pos1 = packet.pos1;
		var pos2 = packet.pos2.scale(0.2);
		if (player != null) {
			var level = Minecraft.getInstance().player.clientLevel;
			double width = 0.5;
			float height = 1;
			for (int i = 0; i < 20; i++) {
				double x = pos1.x + Utils.random.nextDouble() * width * 2 - width;
				double y = pos1.y + Utils.random.nextDouble() * height * 1.2 * 2 - height * 1.2;
				double z = pos1.z + Utils.random.nextDouble() * width * 2 - width;
				double dx = pos2.x + (Utils.random.nextDouble() * .1);
				double dy = pos2.y + (Utils.random.nextDouble() * .1);
				double dz = pos2.z + (Utils.random.nextDouble() * .1);
				level.addParticle(ParticleTypes.SPIT, true, x, y, z, -dx, -dy, -dz);
			}
		}
	});
}

@Override
public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
	return TYPE;
}
}
