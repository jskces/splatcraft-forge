package net.splatcraft.forge.data.capabilities.blockcolorinfo;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;

import java.util.HashMap;

public interface IBlockColorInfo
{
    HashMap<BlockPos, BlockColorInfo.ColorData> getColorData();

    void setColor(BlockPos pos, int color);
    int getColor(BlockPos pos);

    String encode(BlockPos pos);
    BlockColorInfo.ColorData decode(String str);

    CompoundNBT writeNBT(CompoundNBT nbt);

    void readNBT(CompoundNBT nbt);
}
