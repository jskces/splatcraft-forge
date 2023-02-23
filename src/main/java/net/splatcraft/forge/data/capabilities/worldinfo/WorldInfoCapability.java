package net.splatcraft.forge.data.capabilities.worldinfo;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class WorldInfoCapability implements ICapabilitySerializable<CompoundNBT>
{
    @CapabilityInject(IWorldInfo.class)
    public static final Capability<IWorldInfo> CAPABILITY = null;
    private static final IWorldInfo DEFAULT = new WorldInfo();
    private final LazyOptional<IWorldInfo> instance = LazyOptional.of(CAPABILITY::getDefaultInstance);

    public static void register()
    {
        CapabilityManager.INSTANCE.register(IWorldInfo.class, new WorldInfoStorage(), WorldInfo::new);
    }

    public static IWorldInfo get(World level) throws NullPointerException
    {
        return level.getCapability(CAPABILITY).orElseThrow(() -> new NullPointerException("Couldn't find WorldData capability!"));
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side)
    {
        return CAPABILITY.orEmpty(cap, instance);
    }

    @Override
    public CompoundNBT serializeNBT()
    {
        return (CompoundNBT) CAPABILITY.getStorage().writeNBT(CAPABILITY, instance.orElseThrow(() -> new IllegalArgumentException("LazyOptional cannot be empty!")), null);
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt)
    {
        CAPABILITY.getStorage().readNBT(CAPABILITY, instance.orElseThrow(() -> new IllegalArgumentException("LazyOptional cannot be empty!")), null, nbt);
    }

}
