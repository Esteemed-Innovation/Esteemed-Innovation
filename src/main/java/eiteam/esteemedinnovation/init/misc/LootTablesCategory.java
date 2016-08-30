package eiteam.esteemedinnovation.init.misc;

import eiteam.esteemedinnovation.EsteemedInnovation;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

public class LootTablesCategory extends MiscellaneousCategory {
    public enum LootTables {
        WORKED_OUT_ORE_DEPOSIT_TABLE("worked_out_ore_deposit");

        private ResourceLocation loc;

        LootTables(String name) {
            loc = new ResourceLocation(EsteemedInnovation.MOD_ID, name);
        }

        public ResourceLocation getResource() {
            return loc;
        }
    }

    @Override
    public void init(FMLInitializationEvent event) {
        for (LootTables table : LootTables.values()) {
            LootTableList.register(table.getResource());
        }
    }
}
