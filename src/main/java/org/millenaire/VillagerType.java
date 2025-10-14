package org.millenaire;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// L'import AI a changé
import net.minecraft.world.entity.ai.goal.Goal; // EntityAIBase → Goal

public class VillagerType {
    final public String id;
    final private String nativeName;
    final private int gender; // 0=male, 1=female, 2=Sym Female
    final private String[] familyNames;
    final private String[] firstNames;
    final public String[] textures;
    final public boolean isChief;
    final public boolean canBuild;
    final public int hireCost;

    private List<Goal> additionalTasks = new ArrayList<>(); // EntityAIBase → Goal

    public VillagerType(String idIn, String nameIn, int genderIn, String[] familyIn, 
                       String[] firstIn, String[] textureIn, boolean chiefIn, 
                       boolean buildIn, int hireIn) {
        id = idIn;
        nativeName = nameIn;
        gender = genderIn;
        familyNames = familyIn;
        firstNames = firstIn;
        textures = textureIn;
        isChief = chiefIn;
        canBuild = buildIn;
        hireCost = hireIn;
    }
    
    public VillagerType addAI(Goal taskIn) { // EntityAIBase → Goal
        this.additionalTasks.add(taskIn);
        return this;
    }
    
    public String getTexture() { 
        return textures[new Random().nextInt(textures.length)]; 
    }
    
    // Getters pour les noms
    public String getRandomFirstName() {
        return firstNames[new Random().nextInt(firstNames.length)];
    }
    
    public String getRandomFamilyName() {
        return familyNames[new Random().nextInt(familyNames.length)];
    }
}