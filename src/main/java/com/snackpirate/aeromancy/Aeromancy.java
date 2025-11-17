package com.snackpirate.aeromancy;

import com.snackpirate.aeromancy.item.AAItems;
import com.snackpirate.aeromancy.network.AADataAttachments;
import com.snackpirate.aeromancy.spells.AASpells;
import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.spells.fire.FireboltSpell;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(Aeromancy.MOD_ID)
public class Aeromancy
{
    public static final String MOD_ID = "aero_additions";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static ResourceLocation id(String s) {return ResourceLocation.fromNamespaceAndPath(MOD_ID, s);}


    public Aeromancy(IEventBus modEventBus, ModContainer modContainer)
    {
        NeoForge.EVENT_BUS.register(this);
        AASounds.register(modEventBus);
        AASpells.register(modEventBus);
        AAItems.register(modEventBus);
        AACreativeTab.register(modEventBus);
        AADataAttachments.register(modEventBus);

    }

}
