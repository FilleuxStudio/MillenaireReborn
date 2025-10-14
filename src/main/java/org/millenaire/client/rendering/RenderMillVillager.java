package org.millenaire.client.rendering;

import org.millenaire.common.entities.EntityMillVillager;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class RenderMillVillager extends HumanoidMobRenderer<EntityMillVillager, HumanoidModel<EntityMillVillager>> {
    
    private ResourceLocation villagerTexture;
    private String name = "Suzy Carmichael";
    private String quest = null;

    public RenderMillVillager(EntityRendererProvider.Context context) {
        super(context, new PlayerModel<>(context.bakeLayer(ModelLayers.PLAYER), 0.5F);
        this.addLayer(new HumanoidArmorLayer<>(this, 
            new HumanoidModel<>(context.bakeLayer(ModelLayers.PLAYER_INNER_ARMOR)),
            new HumanoidModel<>(context.bakeLayer(ModelLayers.PLAYER_OUTER_ARMOR)),
            context.getModelManager()));
        this.addLayer(new ItemInHandLayer<>(this, context.getItemInHandRenderer()));
    }

    @Override
    public ResourceLocation getTextureLocation(EntityMillVillager entity) {
        villagerTexture = ResourceLocation.parse(entity.getTexture());
        name = entity.getName();
        return villagerTexture;
    }

    @Override
    protected void scale(EntityMillVillager entity, PoseStack poseStack, float partialTickTime) {
        if (!entity.isBaby()) {
            if (entity.getGender() == 1) {
                this.model = new ModelFemaleAsym(this.getModel().root);
            } else if (entity.getGender() == 2) {
                this.model = new ModelFemaleSym(this.getModel().root);
            } else {
                this.model = new HumanoidModel<>(this.getModel().root);
            }
        } else {
            this.model = new HumanoidModel<>(this.getModel().root);
        }
    }

    @Override
    protected void renderNameTag(EntityMillVillager entity, String displayName, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        if (quest != null) {
            this.renderNameTag(entity, quest, poseStack, bufferSource, packedLight, 0.2F);
            poseStack.translate(0.0F, 0.2F, 0.0F);
        }
        this.renderNameTag(entity, name, poseStack, bufferSource, packedLight, 0.0F);
    }

    private void renderNameTag(EntityMillVillager entity, String text, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, float yOffset) {
        double d0 = this.entityRenderDispatcher.distanceToSqr(entity);
        if (d0 > 4096.0D) {
            return;
        }

        poseStack.pushPose();
        poseStack.translate(0.0F, entity.getBbHeight() + 0.5F + yOffset, 0.0F);
        poseStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
        poseStack.scale(-0.025F, -0.025F, 0.025F);

        float backgroundOpacity = 0.25F;
        int textColor = 0xFFFFFF; // Blanc

        if (text.equals(quest)) {
            textColor = 0xAAAAAA; // Gris pour les quêtes
        }

        float textWidth = -this.getFont().width(text) / 2.0F;

        // Fond du texte
        PoseStack.Pose posestack$pose = poseStack.last();
        VertexConsumer vertexconsumer = bufferSource.getBuffer(net.minecraft.client.renderer.RenderType.textIntensitySeeThrough());
        this.getFont().drawInBatch(text, textWidth, 0, textColor, false, posestack$pose.pose(), bufferSource, net.minecraft.client.gui.Font.DisplayMode.NORMAL, 0, packedLight);

        poseStack.popPose();
    }

    @Override
    protected void setupRotations(EntityMillVillager entity, PoseStack poseStack, float ageInTicks, float rotationYaw, float partialTicks) {
        /*if (entity.isAlive() && entity.isSleeping()) {
            float orientation = -entity.getBedOrientation();
            poseStack.mulPose(com.mojang.math.Axis.YP.rotationDegrees(orientation));
            poseStack.mulPose(com.mojang.math.Axis.ZP.rotationDegrees(this.getFlipDegrees(entity)));
            poseStack.mulPose(com.mojang.math.Axis.YP.rotationDegrees(270.0F));
        } else {*/
            super.setupRotations(entity, poseStack, ageInTicks, rotationYaw, partialTicks);
        //}
    }
}