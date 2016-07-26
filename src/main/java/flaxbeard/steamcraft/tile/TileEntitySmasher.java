package flaxbeard.steamcraft.tile;

import flaxbeard.steamcraft.Config;
import flaxbeard.steamcraft.Steamcraft;
import flaxbeard.steamcraft.api.ISteamTransporter;
import flaxbeard.steamcraft.api.IWrenchDisplay;
import flaxbeard.steamcraft.api.IWrenchable;
import flaxbeard.steamcraft.api.steamnet.SteamNetwork;
import flaxbeard.steamcraft.api.tile.SteamTransporterTileEntity;
import flaxbeard.steamcraft.misc.ItemStackUtility;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderGameOverlayEvent.Post;
import net.minecraftforge.oredict.OreDictionary;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import static flaxbeard.steamcraft.init.blocks.SteamMachineryBlocks.Blocks.*;

public class TileEntitySmasher extends SteamTransporterTileEntity implements ISteamTransporter, IWrenchable, IWrenchDisplay {
	public static final SmashablesRegistry REGISTRY = new SmashablesRegistry();

    public int spinup = 0;
    public float extendedLength = 0.0F;
    public Block smooshingBlock;
    public int smooshingMeta;
    public int extendedTicks = 0;
    public List<ItemStack> smooshedStack;
    private boolean hasBlockUpdate = false;
    private boolean isActive = false;
    private boolean isBreaking = false;
    private boolean shouldStop = false;
    private boolean isInitialized = false;
    private boolean running = false;
    private boolean smashNextRound = false;
    private boolean blockBreakerMode = false;
    private boolean hasBeenSet = false;

    public TileEntitySmasher() {
        super(EnumFacing.VALUES);
    }

    @Override
    public void readFromNBT(NBTTagCompound access) {
        super.readFromNBT(access);
        extendedLength = access.getFloat("extendedLength");
        extendedTicks = access.getInteger("extendedTicks");
        spinup = access.getInteger("spinup");
        blockBreakerMode = access.getBoolean("blockBreakerMode");
        hasBeenSet = access.getBoolean("hasBeenSet");
        smooshingBlock = Block.getBlockById(access.getInteger("ORE_BLOCK"));
        smooshingMeta = access.getInteger("smooshingMeta");
        NBTTagList nbttaglist = (NBTTagList) access.getTag("Items");
        smooshedStack = new ArrayList<>();

        for (int i = 0; i < nbttaglist.tagCount(); ++i) {
            NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
            smooshedStack.add(ItemStack.loadItemStackFromNBT(nbttagcompound1));
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound access) {
        super.writeToNBT(access);
        access.setBoolean("blockBreakerMode", blockBreakerMode);
        access.setBoolean("hasBeenSet", hasBeenSet);
        access.setInteger("spinup", spinup);
        access.setFloat("extendedLength", extendedLength);
        access.setInteger("extendedTicks", extendedTicks);
        access.setInteger("ORE_BLOCK", Block.getIdFromBlock(smooshingBlock));
        access.setInteger("smooshingMeta", smooshingMeta);
        NBTTagList nbttaglist = new NBTTagList();

        if (smooshedStack != null) {
            for (ItemStack aSmooshedStack : smooshedStack) {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                aSmooshedStack.writeToNBT(nbttagcompound1);
                nbttaglist.appendTag(nbttagcompound1);
            }
        }

        access.setTag("Items", nbttaglist);

        return access;
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        NBTTagCompound access = super.getUpdateTag();
        access.setInteger("spinup", spinup);
        access.setFloat("extendedLength", extendedLength);
        access.setInteger("extendedTicks", extendedTicks);
        access.setInteger("ORE_BLOCK", Block.getIdFromBlock(smooshingBlock));
        access.setInteger("smooshingMeta", smooshingMeta);
        access.setBoolean("running", running);
        access.setBoolean("blockBreakerMode", blockBreakerMode);
        access.setBoolean("hasBeenSet", hasBeenSet);
        return new SPacketUpdateTileEntity(pos, 1, access);
    }


//	private int encodeParticles(){
//		int smash=0 , smoke1=0, smoke2=0, smoke3=0;
//		if (!worldObj.isRemote){
//			if (extendedTicks==3) smash = 1;
//			if (extendedTicks >= 8 && extendedTicks <= 16 && (extendedTicks % 4) == 0) smoke1 = 2;
//			//int smoke1 = (extendedTicks == 3) ? (1 << 1) : 0;
//			//int smoke2 = (extendedTicks >= 8) && (extendedTicks <= 16) && ((extendedTicks % 4) == 0) ? (1  << 2): 0;
//			//int smoke3 =  (extendedTicks == 6) ? (1 << 3) : 0;
//
//		}
//
//
//		return smash + smoke1 + smoke2 + smoke3;
//	}

    private void decodeAndCreateParticles() {
        if (worldObj.isRemote) {
            if (extendedTicks > 15) {
                float xV = 0F;
                float zV = 0F;
                double xO = 0D;
                double zO = 0D;
                switch (getBlockMetadata()) {
                    case 2:
                        zV = 0.05F;
                        zO = 0.1D;
                        break;
                    case 3:
                        zV = -0.05F;
                        zO = -0.1D;
                        break;
                    case 4:
                        xV = 0.05F;
                        xO = 0.1D;
                        break;
                    case 5:
                        xV = -0.05F;
                        xO = -0.1D;
                        break;
                    default:
                        break;
                }
                double xParticle = pos.getX() + 0.5D + xO;
                double yParticle = pos.getY() + 1.1D;
                double zParticle = pos.getZ() + 0.5D + zO;
                worldObj.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, xParticle, yParticle, zParticle, xV, 0.05F, zV);
            }
        }
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        super.onDataPacket(net, pkt);
        NBTTagCompound access = pkt.getNbtCompound();
        extendedLength = access.getFloat("extendedLength");
        extendedTicks = access.getInteger("extendedTicks");
        spinup = access.getInteger("spinup");
        smooshingBlock = Block.getBlockById(access.getInteger("ORE_BLOCK"));
        smooshingMeta = access.getInteger("smooshingMeta");
        running = access.getBoolean("running");
        blockBreakerMode = access.getBoolean("blockBreakerMode");
        hasBeenSet = access.getBoolean("hasBeenSet");
        markForUpdate();
    }

