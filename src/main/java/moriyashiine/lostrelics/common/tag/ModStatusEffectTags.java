/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.lostrelics.common.tag;

import moriyashiine.lostrelics.common.LostRelics;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;

public class ModStatusEffectTags {
	public static final TagKey<StatusEffect> BYPASSES_CURSED_AMULET = TagKey.of(RegistryKeys.STATUS_EFFECT, LostRelics.id("bypasses_cursed_amulet"));

	public static final TagKey<StatusEffect> CANNOT_BE_SIPHONED = TagKey.of(RegistryKeys.STATUS_EFFECT, LostRelics.id("cannot_be_siphoned"));
}
