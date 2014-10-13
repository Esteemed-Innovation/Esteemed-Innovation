package flaxbeard.steamcraft.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import flaxbeard.steamcraft.api.IEngineerable;
import flaxbeard.steamcraft.tile.TileEntityEngineeringTable;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.tuple.MutablePair;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class GuiEngineeringTable extends GuiContainer {
    public static final ResourceLocation furnaceGuiTextures = new ResourceLocation("steamcraft:textures/gui/engineering.png");
    private boolean wasFull = false;
    private TileEntityEngineeringTable furnaceInventory;
    private InventoryPlayer inv;

    public GuiEngineeringTable(InventoryPlayer par1InventoryPlayer, TileEntityEngineeringTable entity) {
        super(new ContainerEngineeringTable(par1InventoryPlayer, entity));
        this.furnaceInventory = entity;
        this.inv = par1InventoryPlayer;
        // this.wasFull = ((ContainerEngineeringTable)this.inventorySlots).hasEngineer;
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of the items)
     */
    protected void drawGuiContainerForegroundLayer(int par1, int par2) {


    }

    /**
     * Draw the background layer for the GuiContainer (everything behind the items)
     */
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(furnaceGuiTextures);
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        GL11.glEnable(3042);
        this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
        //this.inventorySlots = new ContainerEngineeringTable(inv, furnaceInventory);

        if (furnaceInventory.getStackInSlot(0) != null) {
            if (furnaceInventory.getStackInSlot(0).getItem() instanceof IEngineerable) {
                IEngineerable item = (IEngineerable) furnaceInventory.getStackInSlot(0).getItem();
                item.drawBackground(this, 0, k + 52, l + 8);

                int i = 0;
                for (MutablePair<Integer, Integer> pair : item.engineerCoordinates()) {
                    int x = pair.left;
                    int y = pair.right;
                    item.drawSlot(this, i, k + x + 52, l + y + 8);
                    i++;
                }
            }
        }
    }


}
