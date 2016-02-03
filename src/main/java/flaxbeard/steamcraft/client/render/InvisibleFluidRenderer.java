package flaxbeard.steamcraft.client.render;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fluids.FluidRegistry;
import flaxbeard.steamcraft.item.ItemThinkingCap;

public class InvisibleFluidRenderer implements ISimpleBlockRenderingHandler {
    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {}

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
        System.out.println("rendering");
        EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        if (player == null) {
            return false;
        }
        System.out.println("player not null");
        ItemStack equipment = player.getEquipmentInSlot(4);
        if (equipment == null || equipment.getItem() == null || !(equipment.getItem() instanceof ItemThinkingCap)) {
            return false;
        }
        System.out.println("has thinking cap");
        ItemThinkingCap cap = (ItemThinkingCap) equipment.getItem();
        ItemStack capUpgrade = cap.getStackInSlot(equipment, 1);
        if (capUpgrade == null || capUpgrade.getItem() == null) {
            return false;
        }
        System.out.println("has an upgrade");
        if (capUpgrade.getItem() == Items.water_bucket &&
          (block == Blocks.water || block == Blocks.flowing_water)) {
            System.out.println("rendering water invisible");
            renderInvisible();
            return true;
        } else if (capUpgrade.getItem() == Items.lava_bucket &&
          (block == Blocks.lava || block == Blocks.flowing_lava)) {
            System.out.println("rendering lava invisible");
            renderInvisible();
            return true;
        } else {
            System.out.println("rendering nothing");
            return false;
        }
    }

    private void renderInvisible() {
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.setColorOpaque_F(0.5F, 0.5F, 0.5F);
        tessellator.draw();
    }

    @Override
    public boolean shouldRender3DInInventory(int modelId) {
        return false;
    }

    @Override
    public int getRenderId() {
        return FluidRegistry.renderIdFluid;
    }
}
