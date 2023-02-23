package net.splatcraft.forge.data.capabilities.worldinfo;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import org.jetbrains.annotations.Nullable;

public class WorldInfoStorage implements Capability.IStorage<IWorldInfo>
{
    @Nullable
    @Override
    public INBT writeNBT(Capability<IWorldInfo> capability, IWorldInfo instance, Direction side)
    {
        return instance.writeNBT(new CompoundNBT());
    }

    @Override
    public void readNBT(Capability<IWorldInfo> capability, IWorldInfo instance, Direction side, INBT nbt)
    {
        instance.readNBT((CompoundNBT) nbt);
    }
}
