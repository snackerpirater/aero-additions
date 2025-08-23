package com.snackpirate.aeromancy;

import com.snackpirate.aeromancy.item.AAItems;
import com.snackpirate.aeromancy.network.AAClientData;
import com.snackpirate.aeromancy.network.AeromancySpellData;
import com.snackpirate.aeromancy.spells.AASpells;
import com.snackpirate.aeromancy.spells.airstep.AirstepCounterBar;
import com.snackpirate.aeromancy.spells.airstep.AirstepPacket;
import com.snackpirate.aeromancy.spells.airstep.AirstepSpell;
import com.snackpirate.aeromancy.spells.dash.DashParticlesPacket;
import com.snackpirate.aeromancy.spells.tornado.TornadoRenderer;
import com.snackpirate.aeromancy.spells.updraft.UpdraftVisualRenderer;
import com.snackpirate.aeromancy.spells.wind_blade.WindBladeRenderer;
import com.snackpirate.aeromancy.spells.wind_charge.MagicWindChargeRenderer;
import com.snackpirate.aeromancy.spells.wind_shield.WindySwirlRenderer;
import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.render.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.entity.BreezeRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.event.RenderLivingEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;

import java.util.Arrays;


@EventBusSubscriber(modid = Aeromancy.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class AAClientEvents {

	@SubscribeEvent
	public static void rendererRegister(EntityRenderersEvent.RegisterRenderers event) {
		CuriosRendererRegistry.register(AAItems.UPDRAFT_TOME.get(), SpellBookCurioRenderer::new);

		event.registerEntityRenderer(AASpells.Entities.MAGIC_WIND_CHARGE.get(), MagicWindChargeRenderer::new);
		event.registerEntityRenderer(AASpells.Entities.UPDRAFT_VISUAL_ENTITY.get(), UpdraftVisualRenderer::new);
		event.registerEntityRenderer(AASpells.Entities.SUMMONED_BREEZE.get(), BreezeRenderer::new);
		event.registerEntityRenderer(AASpells.Entities.WIND_BLADE_PROJECTILE.get(), WindBladeRenderer::new);
		event.registerEntityRenderer(AASpells.Entities.TORNADO.get(), TornadoRenderer::new);


	}

	@SubscribeEvent
	public static void registerRenderers(final EntityRenderersEvent.AddLayers event) {
		addLayerToPlayerSkin(event, PlayerSkin.Model.SLIM);
		addLayerToPlayerSkin(event, PlayerSkin.Model.WIDE);

//		for (EntityType<?> type : event.getEntityTypes()) {
//			var renderer = event.getRenderer(type);
//			if (renderer instanceof GeoLivingEntityRenderer geoRenderer) {
//				geoRenderer.addRenderLayer(new WindySwirlRenderer.Geo(geoRenderer, Aeromancy.id("textures/entity/wind_shield_layer.png"), 256L));
//			}
//		}
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	private static void addLayerToPlayerSkin(EntityRenderersEvent.AddLayers event, PlayerSkin.Model skinName) {
		EntityRenderer<? extends Player> render = event.getSkin(skinName);
		if (render instanceof LivingEntityRenderer livingRenderer) {
			livingRenderer.addLayer(new WindySwirlRenderer.Vanilla(livingRenderer, Aeromancy.id("textures/entity/wind_shield_layer.png"), AeromancySpellData.WIND_SHIELD));
		}
	}
	@SubscribeEvent
	public static void registerLayers(RegisterGuiLayersEvent event) {
		event.registerAbove(IronsSpellbooks.id("mana_overlay"), Aeromancy.id("airstep_counter"), AirstepCounterBar.instance);
	}
	@EventBusSubscriber(modid = Aeromancy.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.GAME)
	static class Game {
 	//once again very graciously borrowed from tinkers' construct
		private static boolean wasJumping = false;

		@SubscribeEvent
		private static void handleJump(PlayerTickEvent.Pre event) {
			Minecraft minecraft = Minecraft.getInstance();
			if (minecraft.player != null && minecraft.player == event.getEntity() && !minecraft.player.isSpectator()) {
				boolean isJumping = minecraft.options.keyJump.isDown();
				if (!wasJumping && isJumping && !event.getEntity().onGround() && !event.getEntity().isFallFlying()) {
					if (AirstepSpell.canJump(event.getEntity())) {
						int x = (minecraft.player.input.up ? 1 : 0) + (minecraft.player.input.down ? -1 : 0);
						int z = (minecraft.player.input.right ? 1 : 0) + (minecraft.player.input.left ? -1 : 0);
						AirstepPacket packet = new AirstepPacket(x, z);
						AirstepSpell.airstepJump(packet, event.getEntity());
						PacketDistributor.sendToServer(packet);
					}
				}
				wasJumping = isJumping;
			}
		}
//		@SubscribeEvent
//		private static void renderShapeshift(RenderLivingEvent.Pre<LivingEntity, EntityModel<LivingEntity>> event) {
//			LivingEntity living = event.getEntity();
//			ResourceLocation entityLoc = AAClientData.getAeroSpellData(living).getShapeshiftedEntityId();
//			LivingEntity e = (LivingEntity) BuiltInRegistries.ENTITY_TYPE.get(entityLoc).create(living.level());
//			if (living instanceof Player && e!=null) {
//				e.yBodyRot = living.yBodyRot;
//				e.yBodyRotO = living.yBodyRotO;
//				e.yHeadRot = living.yHeadRot;
//				e.yHeadRotO = living.yHeadRotO;
//				e.xRotO = living.xRotO;
//				e.setXRot(living.getXRot());
//				e.walkAnimation.setSpeed(living.walkAnimation.speed());
//				e.walkAnimation.speedOld = living.walkAnimation.speedOld;
//				e.walkAnimation.position = living.walkAnimation.position;
//				e.attackAnim = living.attackAnim;
//				e.oAttackAnim = living.oAttackAnim;
//				e.swinging = living.swinging;
//				e.setSprinting(living.isSprinting());
//				Arrays.stream(EquipmentSlot.values()).forEach((slot) -> e.setItemSlot(slot, living.getItemBySlot(slot).copy()));
//
//				Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(e).render(e, living.yBodyRot, event.getPartialTick(), event.getPoseStack(), event.getMultiBufferSource(), event.getPackedLight());
////				event.getRenderer().render(e, event.getEntity().yBodyRot, event.getPartialTick(), event.getPoseStack(), event.getMultiBufferSource(), event.getPackedLight());
//				event.setCanceled(true);
//			}
//		}
	}
}
