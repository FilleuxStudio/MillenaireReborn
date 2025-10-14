package org.millenaire.common.events;

import org.millenaire.PlayerTracker;

import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityEvent;

@EventBusSubscriber(modid = "millenaire", bus = EventBusSubscriber.Bus.GAME)
public class MillenaireEventHandler {

    @SubscribeEvent
    public static void onEntityConstructing(EntityEvent.EntityConstructing event) {
        if (event.getEntity() instanceof Player player && PlayerTracker.get(player) == null) {
            PlayerTracker.register(player);
        }
    }
}