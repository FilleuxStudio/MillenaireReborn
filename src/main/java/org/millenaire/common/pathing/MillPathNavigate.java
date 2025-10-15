package org.millenaire.common.pathing;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.PathFinder;

public class MillPathNavigate extends GroundPathNavigation {
    
    public MillPathNavigate(Mob mob, Level level) {
        super(mob, level);
        this.setCanOpenDoors(true);
        this.setCanPassDoors(true);
        this.setCanFloat(false); // Équivalent à setAvoidsWater(true)
    }
    
    @Override
    protected PathFinder createPathFinder(int maxVisitedNodes) {
        // Créer l'évaluateur de nœuds personnalisé
        MillWalkNodeEvaluator nodeEvaluator = new MillWalkNodeEvaluator();
        nodeEvaluator.setCanPassDoors(true);
        nodeEvaluator.setCanOpenDoors(true);
        nodeEvaluator.setCanFloat(false); // Éviter l'eau
        
        return new PathFinder(nodeEvaluator, maxVisitedNodes);
    }
    
    @Override
    public boolean isStableDestination(BlockPos pos) {
        // Logique personnalisée pour les destinations stables
        return this.level.getBlockState(pos.below()).isSolid();
    }
}