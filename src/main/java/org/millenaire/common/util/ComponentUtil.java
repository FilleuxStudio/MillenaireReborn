package org.millenaire.common.util;

// Import de base de Minecraft pour le système de texte
import net.minecraft.network.chat.Component;

/**
 * Utility class for creating Minecraft Text Components (Minecraft 1.21+).
 * This replaces the usage of ChatComponentText from older Minecraft versions
 * and simplifies the creation of simple literal Components (plain text).
 * * Classe utilitaire pour la création de Composants de Texte Minecraft (Minecraft 1.21+).
 * Remplace l'usage de ChatComponentText des anciennes versions de Minecraft
 * et simplifie la création de Components littéraux simples (texte brut).
 */
public class ComponentUtil 
{
    /**
     * Creates a simple literal Component (plain text) from a String.
     * Crée un simple Component littéral (texte brut) à partir d'une chaîne.
     * * @param text The string content. / Le contenu de la chaîne de caractères.
     * @return A Component object. / Un objet Component.
     */
    public static Component literal(String text) 
    {
        return Component.literal(text);
    }

    // D'autres méthodes peuvent être ajoutées ici au besoin (ex: styled, translated, etc.).
}