    @Override
    public void update() {
        if (!isInitialized) {
            EnumFacing facing = EnumFacing.getFront(getBlockMetadata());
            addSideToGaugeBlacklist(facing);
            EnumFacing[] directions = new EnumFacing[5];
            int i = 0;
            for (EnumFacing direction : EnumFacing.VALUES) {
                if (direction != facing && direction != EnumFacing.UP) {
                    directions[i] = direction;
                    i++;
                }
            }
            setDistributionDirections(directions);
            isInitialized = true;
        }
        super.update();
        if (!worldObj.isRemote) {
            int[] target = getTarget(1);

            int x = target[0];
            int y = pos.getY();
            int z = target[1];
            BlockPos soundPos = new BlockPos(pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F);
            if (spinup == 1) {
                worldObj.playSound(null, soundPos, Steamcraft.SOUND_HISS, SoundCategory.BLOCKS,
                  Blocks.ANVIL.getSoundType().getVolume(), 0.9F);
            }
            if (extendedTicks > 15) {
                worldObj.playSound(null, soundPos, Steamcraft.SOUND_LEAK, SoundCategory.BLOCKS, 2F, 0.9F);
            }
            if (extendedTicks == 5) {
                float pitch = (0.75F * (float) Math.random() * 0.1F);
                worldObj.playSound(null, soundPos, SoundEvents.BLOCK_STONE_BREAK, SoundCategory.BLOCKS, 0.5F, pitch);
            }
            if (extendedTicks > 0 && extendedTicks < 6 && smooshingBlock != null) {
                SoundType smooshingSoundType = smooshingBlock.getSoundType();
                if (smooshingSoundType != null) {
                    SoundEvent breakSound = smooshingSoundType.getBreakSound();
                    if (breakSound != null) {
                        worldObj.playSound(null, soundPos, breakSound, SoundCategory.BLOCKS,
                          smooshingSoundType.getVolume(), smooshingSoundType.getPitch());
                    }
                }
            }
            //handle state changes
            if (shouldStop) {
                spinup = 0;
                extendedLength = 0;
                extendedTicks = 0;
                isActive = false;
                shouldStop = false;
                isBreaking = false;
                running = false;
                markForUpdate();
                return;
            }
            if (!smashNextRound && hasSomethingToSmash() && hasPartner() && getSteamShare() > 1000 && !isActive) {
                smashNextRound = true;
                return;
            }
            boolean smashThisRound = false;
            if (smashNextRound) {
                smashThisRound = true;
                smashNextRound = false;
            }
            if (smashThisRound) {
                if (hasSomethingToSmash() && !isActive) {
                    decrSteam(1000);
                    running = true;
                    markForUpdate();
                    isActive = true;
                    isBreaking = true;
                }
            }

            BlockPos middleBlockPos = new BlockPos(x, y, z);
            //handle processing
            if (isActive) {
                // if we haven't spun up yet, do it.
                if (isBreaking) {
                    if (!hasSomethingToSmash() && spinup < 40 && getBlockMetadata() % 2 == 0) {
                        shouldStop = true;
                        int[] tc = getTarget(2);
                        BlockPos position = new BlockPos(tc[0], y, tc[1]);
                        TileEntitySmasher partner = (TileEntitySmasher) worldObj.getTileEntity(position);
                        partner.shouldStop = true;
                        return;
                    }
                    if (spinup < 41) {
                        // spinup complete. SMAASH!
                        if (spinup == 40) {
                            IBlockState middleBlockState = worldObj.getBlockState(middleBlockPos);
                            Block middleBlock = middleBlockState.getBlock();
                            if (hasSomethingToSmash()) {
                                spinup++;
                                if (getBlockMetadata() % 2 == 0) {
                                    smooshingBlock = middleBlock;
                                    smooshingMeta = middleBlock.getMetaFromState(middleBlockState);
                                    smooshedStack = smooshingBlock.getDrops(worldObj, middleBlockPos, middleBlockState, 0);
                                    markForUpdate();
                                    worldObj.setBlockState(middleBlockPos, ROCK_SMASHER_DUMMY.getBlock().getDefaultState());
                                }
                            } else {
                                if (hasPartner()) {
                                    int[] pc = getTarget(2);
                                    BlockPos partnerPosition = new BlockPos(pc[0], y, pc[1]);
                                    TileEntitySmasher partner = (TileEntitySmasher) worldObj.getTileEntity(partnerPosition);
                                    if (partner.spinup < 41 || (partner.spinup >= 41 && partner.shouldStop)) {
                                        shouldStop = true;
                                    }

                                    if (shouldStop) {
                                        spinup++;
                                        return;
                                    }
                                }
                            }
                        }

                        spinup++;
//                     if we've spun up, extend
                    } else if (extendedLength < 0.5F && !shouldStop) {
                        extendedLength += 0.1F;
                        if (extendedTicks == 3 && getBlockMetadata() % 2 == 0 && !worldObj.isRemote) {
                            spawnItems(middleBlockPos);
                        }
                        extendedTicks++;

                        // we're done extending. Time to go inactive and start retracting
                    } else {
                        isBreaking = false;
                        spinup = 0;
                    }
                } else {
                    // Get back in line!
                    if (extendedLength > 0.0F) {
                        extendedLength -= 0.025F;
                        extendedTicks++;

                        if (extendedLength < 0F) {
                            extendedLength = 0F;
                        }
                    } else {
                        isActive = false;
                        running = false;
                        markForUpdate();
                        extendedTicks = 0;
                        if (worldObj.getBlockState(middleBlockPos).getBlock() == ROCK_SMASHER_DUMMY.getBlock()) {
                            worldObj.setBlockToAir(middleBlockPos);
                        }
                    }
                }
                // Sync.
            } else if (worldObj.getBlockState(middleBlockPos).getBlock() == ROCK_SMASHER_DUMMY.getBlock() &&
              getBlockMetadata() % 2 == 0) {
                worldObj.setBlockToAir(middleBlockPos);
            }
        } else {
            if (running) {
                decodeAndCreateParticles();
                if (spinup < 40) {
                    spinup++;
                } else if (spinup == 40) {
                    int[] tc = getTarget(1);
                    int x = tc[0];
                    int y = tc[1];
                    int z = tc[2];
                    BlockPos targetPosition = new BlockPos(x, y, z);
                    spinup++;
                    if (!worldObj.isAirBlock(targetPosition) && worldObj.getTileEntity(targetPosition) == null) {
                        IBlockState blockState = worldObj.getBlockState(targetPosition);
                        if (blockState.getBlockHardness(worldObj, targetPosition) < 50F && getBlockMetadata() % 2 == 0) {
                            smooshingBlock = blockState.getBlock();
                            smooshingMeta = blockState.getBlock().getMetaFromState(blockState);
                        }
                    }
                } else if (extendedTicks < 25) {
                    extendedTicks++;
                }
            } else {
                spinup = 0;
                extendedTicks = 0;
            }
        }
    }

