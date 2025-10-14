package org.millenaire.common.pathing;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.PathNavigationRegion;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.Target;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;

public class MillWalkNodeProcessor extends WalkNodeEvaluator
{
    @Override
    public int getNeighbors(Node[] neighbors, Node currentNode, Target target, float maxDistance)
    {
        int i = 0;
        int j = 0;

        if (this.getMillVerticalOffset(this.mob, currentNode.x, currentNode.y + 1, currentNode.z) == 1)
        {
            j = 1;
        }

        Node node = this.getMillSafePoint(this.mob, currentNode.x, currentNode.y, currentNode.z + 1, j);
        Node node1 = this.getMillSafePoint(this.mob, currentNode.x - 1, currentNode.y, currentNode.z, j);
        Node node2 = this.getMillSafePoint(this.mob, currentNode.x + 1, currentNode.y, currentNode.z, j);
        Node node3 = this.getMillSafePoint(this.mob, currentNode.x, currentNode.y, currentNode.z - 1, j);

        if (node != null && !node.closed() && node.distanceTo(target) < maxDistance)
        {
            neighbors[i++] = node;
        }

        if (node1 != null && !node1.closed() && node1.distanceTo(target) < maxDistance)
        {
            neighbors[i++] = node1;
        }

        if (node2 != null && !node2.closed() && node2.distanceTo(target) < maxDistance)
        {
            neighbors[i++] = node2;
        }

        if (node3 != null && !node3.closed() && node3.distanceTo(target) < maxDistance)
        {
            neighbors[i++] = node3;
        }

        return i;
    }

    /**
     * Returns a point that the entity can safely move to
     */
    private Node getMillSafePoint(Entity entityIn, int x, int y, int z, int verticalOffset)
    {
        Node node = null;
        int i = this.getMillVerticalOffset(entityIn, x, y, z);

        if (i == 2)
        {
            return this.getNode(x, y, z);
        }
        else
        {
            if (i == 1)
            {
                node = this.getNode(x, y, z);
            }

            if (node == null && verticalOffset > 0 && i != -3 && i != -4 && this.getMillVerticalOffset(entityIn, x, y + verticalOffset, z) == 1)
            {
                node = this.getNode(x, y + verticalOffset, z);
                y += verticalOffset;
            }

            if (node != null)
            {
                int j = 0;
                int k;

                for (k = 0; y > 0; node = this.getNode(x, y, z))
                {
                    k = this.getMillVerticalOffset(entityIn, x, y - 1, z);

                    if (this.canFloat() && k == -1)
                    {
                        return null;
                    }

                    if (k != 1)
                    {
                        break;
                    }

                    if (j++ >= entityIn.getMaxFallDistance())
                    {
                        return null;
                    }

                    --y;

                    if (y <= 0)
                    {
                        return null;
                    }
                }

                if (k == -2)
                {
                    return null;
                }
            }

            return node;
        }
    }

    /**
     * Checks if an entity collides with blocks at a position.
     * Returns 1 if clear, 0 for colliding with any solid block, -1 for water(if avoids water),
     * -2 for lava, -3 for fence and wall, -4 for closed trapdoor, 2 if otherwise clear except for open trapdoor or
     * water(if not avoiding)
     */
    private int getMillVerticalOffset(Entity entityIn, int x, int y, int z)
    {
        BlockPos pos = new BlockPos(x, y, z);
        
        if (!(this.level instanceof PathNavigationRegion navigationRegion)) {
            return 0;
        }
        
        Block block = navigationRegion.getBlockState(pos).getBlock();

        return block instanceof FenceGateBlock && navigationRegion.isEmptyBlock(pos.above()) ? 2 :
                getBlockPathTypeStatic(navigationRegion, entityIn, x, y, z);
    }
}