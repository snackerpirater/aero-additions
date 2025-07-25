package com.snackpirate.aeromancy.network;

import com.snackpirate.aeromancy.Aeromancy;
import com.snackpirate.aeromancy.spells.airstep.AirstepPacket;
import com.snackpirate.aeromancy.spells.dash.DashParticlesPacket;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, modid = Aeromancy.MOD_ID)
public class AAPayloadHandler {

	@SubscribeEvent
	public static void register(final RegisterPayloadHandlersEvent event) {
		final PayloadRegistrar payloadRegistrar = event.registrar(Aeromancy.MOD_ID).versioned("1.0.0").optional();

		payloadRegistrar.playToClient(DashParticlesPacket.TYPE, DashParticlesPacket.STREAM_CODEC, DashParticlesPacket::handle);
		payloadRegistrar.playToServer(AirstepPacket.TYPE, AirstepPacket.STREAM_CODEC, AirstepPacket::handle);
		payloadRegistrar.playToClient(AASyncPlayerDataPacket.TYPE, AASyncPlayerDataPacket.STREAM_CODEC, AASyncPlayerDataPacket::handle);
	}
}
