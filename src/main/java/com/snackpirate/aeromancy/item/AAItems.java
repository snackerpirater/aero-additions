package com.snackpirate.aeromancy.item;

import com.snackpirate.aeromancy.Aeromancy;
import com.snackpirate.aeromancy.data.AAUpgradeOrbs;
import com.snackpirate.aeromancy.spells.AASpells;
import io.redspace.ironsspellbooks.api.item.weapons.MagicSwordItem;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.registry.SpellDataRegistryHolder;
import io.redspace.ironsspellbooks.item.UniqueSpellBook;
import io.redspace.ironsspellbooks.item.weapons.AttributeContainer;
import io.redspace.ironsspellbooks.item.weapons.StaffItem;
import io.redspace.ironsspellbooks.registries.ComponentRegistry;
import io.redspace.ironsspellbooks.util.ItemPropertiesHelper;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class AAItems {
	private static final DeferredRegister<Item> ITEMS = DeferredRegister.createItems(Aeromancy.MOD_ID);
	public static void register(IEventBus eventBus) {
		ITEMS.register(eventBus);
	}

	public static final DeferredHolder<Item,Item> AIR_STAFF = ITEMS.register("air_staff", () -> new StaffItem(new Item.Properties()
			.stacksTo(1)
			.rarity(Rarity.RARE)
			.attributes(ItemAttributeModifiers.builder()
					.add(Attributes.ATTACK_DAMAGE, new AttributeModifier(Item.BASE_ATTACK_DAMAGE_ID,2, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
					.add(Attributes.ATTACK_SPEED,  new AttributeModifier(Item.BASE_ATTACK_SPEED_ID,-3, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
					.add(AASpells.Attributes.WIND_SPELL_POWER, new AttributeModifier(Aeromancy.id("wind_spell_power"), 0.15, AttributeModifier.Operation.ADD_MULTIPLIED_BASE), EquipmentSlotGroup.MAINHAND)
					.add(AttributeRegistry.SPELL_POWER, new AttributeModifier(Aeromancy.id("spell_power"), 0.05, AttributeModifier.Operation.ADD_MULTIPLIED_BASE), EquipmentSlotGroup.MAINHAND)
					.add(Attributes.GRAVITY, new AttributeModifier(Aeromancy.id("gravity"), -0.25, AttributeModifier.Operation.ADD_MULTIPLIED_BASE), EquipmentSlotGroup.MAINHAND)
					.build())
			));

	public static final DeferredHolder<Item, Item> UPDRAFT_TOME = ITEMS.register("updraft_tome", () -> new UniqueSpellBook(
			new SpellDataRegistryHolder[]{
					new SpellDataRegistryHolder(AASpells.UPDRAFT, 5)
			},
			7).withSpellbookAttributes(
					new AttributeContainer(AASpells.Attributes.WIND_SPELL_POWER, .10, AttributeModifier.Operation.ADD_MULTIPLIED_BASE),
					new AttributeContainer(AttributeRegistry.MAX_MANA, 200, AttributeModifier.Operation.ADD_VALUE))
	);
	public static final DeferredHolder<Item, Item> WIND_RUNE = ITEMS.register("wind_rune", () -> new Item(new Item.Properties().stacksTo(64)));

	public static final DeferredHolder<Item, Item> WIND_UPGRADE_ORB = ITEMS.register("wind_upgrade_orb", () -> new Item(new Item.Properties().stacksTo(64).rarity(Rarity.UNCOMMON).component(ComponentRegistry.UPGRADE_ORB_TYPE, AAUpgradeOrbs.WIND_SPELL_POWER)));

	public static final DeferredHolder<Item, Item> WINDMAKER_HEADPIECE = ITEMS.register("windmaker_headpiece", () -> new WindmakerRobeItem(ArmorItem.Type.HELMET, ItemPropertiesHelper.equipment(1).durability(ArmorItem.Type.HELMET.getDurability(37))));
	public static final DeferredHolder<Item, Item> WINDMAKER_ROBES = ITEMS.register("windmaker_robes", () -> new WindmakerRobeItem(ArmorItem.Type.CHESTPLATE, ItemPropertiesHelper.equipment(1).durability(ArmorItem.Type.CHESTPLATE.getDurability(37))));
	public static final DeferredHolder<Item, Item> WINDMAKER_SKIRT = ITEMS.register("windmaker_leggings", () -> new WindmakerRobeItem(ArmorItem.Type.LEGGINGS, ItemPropertiesHelper.equipment(1).durability(ArmorItem.Type.LEGGINGS.getDurability(37))));
	public static final DeferredHolder<Item, Item> WINDMAKER_BOOTS = ITEMS.register("windmaker_boots", () -> new WindmakerRobeItem(ArmorItem.Type.BOOTS, ItemPropertiesHelper.equipment(1).durability(ArmorItem.Type.BOOTS.getDurability(37))));

	public static final DeferredHolder<Item, Item> WIND_SWORD = ITEMS.register("wind_sword", () -> new MagicSwordItem(
			Tiers.DIAMOND,
			ItemPropertiesHelper.equipment(1).rarity(Rarity.EPIC).durability(Tiers.DIAMOND.getUses()).attributes(
					ItemAttributeModifiers.builder()
							.add(AASpells.Attributes.WIND_SPELL_POWER, new AttributeModifier(Aeromancy.id("wind_sword_power"), .1f, AttributeModifier.Operation.ADD_MULTIPLIED_BASE), EquipmentSlotGroup.MAINHAND)
							.add(Attributes.ATTACK_DAMAGE, new AttributeModifier(Item.BASE_ATTACK_DAMAGE_ID, 4, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
							.add(Attributes.ATTACK_SPEED, new AttributeModifier(Item.BASE_ATTACK_SPEED_ID, -2, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
							.add(Attributes.ATTACK_KNOCKBACK, new AttributeModifier(Aeromancy.id("wind_sword_kb"), 1.5, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND).build()),
			new SpellDataRegistryHolder[]{new SpellDataRegistryHolder(AASpells.WIND_BLADE, 10)}
			));
//
//	public static final DeferredHolder<Item, Item> TEST_PICKAXE = ITEMS.register("test_pickaxe", () -> new TestImbuableTool(
//			ItemPropertiesHelper
//					.equipment(1)
//					.rarity(Rarity.EPIC)
//					.durability(Tiers.DIAMOND.getUses())
//					.component(DataComponents.TOOL, Tiers.DIAMOND.createToolProperties(BlockTags.MINEABLE_WITH_PICKAXE))));
}
