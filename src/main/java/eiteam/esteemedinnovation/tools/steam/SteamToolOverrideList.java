package eiteam.esteemedinnovation.tools.steam;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import eiteam.esteemedinnovation.api.tool.SteamTool;
import eiteam.esteemedinnovation.api.tool.SteamToolUpgrade;
import eiteam.esteemedinnovation.api.tool.UtilSteamTool;
import eiteam.esteemedinnovation.commons.util.JavaHelper;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.SimpleModelState;

import java.util.List;

public final class SteamToolOverrideList extends ItemOverrideList {
    public static final ItemOverrideList INSTANCE = new SteamToolOverrideList();

    private SteamToolOverrideList() {
        super(ImmutableList.of());
    }

    @Override
    public IBakedModel handleItemState(IBakedModel originalModel, ItemStack stack, World world, EntityLivingBase entity) {
        List<SteamToolUpgrade> upgrades = UtilSteamTool.getUpgrades(stack);
        // TODO: Improve the API here with a new "resourceSuffix" method in SteamTool instead of using the toolClass.
        String toolName = "drill";
        Item item = stack.getItem();
        if (item instanceof SteamTool) {
            SteamTool tool = (SteamTool) item;
            String toolClass = tool.toolClass();
            if (!"pickaxe".equalsIgnoreCase(toolClass)) {
                toolName = toolClass;
            }
        }

        ImmutableMap.Builder<String, String> builder = new ImmutableMap.Builder<>();
        builder.put("tool", toolName);
        ResourceLocation core = null;
        ResourceLocation head = null;

        for (SteamToolUpgrade upgrade : upgrades) {
            ResourceLocation baseIcon = upgrade.getBaseIcon();
            if (baseIcon == null) {
                continue;
            }
            if (upgrade.getToolSlot().slot == 1) {
                core = baseIcon;
                builder.put("base_core", core.toString());
            } else {
                head = baseIcon;
                builder.put("head_core", head.toString());
            }
        }

        int which = stack.hasTagCompound() && stack.getTagCompound().hasKey("Ticks") && stack.getTagCompound().getInteger("Ticks") > 50 ? 0 : 1;
        builder.put("which", Integer.toString(which));

        ImmutableMap<String, String> map = builder.build();
        String mapString = map.toString();

        SteamToolBakedModel steamToolBakedModel = (SteamToolBakedModel) originalModel;
        if (steamToolBakedModel.cache.containsKey(mapString)) {
            return steamToolBakedModel.cache.get(mapString);
        }

        ResourceLocation trueCore = core == null ? null : new ResourceLocation(core.getResourceDomain(),
          core.getResourcePath() + JavaHelper.capitalize(toolName) + which);
        ResourceLocation trueHead = head == null ? null : new ResourceLocation(head.getResourceDomain(),
          head.getResourcePath() + JavaHelper.capitalize(toolName) + which);

        IModel processed = new SteamToolModel(trueCore, trueHead, which, toolName);

        IBakedModel bakedModel = processed.bake(new SimpleModelState(steamToolBakedModel.transforms), steamToolBakedModel.format, ModelLoader.defaultTextureGetter());
        steamToolBakedModel.cache.put(mapString, bakedModel);

        return bakedModel;
    }
}
