package com.snackpirate.aeromancy.network;

import io.redspace.ironsspellbooks.api.magic.MagicData;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.attachment.IAttachmentSerializer;
import org.jetbrains.annotations.Nullable;

public class AeromancyDataProvider implements IAttachmentSerializer<CompoundTag, AeromancySpellData> {
	@Override
	public AeromancySpellData read(IAttachmentHolder holder, CompoundTag tag, HolderLookup.Provider provider) {

		var magicData = holder instanceof ServerPlayer serverPlayer ? new AeromancySpellData(serverPlayer) : new AeromancySpellData(true);
		magicData.loadNBTData(tag, provider);
		return magicData;
	}

	@Override
	public @Nullable CompoundTag write(AeromancySpellData aeromancySpellData, HolderLookup.Provider provider) {
		var tag = new CompoundTag();
		aeromancySpellData.saveNBTData(tag, provider);
		return tag;
	}
}