    private void spawnItems(BlockPos position) {
        if (smooshedStack != null) {
            for (ItemStack stack : smooshedStack) {
            	ItemStack output = REGISTRY.getOutput(stack);
                double x = position.getX() + 0.5F;
                double y = position.getY() + 0.1F;
                double z = position.getZ() + 0.5F;
                
                if (output == null || blockBreakerMode) {
                    output = stack;
                    EntityItem entityItem = new EntityItem(worldObj, x, y, z, output);
                    worldObj.spawnEntityInWorld(entityItem);
                } else {
                    // Ore doubling
                    if (worldObj.rand.nextInt(Config.chance) == 0) {
                        output.stackSize *= 2;
                    }
                    EntityItem entityItem = new EntityItem(worldObj, x, y, z, output);
                    worldObj.spawnEntityInWorld(entityItem);
                    smooshedStack = null;
                }
            }
        }
    }

    private boolean hasSomethingToSmash() {
        int[] target = getTarget(1);
        int x = target[0];
        int y = pos.getY();
        int z = target[1];
        BlockPos position = new BlockPos(x, y, z);
        IBlockState blockState = worldObj.getBlockState(position);
        Block block = blockState.getBlock();
        return !worldObj.isAirBlock(position) && block == Blocks.BEDROCK &&
          worldObj.getTileEntity(position) == null && blockState.getBlockHardness(worldObj, position) < 50F;
    }

