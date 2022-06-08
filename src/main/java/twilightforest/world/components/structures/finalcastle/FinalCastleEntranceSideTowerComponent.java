package twilightforest.world.components.structures.finalcastle;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import twilightforest.block.TFBlocks;
import twilightforest.world.components.structures.TFStructureComponentOld;
import twilightforest.world.registration.TFFeature;

import java.util.RandomSource;

public class FinalCastleEntranceSideTowerComponent extends FinalCastleMazeTower13Component {

	public FinalCastleEntranceSideTowerComponent(StructurePieceSerializationContext ctx, CompoundTag nbt) {
		super(FinalCastlePieces.TFFCEnSiTo, nbt);
	}

	public FinalCastleEntranceSideTowerComponent(TFFeature feature, RandomSource rand, int i, int x, int y, int z, int floors, int entranceFloor, Direction direction) {
		super(FinalCastlePieces.TFFCEnSiTo, feature, rand, i, x, y, z, floors, entranceFloor, TFBlocks.PINK_CASTLE_RUNE_BRICK.get().defaultBlockState(), direction);

		addOpening(0, 1, size / 2, Rotation.CLOCKWISE_180);
	}

	@Override
	public void addChildren(StructurePiece parent, StructurePieceAccessor list, RandomSource rand) {
		if (parent != null && parent instanceof TFStructureComponentOld) {
			this.deco = ((TFStructureComponentOld) parent).deco;
		}

		// add foundation
		FinalCastleFoundation13Component foundation = new FinalCastleFoundation13Component(FinalCastlePieces.TFFCToF13, getFeatureType(), rand, 4, this, getLocatorPosition().getX(), getLocatorPosition().getY(), getLocatorPosition().getZ());
		list.addPiece(foundation);
		foundation.addChildren(this, list, rand);

		// add roof
		TFStructureComponentOld roof = new FinalCastleRoof13PeakedComponent(getFeatureType(), rand, 4, this, getLocatorPosition().getX(), getLocatorPosition().getY(), getLocatorPosition().getZ());
		list.addPiece(roof);
		roof.addChildren(this, list, rand);
	}
}
