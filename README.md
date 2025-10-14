# 🏰 Millénaire Reborn (Migration vers Minecraft 1.21.x)



Une **initiative personnelle** visant à migrer le célèbre mod **Millénaire** (créé par **Kinniken**) de sa dernière version stable (1.12.2) vers **Minecraft 1.21.10** en utilisant **NeoForge**.

Ce projet est motivé par la passion de voir ce mod classique, pour sa simplicité et sa profondeur, revivre sur les versions modernes du jeu.

## 👩‍💻 L'Histoire de ce Projet (Mon Défi)

Je me suis lancée seule dans cette migration complexe. L'objectif est de reconstruire les fondations du mod pour qu'il fonctionne avec les nouvelles APIs de Minecraft 1.21.10, tout en préservant l'esprit et les fonctionnalités d'origine.

### 🎯 Objectif Actuel

J'ai réussi la migration initiale de tous les fichiers et *assets*. Le défi actuel est de corriger les nombreuses erreurs de compilation et de *runtime* sur Java SDK 21, dues aux changements majeurs dans les APIs de Minecraft entre les versions 1.12.2 et 1.21.10.

## 🛠️ État Actuel du Développement

| Catégorie | État | Notes |
| :--- | :--- | :--- |
| **Migration des Fichiers** | ✅ Terminée | Tous les fichiers source et *assets* ont été transférés. |
| **Erreurs de Code Java** | ⚠️ En cours | De nombreuses erreurs subsistent, nécessitant une réécriture des appels d'API. |
| **Première Compilation** | ❌ Échec | Le projet ne compile pas encore à cause des erreurs Java. |

## ⚙️ Détails Techniques

### Version Cible

* **Minecraft :** 1.21.10
* **Mod Loader :** NeoForge
* **SDK :** **Java SDK 21**

### 🔗 Repositories Source

J'ai utilisé et migré le code des sources suivantes :

| Repository | Rôle |
| :--- | :--- |
| **Base du Mod** | **`MoonCutter2B/Millenaire`** (Code source original 1.12.2) |
| **Base NeoForge** | **`NeoForgeMDKs/MDK-1.21.10-NeoGradle`** |

## 🤝 Aide et Soutien (Bienvenue !)

Bien que ce soit un projet solo, toute aide est la bienvenue ! La migration de code entre des versions majeures est un défi de taille.

Si vous avez de l'expérience en développement de mods Minecraft (surtout avec NeoForge ou Forge), et que vous souhaitez donner un coup de main, n'hésitez pas :

1.  **Fork** ce dépôt.
2.  **Créer une *Pull Request*** (PR) détaillée vers la branche `main` pour toute correction de bug ou refonte d'API.

Vos contributions accéléreront la renaissance de Millénaire !

## 📝 Licence

Ce projet s'inscrit dans la continuité du mod original Millénaire. Veuillez vous référer à la licence présente dans le code source original pour les détails légaux.

---
*Développé par FilleuxStudio*