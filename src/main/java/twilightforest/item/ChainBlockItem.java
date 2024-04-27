package twilightforest.item;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import twilightforest.data.tags.ItemTagGenerator;
import twilightforest.entity.projectile.ChainBlock;
import twilightforest.init.TFDataComponents;
import twilightforest.init.TFEnchantments;
import twilightforest.init.TFEntities;
import twilightforest.init.TFSounds;

import java.util.UUID;

public class ChainBlockItem extends Item {

	private static final String THROWN_UUID_KEY = "chainEntity";

	public ChainBlockItem(Properties properties) {
		super(properties);
	}

	@Override
	public void inventoryTick(ItemStack stack, Level level, Entity holder, int slot, boolean isSelected) {
		if (!level.isClientSide() && stack.get(TFDataComponents.THROWN_PROJECTILE) != null && this.getThrownEntity(level, stack) == null) {
			stack.remove(TFDataComponents.THROWN_PROJECTILE);
		}
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);

		if (stack.get(TFDataComponents.THROWN_PROJECTILE) != null || !level.getWorldBorder().isWithinBounds(player.blockPosition()))
			return new InteractionResultHolder<>(InteractionResult.PASS, stack);

		player.playSound(TFSounds.BLOCK_AND_CHAIN_FIRED.get(), 0.5F, 1.0F / (level.getRandom().nextFloat() * 0.4F + 1.2F));

		if (!level.isClientSide()) {
			ChainBlock launchedBlock = new ChainBlock(TFEntities.CHAIN_BLOCK.get(), level, player, hand, stack);
			level.addFreshEntity(launchedBlock);
			stack.set(TFDataComponents.THROWN_PROJECTILE, launchedBlock.getUUID());
		}

		player.startUsingItem(hand);
		return new InteractionResultHolder<>(InteractionResult.SUCCESS, stack);
	}

	@Nullable
	private ChainBlock getThrownEntity(Level level, ItemStack stack) {
		if (level instanceof ServerLevel server) {
			UUID id = stack.get(TFDataComponents.THROWN_PROJECTILE);
			if (id != null) {
				Entity e = server.getEntity(id);
				if (e instanceof ChainBlock) {
					return (ChainBlock) e;
				}
			}
		}

		return null;
	}

	@Override
	public int getUseDuration(ItemStack stack) {
		return 72000;
	}

	@Override
	public UseAnim getUseAnimation(ItemStack stack) {
		return UseAnim.BLOCK;
	}

	@Override
	public boolean isValidRepairItem(ItemStack stack, ItemStack repairItem) {
		return repairItem.is(ItemTagGenerator.KNIGHTMETAL_INGOTS);
	}

	//FIXME I hate new tool tier logic
	@Override
	public boolean isCorrectToolForDrops(ItemStack stack, BlockState state) {
		//dont try to check harvest level if we arent thrown
		if (stack.get(TFDataComponents.THROWN_PROJECTILE) == null) return false;
		if (stack.getEnchantmentLevel(TFEnchantments.DESTRUCTION.get()) > 0) {
			if (state.is(BlockTags.MINEABLE_WITH_PICKAXE) || state.is(BlockTags.MINEABLE_WITH_HOE)
					|| state.is(BlockTags.MINEABLE_WITH_SHOVEL) || state.is(BlockTags.MINEABLE_WITH_AXE))
				return this.getHarvestLevel(stack).createToolProperties(BlockTags.MINEABLE_WITH_AXE).isCorrectForDrops(state);
		}
		return false;
	}

	public Tier getHarvestLevel(ItemStack stack) {
		int enchantLevel = stack.getEnchantmentLevel(TFEnchantments.DESTRUCTION.get());
		if (enchantLevel == 2) {
			return Tiers.STONE;
		} else if (enchantLevel >= 3) {
			return Tiers.IRON;
		} else {
			return Tiers.WOOD;
		}
	}
}