package org.millenaire.common.pathing;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.PathNavigationRegion;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

public class MillWalkNodeEvaluator extends WalkNodeEvaluator {
    
    private PathNavigationRegion level;
    private Mob mob;
    
    @Override
    public void prepare(PathNavigationRegion level, Mob mob) {
        super.prepare(level, mob);
        this.level = level;
        this.mob = mob;
    }
    
    @Override
    @Nullable
    public Node getStart() {
        // Retourne le point de départ basé sur la position de l'entité
        BlockPos pos = this.mob.blockPosition();
        return this.getNode(pos.getX(), pos.getY(), pos.getZ());
    }
    
    @Override
    @Nullable
    public Target getGoal(double x, double y, double z) {
        // Crée une cible pour la position spécifiée
        return new Target(this.getNode((int) x, (int) y, (int) z));
    }
    
    @Override
    public int getNeighbors(Node[] neighbors, Node currentNode) {
        int count = 0;
        int verticalOffset = 0;

        // Vérifier si l'entité peut monter d'un bloc
        if (this.getVerticalOffset(currentNode.x, currentNode.y + 1, currentNode.z) == 1) {
            verticalOffset = 1;
        }

        // Points cardinaux
        Node north = this.getMillSafePoint(currentNode.x, currentNode.y, currentNode.z + 1, verticalOffset);
        Node west = this.getMillSafePoint(currentNode.x - 1, currentNode.y, currentNode.z, verticalOffset);
        Node east = this.getMillSafePoint(currentNode.x + 1, currentNode.y, currentNode.z, verticalOffset);
        Node south = this.getMillSafePoint(currentNode.x, currentNode.y, currentNode.z - 1, verticalOffset);

        // Ajouter les voisins valides
        if (north != null && !north.closed && this.isValidNeighbor(currentNode, north)) {
            neighbors[count++] = north;
        }

        if (west != null && !west.closed && this.isValidNeighbor(currentNode, west)) {
            neighbors[count++] = west;
        }

        if (east != null && !east.closed && this.isValidNeighbor(currentNode, east)) {
            neighbors[count++] = east;
        }

        if (south != null && !south.closed && this.isValidNeighbor(currentNode, south)) {
            neighbors[count++] = south;
        }

        return count;
    }

    /**
     * Vérifie si un voisin est valide pour le chemin
     */
    private boolean isValidNeighbor(Node from, Node to) {
        // Vérifier la distance et autres contraintes
        double distance = Math.sqrt(
            Math.pow(to.x - from.x, 2) + 
            Math.pow(to.y - from.y, 2) + 
            Math.pow(to.z - from.z, 2)
        );
        return distance <= 1.5; // Distance maximale entre nœuds
    }

    /**
     * Retourne un point sûr pour le déplacement
     */
    @Nullable
    private Node getMillSafePoint(int x, int y, int z, int verticalOffset) {
        Node node = null;
        int verticalCheck = this.getVerticalOffset(x, y, z);

        if (verticalCheck == 2) {
            return this.getNode(x, y, z);
        } else {
            if (verticalCheck == 1) {
                node = this.getNode(x, y, z);
            }

            if (node == null && verticalOffset > 0 && verticalCheck != -3 && verticalCheck != -4 && 
                this.getVerticalOffset(x, y + verticalOffset, z) == 1) {
                node = this.getNode(x, y + verticalOffset, z);
                y += verticalOffset;
            }

            if (node != null) {
                int fallHeight = 0;
                int blockCheck;

                // Vérifier la descente
                for (blockCheck = 0; y > 0; node = this.getNode(x, y, z)) {
                    blockCheck = this.getVerticalOffset(x, y - 1, z);

                    if (!this.canFloat() && blockCheck == -1) {
                        return null;
                    }

                    if (blockCheck != 1) {
                        break;
                    }

                    // Utiliser la hauteur de chute maximale de l'entité
                    if (fallHeight++ >= this.mob.getMaxFallHeight()) {
                        return null;
                    }

                    --y;

                    if (y <= 0) {
                        return null;
                    }
                }

                if (blockCheck == -2) {
                    return null;
                }
            }

            return node;
        }
    }

