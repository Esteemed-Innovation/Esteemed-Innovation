package flaxbeard.steamcraft.init.items;

import flaxbeard.steamcraft.init.IInitCategory;
import flaxbeard.steamcraft.init.items.armor.ArmorItems;
import flaxbeard.steamcraft.init.items.armor.ExosuitUpgradeItems;
import flaxbeard.steamcraft.init.items.firearms.FirearmAmmunitionItems;
import flaxbeard.steamcraft.init.items.firearms.FirearmItems;
import flaxbeard.steamcraft.init.items.firearms.FirearmUpgradeItems;
import flaxbeard.steamcraft.init.items.tools.GadgetItems;
import flaxbeard.steamcraft.init.items.tools.ToolItems;
import flaxbeard.steamcraft.init.items.tools.ToolUpgradeItems;

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
    GADGETS(new GadgetItems());

    private IInitCategory category;

    ItemCategories(IInitCategory category) {
        this.category = category;
    }

    public IInitCategory getCategory() {
        return category;
    }
}
