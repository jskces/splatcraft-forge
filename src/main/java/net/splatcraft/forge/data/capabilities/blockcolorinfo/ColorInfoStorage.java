package net.splatcraft.forge.data.capabilities.blockcolorinfo;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import org.jetbrains.annotations.Nullable;

public class ColorInfoStorage implements Capability.IStorage<IBlockColorInfo>
{
    @Nullable
    @Override
    public INBT writeNBT(Capability<IBlockColorInfo> capability, IBlockColorInfo instance, Direction side)
    {
        return instance.writeNBT(new CompoundNBT());
    }

    @Override
    public void readNBT(Capability<IBlockColorInfo> capability, IBlockColorInfo instance, Direction side, INBT nbt)
    {
        instance.readNBT((CompoundNBT) nbt);
    }
}
