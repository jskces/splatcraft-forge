package com.cibernet.splatcraft.entities.subs;

import com.cibernet.splatcraft.entities.IColoredEntity;
import com.cibernet.splatcraft.entities.InkProjectileEntity;
import com.cibernet.splatcraft.handlers.WeaponHandler;
import com.cibernet.splatcraft.registries.SplatcraftEntities;
import com.cibernet.splatcraft.util.ColorUtils;
import com.cibernet.splatcraft.util.InkBlockUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nonnull;

public abstract class AbstractSubWeaponEntity extends ProjectileItemEntity implements IColoredEntity
{
    protected static final String SPLASH_DAMAGE_TYPE = "ink";
    protected static final DamageSource SPLASH_DAMAGE_SOURCE = new DamageSource(SPLASH_DAMAGE_TYPE);

    private static final DataParameter<Integer> COLOR = EntityDataManager.createKey(AbstractSubWeaponEntity.class, DataSerializers.VARINT);

    public boolean damageMobs = false;
    public InkBlockUtils.InkType inkType;
    public ItemStack sourceWeapon = ItemStack.EMPTY;

    @Deprecated //use AbstractWeaponEntity.create
    public AbstractSubWeaponEntity(EntityType<? extends AbstractSubWeaponEntity> type, World world)
    {
        super(type, world);
    }

    public static <A extends AbstractSubWeaponEntity> A create(EntityType<A> type, World world, @Nonnull LivingEntity thrower, ItemStack sourceWeapon)
    {
        return create(type, world, thrower, ColorUtils.getInkColor(sourceWeapon), InkBlockUtils.getInkType(thrower), sourceWeapon);
    }

    public static <A extends AbstractSubWeaponEntity> A create(EntityType<A> type, World world, LivingEntity thrower, int color, InkBlockUtils.InkType inkType, ItemStack sourceWeapon)
    {
        A result = type.create(world);
        result.setPosition(thrower.getPosX(), thrower.getPosYEye() - (double)0.1F, thrower.getPosZ());
        result.setShooter(thrower);
        result.setColor(color);
        result.inkType = inkType;
        result.sourceWeapon = sourceWeapon;

        return result;
    }

    public void shoot(Entity thrower, float pitch, float yaw, float pitchOffset, float velocity, float inaccuracy)
    {
        func_234612_a_(thrower, pitch, yaw, pitchOffset, velocity, inaccuracy);

        Vector3d posDiff = new Vector3d(0, 0, 0);

        if (thrower instanceof PlayerEntity)
        {
            try
            {
                posDiff = thrower.getPositionVec().subtract(WeaponHandler.getPlayerPrevPos((PlayerEntity) thrower));
                if(thrower.isOnGround())
                    posDiff.mul(1, 0, 1);

            } catch (NullPointerException ignored)
            {
            }
        }


        setPositionAndUpdate(getPosX() + posDiff.getX(), getPosY() + posDiff.getY(), getPosZ() + posDiff.getZ());
        setMotion(getMotion().add(posDiff.mul(0.8, 0.8, 0.8)));
    }

    @Override
    protected float getGravityVelocity() {
        return 0.09f;
    }

    @Override
    public void writeAdditional(CompoundNBT nbt)
    {
        nbt.putInt("Color", getColor());
        nbt.putBoolean("DamageMobs", damageMobs);
        nbt.putInt("InkType", inkType.getIndex());
        nbt.put("SourceWeapon", sourceWeapon.write(new CompoundNBT()));
    }


    @Override
    public void readAdditional(CompoundNBT nbt)
    {
        if(nbt.contains("Color"))
            setColor(nbt.getInt("Color"));
        damageMobs = nbt.getBoolean("DamageMobs");
        inkType = InkBlockUtils.InkType.values.get(nbt.getInt("InkType"));
        sourceWeapon = ItemStack.read(nbt.getCompound("SourceWeapon"));
    }


    protected void onBlockHit(BlockRayTraceResult result) { }

    @Override
    protected void onImpact(RayTraceResult result)
    {
        RayTraceResult.Type rayType = result.getType();
        if (rayType == RayTraceResult.Type.ENTITY)
        {
            this.onEntityHit((EntityRayTraceResult) result);
        } else if (rayType == RayTraceResult.Type.BLOCK)
        {
            onBlockHit((BlockRayTraceResult) result);
        }
    }

    @Override
    protected void registerData() {
        super.registerData();
        dataManager.register(COLOR, ColorUtils.DEFAULT);
    }

    @Override
    public int getColor() {
        return dataManager.get(COLOR);
    }

    @Override
    public void setColor(int color)
    {
        dataManager.set(COLOR, color);
    }


    @Override
    public IPacket<?> createSpawnPacket()
    {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}