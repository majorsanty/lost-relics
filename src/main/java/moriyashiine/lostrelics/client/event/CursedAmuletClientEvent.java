/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.lostrelics.client.event;

import moriyashiine.lostrelics.common.init.ModItems;
import moriyashiine.lostrelics.common.item.CursedAmuletItem;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableTextContent;
import net.minecraft.util.Formatting;

import java.util.List;
import java.util.Map;

public class CursedAmuletClientEvent implements ItemTooltipCallback {
	@Override
	public void getTooltip(ItemStack stack, TooltipContext context, List<Text> lines) {
		if (stack.isOf(ModItems.CURSED_AMULET)) {
			boolean added = false;
			for (int i = lines.size() - 1; i >= 0; i--) {
				if (lines.get(i).getContent() instanceof TranslatableTextContent content && content.getKey().contains("attribute.modifier")) {
					lines.remove(i);
					if (!added) {
						int addIndex = i;
						for (Map.Entry<EntityAttribute, EntityAttributeModifier> entry : CursedAmuletItem.GOOD_MODIFIERS.entries()) {
							double baseValue = entry.getValue().getValue();
							double value = entry.getValue().getOperation() == EntityAttributeModifier.Operation.MULTIPLY_BASE || entry.getValue().getOperation() == EntityAttributeModifier.Operation.MULTIPLY_TOTAL ? baseValue * 100.0 : (entry.getKey().equals(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE) ? baseValue * 10.0 : baseValue);
							lines.add(addIndex, Text.translatable("attribute.modifier.plus." + entry.getValue().getOperation().getId(), ItemStack.MODIFIER_FORMAT.format(value), Text.translatable(entry.getKey().getTranslationKey()).append(Text.literal("?"))).formatted(Formatting.LIGHT_PURPLE));
							addIndex++;
						}
						added = true;
					}
				}
			}
		}
	}
}