    /**
     * Vérifie les décalages verticaux pour la navigation
     * Retourne:
     * 1 = Libre
     * 0 = Bloc solide
     * -1 = Eau (si évitement activé)
     * -2 = Lave
     * -3 = Barrière/Clôture
     * -4 = Trappe fermée
     * 2 = Libre sauf pour trappe ouverte/eau
     */
    private int getVerticalOffset(int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        
        // Vérifier les portails (remplacement des portes de clôture)
        if (this.level.getBlockState(pos).getBlock() instanceof FenceGateBlock) {
            if (this.level.isEmptyBlock(pos.above())) {
                return 2;
            }
        }
        
        // Utiliser la logique de vérification des blocs de base
        return this.checkBlockValidity(x, y, z);
    }
    
    /**
     * Vérifie la validité d'un bloc pour la navigation
     */
    private int checkBlockValidity(int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        BlockPos belowPos = pos.below();
        
        // Vérifier si le bloc est solide en dessous
        if (!this.level.getBlockState(belowPos).isSolid()) {
            return 0; // Pas solide
        }
        
        // Vérifier l'espace pour l'entité
        AABB mobBounds = this.mob.getBoundingBox();
        AABB checkBounds = new AABB(
            pos.getX(), pos.getY(), pos.getZ(),
            pos.getX() + 1, pos.getY() + mobBounds.getYsize(), pos.getZ() + 1
        );
        
        if (!this.level.noCollision(this.mob, checkBounds)) {
            return 0; // Collision
        }
        
        // Vérifier l'eau
        if (this.level.getBlockState(pos).liquid() && !this.canFloat()) {
            return -1; // Eau à éviter
        }
        
        // Vérifier la lave
        if (this.level.getBlockState(pos).getBlock() == Blocks.LAVA) {
            return -2; // Lave
        }
        
        // Vérifier les barrières et clôtures
        if (this.level.getBlockState(pos).getBlock() instanceof net.minecraft.world.level.block.FenceBlock ||
            this.level.getBlockState(pos).getBlock() instanceof net.minecraft.world.level.block.WallBlock) {
            return -3; // Barrière/Clôture
        }
        
        // Vérifier les trappes fermées
        if (this.level.getBlockState(pos).getBlock() instanceof net.minecraft.world.level.block.TrapDoorBlock &&
            !this.level.getBlockState(pos).getValue(net.minecraft.world.level.block.TrapDoorBlock.OPEN)) {
            return -4; // Trappe fermée
        }
        
        return 1; // Libre
    }
    
    @Override
    protected boolean hasValidPathType(@Nullable Node node) {
        // Logique personnalisée pour les types de chemins valides
        if (node == null || node.closed) {
            return false;
        }
        
        // Vérifier le type de chemin
        BlockPathTypes pathType = this.level.getBlockPathTypes(node.x, node.y, node.z);
        return pathType != BlockPathTypes.BLOCKED && pathType != BlockPathTypes.TRAPDOOR;
    }
    
    @Override
    public void done() {
        super.done();
        // Nettoyage
        this.level = null;
        this.mob = null;
    }
    
    /**
     * Récupère un nœud à la position spécifiée
     */
    @Nullable
    private Node getNode(int x, int y, int z) {
        // Créer un nouveau nœud avec la position
        Node node = new Node(x, y, z);
        
        // Déterminer le coût du nœud
        node.costMalus = this.getMobPathingMalus(this.level, this.mob, x, y, z);
        
        return node;
    }
    
    /**
     * Calcule le coût de pathfinding pour une position
     */
    private float getMobPathingMalus(PathNavigationRegion level, Mob mob, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        float malus = 0.0F;
        
        // Coût de base basé sur le type de bloc
        BlockPathTypes pathType = level.getBlockPathTypes(x, y, z);
        if (pathType != null) {
            malus += pathType.getMalus();
        }
        
        // Pénalités supplémentaires
        if (level.getBlockState(pos).liquid()) {
            malus += 2.0F; // Pénalité pour les liquides
        }
        
        return malus;
    }
}