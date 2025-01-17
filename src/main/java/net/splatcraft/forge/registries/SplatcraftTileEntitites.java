package net.splatcraft.forge.registries;

import net.minecraft.block.Block;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.IContainerFactory;
import net.minecraftforge.registries.IForgeRegistry;
import net.splatcraft.forge.client.renderer.tileentity.InkedBlockTileEntityRenderer;
import net.splatcraft.forge.client.renderer.tileentity.RemotePedestalTileEntityRenderer;
import net.splatcraft.forge.client.renderer.tileentity.StageBarrierTileEntityRenderer;
import net.splatcraft.forge.tileentities.ColoredBarrierTileEntity;
import net.splatcraft.forge.tileentities.CrateTileEntity;
import net.splatcraft.forge.tileentities.InkColorTileEntity;
import net.splatcraft.forge.tileentities.InkVatTileEntity;
import net.splatcraft.forge.tileentities.InkedBlockTileEntity;
import net.splatcraft.forge.tileentities.InkwellTileEntity;
import net.splatcraft.forge.tileentities.RemotePedestalTileEntity;
import net.splatcraft.forge.tileentities.StageBarrierTileEntity;
import net.splatcraft.forge.tileentities.container.InkVatContainer;
import net.splatcraft.forge.tileentities.container.WeaponWorkbenchContainer;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import static net.splatcraft.forge.registries.SplatcraftBlocks.allowedColorBarrier;
import static net.splatcraft.forge.registries.SplatcraftBlocks.canvas;
import static net.splatcraft.forge.registries.SplatcraftBlocks.clearInkedBlock;
import static net.splatcraft.forge.registries.SplatcraftBlocks.crate;
import static net.splatcraft.forge.registries.SplatcraftBlocks.deniedColorBarrier;
import static net.splatcraft.forge.registries.SplatcraftBlocks.glowingInkedBlock;
import static net.splatcraft.forge.registries.SplatcraftBlocks.inkVat;
import static net.splatcraft.forge.registries.SplatcraftBlocks.inkedBlock;
import static net.splatcraft.forge.registries.SplatcraftBlocks.inkedWool;
import static net.splatcraft.forge.registries.SplatcraftBlocks.inkwell;
import static net.splatcraft.forge.registries.SplatcraftBlocks.remotePedestal;
import static net.splatcraft.forge.registries.SplatcraftBlocks.splatSwitch;
import static net.splatcraft.forge.registries.SplatcraftBlocks.stageBarrier;
import static net.splatcraft.forge.registries.SplatcraftBlocks.stageVoid;
import static net.splatcraft.forge.registries.SplatcraftBlocks.sunkenCrate;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class SplatcraftTileEntitites
{

    private static final List<TileEntityType<?>> te_registry = new ArrayList<>();
    public static final TileEntityType<InkColorTileEntity> colorTileEntity = registerTileEntity("color", InkColorTileEntity::new, inkedWool, canvas, splatSwitch);
    public static final TileEntityType<InkwellTileEntity> inkwellTileEntity = registerTileEntity("inkwell", InkwellTileEntity::new, inkwell);
    public static final TileEntityType<InkedBlockTileEntity> inkedTileEntity = registerTileEntity("inked_block", InkedBlockTileEntity::new, inkedBlock, glowingInkedBlock, clearInkedBlock);
    public static final TileEntityType<CrateTileEntity> crateTileEntity = registerTileEntity("crate", CrateTileEntity::new, crate, sunkenCrate);
    public static final TileEntityType<StageBarrierTileEntity> stageBarrierTileEntity = registerTileEntity("stage_barrier", StageBarrierTileEntity::new, stageBarrier, stageVoid);
    public static final TileEntityType<ColoredBarrierTileEntity> colorBarrierTileEntity = registerTileEntity("color_barrier", ColoredBarrierTileEntity::new, allowedColorBarrier, deniedColorBarrier);
    public static final TileEntityType<InkVatTileEntity> inkVatTileEntity = registerTileEntity("ink_vat", InkVatTileEntity::new, inkVat);
    public static final TileEntityType<RemotePedestalTileEntity> remotePedestalTileEntity = registerTileEntity("remote_pedestal", RemotePedestalTileEntity::new, remotePedestal);

    private static final List<ContainerType<?>> c_registry = new ArrayList<>();

    public static final ContainerType<InkVatContainer> inkVatContainer = registerContainer("ink_vat", InkVatContainer::new);
    public static final ContainerType<WeaponWorkbenchContainer> weaponWorkbenchContainer = registerMenu("weapon_workbench", WeaponWorkbenchContainer::new);

    @SuppressWarnings("ConstantConditions")
    private static <T extends TileEntity> TileEntityType<T> registerTileEntity(String name, Supplier<T> factoryIn, Block... allowedBlocks)
    {
        TileEntityType<T> te = TileEntityType.Builder.of(factoryIn, allowedBlocks).build(null);
        te.setRegistryName(name);
        te_registry.add(te);
        return te;
    }

    private static <T extends Container> ContainerType<T> registerContainer(String name, IContainerFactory<T> factoryIn)
    {
        ContainerType<T> container = IForgeContainerType.create(factoryIn);
        container.setRegistryName(name);
        c_registry.add(container);
        return container;
    }

    private static <T extends Container> ContainerType<T> registerMenu(String name, ContainerType.IFactory<T> factory)
    {
        ContainerType<T> container = new ContainerType<>(factory);
        container.setRegistryName(name);
        c_registry.add(container);
        return container;
    }

    @SubscribeEvent
    public static void tileEntityInit(final RegistryEvent.Register<TileEntityType<?>> event)
    {
        IForgeRegistry<TileEntityType<?>> registry = event.getRegistry();
        te_registry.forEach(registry::register);
    }

    @SubscribeEvent
    public static void containerInit(final RegistryEvent.Register<ContainerType<?>> event)
    {
        IForgeRegistry<ContainerType<?>> registry = event.getRegistry();

        c_registry.forEach(registry::register);
    }

    public static void bindTESR()
    {
        ClientRegistry.bindTileEntityRenderer(inkedTileEntity, InkedBlockTileEntityRenderer::new);
        ClientRegistry.bindTileEntityRenderer(stageBarrierTileEntity, StageBarrierTileEntityRenderer::new);
        ClientRegistry.bindTileEntityRenderer(colorBarrierTileEntity, StageBarrierTileEntityRenderer::new);
        ClientRegistry.bindTileEntityRenderer(remotePedestalTileEntity, RemotePedestalTileEntityRenderer::new);
    }
}
