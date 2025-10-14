package org.millenaire.client.rendering;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.Entity;

public class ModelFemaleSym extends HumanoidModel<Entity> {
    
    public ModelFemaleSym(ModelPart root) {
        super(root);
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F);
        PartDefinition partdefinition = meshdefinition.getRoot();

        partdefinition.addOrReplaceChild("head", 
            CubeListBuilder.create()
                .texOffs(0, 0)
                .addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8),
            PartPose.offset(0.0F, 0.0F, 0.0F));

        partdefinition.addOrReplaceChild("hat", 
            CubeListBuilder.create()
                .texOffs(32, 0)
                .addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, new CubeDeformation(0.5F)),
            PartPose.offset(0.0F, 0.0F, 0.0F));

        partdefinition.addOrReplaceChild("body", 
            CubeListBuilder.create()
                .texOffs(18, 17)
                .addBox(-3.5F, 0.0F, -1.5F, 7, 12, 3),
            PartPose.offset(0.0F, 0.0F, 0.0F));

        // Partie poitrine ajoutée
        partdefinition.addOrReplaceChild("breast", 
            CubeListBuilder.create()
                .texOffs(18, 18)
                .addBox(-3.5F, 0.75F, -3.0F, 7, 4, 2),
            PartPose.offset(0.0F, 0.0F, 0.0F));

        partdefinition.addOrReplaceChild("right_arm", 
            CubeListBuilder.create()
                .texOffs(40, 17)
                .addBox(-1.5F, -2.0F, -1.5F, 3, 12, 4),
            PartPose.offset(-5.0F, 2.0F, 0.0F));

        partdefinition.addOrReplaceChild("left_arm", 
            CubeListBuilder.create()
                .texOffs(40, 17)
                .mirror()
                .addBox(-1.5F, -2.0F, -1.5F, 3, 12, 4),
            PartPose.offset(5.0F, 2.0F, 0.0F));

        partdefinition.addOrReplaceChild("right_leg", 
            CubeListBuilder.create()
                .texOffs(0, 16)
                .addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4),
            PartPose.offset(-2.0F, 12.0F, 0.0F));

        partdefinition.addOrReplaceChild("left_leg", 
            CubeListBuilder.create()
                .texOffs(0, 16)
                .mirror()
                .addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4),
            PartPose.offset(2.0F, 12.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 32);
    }

    @Override
    public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
    }
}