    public boolean hasPartner() {
        int[] target = getTarget(2);
        int x = target[0];
        int y = pos.getY();
        int z = target[1];
        int opposite = target[2];

        BlockPos partnerPos = new BlockPos(x, y, z);
        IBlockState partnerState = worldObj.getBlockState(partnerPos);
        Block partner = partnerState.getBlock();
        TileEntity partnerTE = worldObj.getTileEntity(partnerPos);
        if (partnerTE == null || !(partnerTE instanceof TileEntitySmasher)) {
            return false;
        }
        if (partner == ROCK_SMASHER.getBlock() && ((TileEntitySmasher) partnerTE).getSteamShare() > 100 &&
          partner.getMetaFromState(partnerState) == opposite) {
            TileEntitySmasher partnerSmasher = (TileEntitySmasher) partnerTE;
            if (partnerSmasher.blockBreakerMode != blockBreakerMode) {
                if (hasBeenSet && !partnerSmasher.hasBeenSet) {
                    partnerSmasher.blockBreakerMode = blockBreakerMode;
                } else if (!hasBeenSet && partnerSmasher.hasBeenSet) {
                    blockBreakerMode = partnerSmasher.blockBreakerMode;
                } else {
                    blockBreakerMode = partnerSmasher.blockBreakerMode;
                }
                hasBeenSet = true;
                partnerSmasher.hasBeenSet = true;
            }
            return true;
        }

        return false;
    }

    // returns x, z, opposite
    private int[] getTarget(int distance) {
        int x = pos.getX();
        int z = pos.getZ();
        int meta = getBlockMetadata();
        int opposite = meta % 2 == 0 ? meta + 1 : meta - 1;
        switch (meta) {
            case 2: {
                z -= distance;
                opposite = 3;
                break;
            }
            case 3: {
                z += distance;
                opposite = 2;
                break;
            }
            case 4: {
                x -= distance;
                opposite = 5;
                break;
            }
            case 5: {
                x += distance;
                opposite = 4;
                break;
            }
            default: {
                break;
            }
        }

        return new int[]{x, z, opposite};
    }

    public void blockUpdate() {
        hasBlockUpdate = true;
    }

    public boolean hasUpdate() {
        return hasBlockUpdate;
    }

