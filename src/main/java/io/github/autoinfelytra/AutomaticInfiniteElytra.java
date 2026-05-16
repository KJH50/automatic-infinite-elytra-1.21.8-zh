package io.github.autoinfelytra;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ColumnPos;

public class AutomaticInfiniteElytra {
    public static BlockPos blockPos(ColumnPos columnPos){
        return new BlockPos(columnPos.x(), 0, columnPos.z());
    }
    public static ColumnPos columnPos(BlockPos blockPos){
        return new ColumnPos(blockPos.getX(), blockPos.getZ());
    }
}