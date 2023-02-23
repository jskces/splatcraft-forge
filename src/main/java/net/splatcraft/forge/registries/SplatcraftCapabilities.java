package net.splatcraft.forge.registries;

import net.minecraft.world.chunk.Chunk;
import net.splatcraft.forge.Splatcraft;
import net.splatcraft.forge.data.capabilities.blockcolorinfo.ColorInfoCapability;
import net.splatcraft.forge.data.capabilities.inkoverlay.InkOverlayCapability;
import net.splatcraft.forge.data.capabilities.playerinfo.PlayerInfoCapability;
import net.splatcraft.forge.data.capabilities.saveinfo.SaveInfoCapability;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.splatcraft.forge.data.capabilities.worldinfo.WorldInfoCapability;

@Mod.EventBusSubscriber(modid = Splatcraft.MODID)
public class SplatcraftCapabilities
{
    public static void registerCapabilities()
    {
        PlayerInfoCapability.register();
        SaveInfoCapability.register();
        InkOverlayCapability.register();
        ColorInfoCapability.register();
        WorldInfoCapability.register();
    }

    @SubscribeEvent
    public static void attachEntityCapabilities(final AttachCapabilitiesEvent<Entity> event)
    {
        if (event.getObject() instanceof PlayerEntity)
        {
            event.addCapability(new ResourceLocation(Splatcraft.MODID, "player_info"), new PlayerInfoCapability());
        }
        event.addCapability(new ResourceLocation(Splatcraft.MODID, "ink_overlay"), new InkOverlayCapability());

    }

    @SubscribeEvent
    public static void attachWorldCapabilities(final AttachCapabilitiesEvent<World> event)
    {
        if (event.getObject().dimension() == World.OVERWORLD)
            event.addCapability(new ResourceLocation(Splatcraft.MODID, "save_info"), new SaveInfoCapability());
        event.addCapability(new ResourceLocation(Splatcraft.MODID, "world_info"), new WorldInfoCapability());
    }

    @SubscribeEvent
    public static void attachChunkCapabilities(final AttachCapabilitiesEvent<Chunk> event)
    {
        if(!event.getObject().getCapability(ColorInfoCapability.CAPABILITY).isPresent())
            event.addCapability(new ResourceLocation(Splatcraft.MODID, "color_info"), new ColorInfoCapability());
    }
}
