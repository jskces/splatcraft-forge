package net.splatcraft.forge.client.layer;

import net.splatcraft.forge.Splatcraft;
import net.splatcraft.forge.SplatcraftConfig;
import net.splatcraft.forge.client.model.InkSquidModel;
import net.splatcraft.forge.util.ColorUtils;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;

public class InkSquidColorLayer extends LayerRenderer<LivingEntity, InkSquidModel>
{
    private static final ResourceLocation TEXTURE = new ResourceLocation(Splatcraft.MODID, "textures/entity/ink_squid.png");
    private final InkSquidModel MODEL = new InkSquidModel();

    public InkSquidColorLayer(IEntityRenderer<LivingEntity, InkSquidModel> renderer)
    {
        super(renderer);
    }


    @Override
    public void render(MatrixStack matrixStack, IRenderTypeBuffer iRenderTypeBuffer, int i, LivingEntity entity, float v, float v1, float v2, float v3, float v4, float v5)
    {
        int color = ColorUtils.getEntityColor(entity);
        if (SplatcraftConfig.Client.getColorLock())
        {
            color = ColorUtils.getLockedColor(color);
        }
        float r = ((color & 16711680) >> 16) / 255.0f;
        float g = ((color & '\uff00') >> 8) / 255.0f;
        float b = (color & 255) / 255.0f;

        coloredCutoutModelCopyLayerRender(getParentModel(), MODEL, TEXTURE, matrixStack, iRenderTypeBuffer, i, entity, v, v1, v3, v4, v5, v2, r, g, b);
    }


}
