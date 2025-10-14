package org.millenaire.events; 
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod; // Importez Mod si vous enregistrez le gestionnaire dans la classe principale
import net.neoforged.neoforge.event.TickEvent; // Nouvel import pour les événements de Tick

// Enregistrez cette classe dans votre mod bus dans la classe principale (Millenaire.java)
// Exemple: eventBus.register(new RaidEventHandler());
// Si vous utilisez la classe principale pour tous les événements, vous pouvez intégrer cette logique directement là-bas.

public class RaidEventHandler 
{
    /**
     * Gère les événements survenant à chaque tick côté serveur.
     * * @param event L'événement ServerTickEvent.
     */
    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void onServerTick(TickEvent.ServerTickEvent event)
    {
        // NeoForge sépare les ticks de DÉBUT et de FIN. 
        // L'action principale doit généralement se produire à la fin du tick.
        if (event.phase == TickEvent.Phase.END) {
            
            // Remplacez cette ligne par l'appel de votre logique de raid, par exemple :
            // Millenaire.instance.getVillageList().updateRaids();
        }
    }
}