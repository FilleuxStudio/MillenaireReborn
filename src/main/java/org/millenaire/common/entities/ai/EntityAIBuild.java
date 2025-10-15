package org.millenaire.common.entities.ai;


import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.Mob;
import org.millenaire.common.entities.EntityMillVillager;
import org.millenaire.VillagerType;

import java.util.EnumSet;

public class EntityAIBuild extends Goal {
    private final EntityMillVillager villager;
    
    public EntityAIBuild(EntityMillVillager villager) {
        this.villager = villager;
        
        // Validation du type de villageois
        VillagerType villagerType = villager.getVillagerType();
        if (villagerType == null || !villagerType.canBuild) {
            throw new IllegalArgumentException("Villager does not support BuildGoal");
        }
        
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        // Conditions pour commencer la construction
        if (villager.isPlayerInteracting || villager.isSleeping()) {
            return false;
        }
        
        // Vérifier s'il y a des projets de construction en attente
        // return hasBuildingProjects();
        return false; // Temporaire
    }

    @Override
    public boolean canContinueToUse() {
        return canUse() && !villager.isPlayerInteracting;
    }

    @Override
    public void start() {
        // Initialiser la construction
        // targetBuilding = getNextBuildingProject();
        villager.getNavigation().stop();
    }

    @Override
    public void stop() {
        // Nettoyer les ressources
        // resetBuildingState();
    }

    @Override
    public void tick() {
        if (villager.level().isClientSide()) {
            return;
        }
        
        // Logique de construction
        executeBuildingLogic();
    }

    private void executeBuildingLogic() {
        // Implémentation de la logique de construction
        // 1. Se déplacer vers le chantier
        // 2. Collecter les ressources nécessaires
        // 3. Placer les blocs
        // 4. Gérer la progression
        
        /* Exemple de structure :
        if (!isAtBuildSite()) {
            moveToBuildSite();
            return;
        }
        
        if (needsResources()) {
            collectResources();
            return;
        }
        
        placeNextBlock();
        updateBuildingProgress();
        */
    }

    // Méthodes auxiliaires pour la construction
    private boolean isAtBuildSite() {
        // return villager.distanceToSqr(targetBuilding.getPos()) < 4.0;
        return true;
    }

    private void moveToBuildSite() {
        // villager.getNavigation().moveTo(targetBuilding.getPos().getX(), targetBuilding.getPos().getY(), targetBuilding.getPos().getZ(), 0.6);
    }

    private boolean needsResources() {
        // return !hasRequiredResources();
        return false;
    }

    private void collectResources() {
        // Logique de collecte des ressources
    }

    private void placeNextBlock() {
        // Logique de placement des blocs
    }
}