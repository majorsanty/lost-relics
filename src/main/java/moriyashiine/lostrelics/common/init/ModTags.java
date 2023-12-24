/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.lostrelics.common.init;

import moriyashiine.lostrelics.common.LostRelics;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;

public class ModTags {
	public static class ItemTags {
		public static final TagKey<Item> RELICS = TagKey.of(RegistryKeys.ITEM, LostRelics.id("relics"));

		public static final TagKey<Item> JUNGLE_RELICS = TagKey.of(RegistryKeys.ITEM, LostRelics.id("jungle_relics"));
	}

	public static class StatusEffectTags {
		public static final TagKey<StatusEffect> BYPASSES_CURSED_AMULET = TagKey.of(RegistryKeys.STATUS_EFFECT, LostRelics.id("bypasses_cursed_amulet"));

		public static final TagKey<StatusEffect> CANNOT_BE_SIPHONED = TagKey.of(RegistryKeys.STATUS_EFFECT, LostRelics.id("cannot_be_siphoned"));

		public static boolean isIn(StatusEffectInstance statusEffect, TagKey<net.minecraft.entity.effect.StatusEffect> tag) {
			return Registries.STATUS_EFFECT.entryOf(Registries.STATUS_EFFECT.getKey(statusEffect.getEffectType()).orElse(null)).isIn(tag);
		}
	}
}
