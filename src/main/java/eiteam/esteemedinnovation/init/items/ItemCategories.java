package eiteam.esteemedinnovation.init.items;

import eiteam.esteemedinnovation.init.InitCategory;
import eiteam.esteemedinnovation.init.items.armor.ArmorItems;
import eiteam.esteemedinnovation.init.items.armor.ExosuitUpgradeItems;
import eiteam.esteemedinnovation.init.items.firearms.FirearmAmmunitionItems;
import eiteam.esteemedinnovation.init.items.firearms.FirearmItems;
import eiteam.esteemedinnovation.init.items.firearms.FirearmUpgradeItems;
import eiteam.esteemedinnovation.init.items.tools.GadgetItems;
import eiteam.esteemedinnovation.init.items.tools.ToolItems;
import eiteam.esteemedinnovation.init.items.tools.ToolUpgradeItems;

public enum ItemCategories {
    COMPONENTS(new CraftingComponentItems()),
    FIREARMS(new FirearmItems()),
    FIREARM_AMMUNITION(new FirearmAmmunitionItems()),
    FIREARM_UPGRADES(new FirearmUpgradeItems()),
    METALCASTING(new MetalcastingItems()),
    ARMOR(new ArmorItems()),
    EXOSUIT_UPGRADES(new ExosuitUpgradeItems()),
    TOOLS(new ToolItems()),
    TOOL_UPGRADES(new ToolUpgradeItems()),
    FOOD(new FoodItems()),
    METALS(new MetalItems()),
    GADGETS(new GadgetItems()),
    NATURAL_PHILOSOPHY(new NaturalPhilosophyItems());

    private InitCategory category;

    ItemCategories(InitCategory category) {
        this.category = category;
    }

    public InitCategory getCategory() {
        return category;
    }
}
