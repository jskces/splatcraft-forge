package net.splatcraft.forge.data.capabilities.worldinfo;

import net.minecraft.nbt.CompoundNBT;

import java.util.ArrayList;
import java.util.Collection;

public interface IWorldInfo
{
    ArrayList<Long> getPendingColorData();

    CompoundNBT writeNBT(CompoundNBT nbt);

    void readNBT(CompoundNBT nbt);
}