    @Override
    public boolean onWrench(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, IBlockState state, float hitX, float hitY, float hitZ) {
        if (player.isSneaking()) {
            hasBeenSet = true;
            blockBreakerMode = !blockBreakerMode;
            int[] target = getTarget(2);
            int x2 = target[0];
            int y2 = this.pos.getY();
            int z2 = target[1];
            int opposite = target[2];
            BlockPos pos2 = new BlockPos(x2, y2, z2);
            IBlockState state2 = world.getBlockState(pos2);
            Block block = state2.getBlock();
            if (block == ROCK_SMASHER.getBlock() && block.getMetaFromState(state2) == opposite) {
                TileEntitySmasher smasher = (TileEntitySmasher) world.getTileEntity(pos2);
                if (smasher != null) {
                    smasher.blockBreakerMode = blockBreakerMode;
                    smasher.hasBeenSet = true;
                    smasher.markForUpdate();
                }
            }
            markForUpdate();
        } else {
            int steam = getSteamShare();
            getNetwork().split(this, true);
            EnumFacing[] directions = new EnumFacing[5];
            int i = 0;
            for (EnumFacing direction : EnumFacing.values()) {
                if (direction != EnumFacing.getFront(getBlockMetadata()) && direction != EnumFacing.UP) {
                    directions[i] = direction;
                    i++;
                }
            }
            setDistributionDirections(directions);
            SteamNetwork.newOrJoin(this);
            getNetwork().addSteam(steam);
        }
        return true;
    }


    @Override
    public void displayWrench(Post event) {
        GL11.glPushMatrix();
        int color = Minecraft.getMinecraft().thePlayer.isSneaking() ? 0xC6C6C6 : 0x777777;
        int x = event.getResolution().getScaledWidth() / 2 - 8;
        int y = event.getResolution().getScaledHeight() / 2 - 8;
        String loc = I18n.format("steamcraft.smasher." + blockBreakerMode);
        Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(loc, x + 15, y + 13, color);
        GL11.glPopMatrix();
    }

    @SuppressWarnings("unused")
    public static class SmashablesRegistry {
        public final Map<String, ItemStack> oreDicts = new HashMap<>();
        public final Map<ItemStack, ItemStack> registry = new HashMap<>();

        public ItemStack getOutput(ItemStack input) {
            if (input == null) {
                return null;
            }
            ItemStack output = null;
            int[] ids = OreDictionary.getOreIDs(input);

            if (ids != null && ids.length > 0) {
                for (int id : ids) {
                    output = oreDicts.get(OreDictionary.getOreName(id));
                    if (output != null) {
                        break;
                    }
                }
            }

            if (output == null) {
                for (Entry<ItemStack, ItemStack> entry : registry.entrySet()) {
                    if (ItemStack.areItemStacksEqual(entry.getKey(), input)) {
                        output = entry.getValue();
                        if (output != null) {
                            break;
                        }
                    }
                }
            }

            return ItemStack.copyItemStack(output);
        }

        public List<ItemStack> getInputs(ItemStack output) {
            if(output == null) {
                return null;
            }

            List<ItemStack> inputs = new ArrayList<>();

            for (Entry<ItemStack, ItemStack> entry : registry.entrySet()) {
                if(ItemStackUtility.areItemStacksMostlyEqual(entry.getValue(), output)){
                    inputs.add(entry.getKey());
                }
            }

            for (Entry<String, ItemStack> entry : oreDicts.entrySet()) {
                if(ItemStackUtility.areItemStacksMostlyEqual(entry.getValue(), output)){
                    inputs.addAll(OreDictionary.getOres(entry.getKey()));
                }
            }

            if(inputs.isEmpty()){
                return null;
            }

            return inputs;
        }

        public void registerSmashable(String input, ItemStack output) {
            oreDicts.put(input, output);
        }

        public void registerSmashable(Block input, ItemStack output) {
            registerSmashable(new ItemStack(input), output);
        }

        public void registerSmashable(Item input, ItemStack output) {
            registerSmashable(new ItemStack(input), output);
        }

        public void registerSmashable(ItemStack input, ItemStack output) {
            registry.put(input, output);
        }

        public void removeSmashable(String input, ItemStack output) {
            oreDicts.remove(input);
        }

        public void removeSmashable(ItemStack input, ItemStack output) {
            registry.remove(input);
        }
    }
}
