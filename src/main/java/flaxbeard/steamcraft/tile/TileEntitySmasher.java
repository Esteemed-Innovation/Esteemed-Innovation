package flaxbeard.steamcraft.tile;

import flaxbeard.steamcraft.Config;
import flaxbeard.steamcraft.Steamcraft;
import flaxbeard.steamcraft.api.ISteamTransporter;
import flaxbeard.steamcraft.api.IWrenchDisplay;
import flaxbeard.steamcraft.api.IWrenchable;
import flaxbeard.steamcraft.api.SmasherRegistry;
import flaxbeard.steamcraft.api.steamnet.SteamNetwork;
import flaxbeard.steamcraft.api.tile.SteamTransporterTileEntity;
import flaxbeard.steamcraft.block.BlockSmasher;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
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

import java.util.ArrayList;
import java.util.List;

import static flaxbeard.steamcraft.init.blocks.SteamMachineryBlocks.Blocks.ROCK_SMASHER;
import static flaxbeard.steamcraft.init.blocks.SteamMachineryBlocks.Blocks.ROCK_SMASHER_DUMMY;

public class TileEntitySmasher extends SteamTransporterTileEntity implements ISteamTransporter, IWrenchable, IWrenchDisplay {
	public int spinup = 0;
    private float extendedLength = 0.0F;
    private Block smooshingBlock;
    private int smooshingMeta;
    public int extendedTicks = 0;
    private List<ItemStack> smooshedStack;
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
        smooshingBlock = Block.getBlockById(access.getInteger("block"));
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
        access.setInteger("block", Block.getIdFromBlock(smooshingBlock));
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
        access.setInteger("block", Block.getIdFromBlock(smooshingBlock));
        access.setInteger("smooshingMeta", smooshingMeta);
        access.setBoolean("running", running);
        access.setBoolean("blockBreakerMode", blockBreakerMode);
        access.setBoolean("hasBeenSet", hasBeenSet);
        return new SPacketUpdateTileEntity(pos, 1, access);
    }

    private void decodeAndCreateParticles() {
        if (worldObj.isRemote) {
            if (extendedTicks > 15) {
                float xVelocity = 0F;
                float zVelocity = 0F;
                double xOffset = 0D;
                double zOffset = 0D;
                EnumFacing facing = worldObj.getBlockState(pos).getValue(BlockSmasher.FACING);
                if (facing.getAxis() == EnumFacing.Axis.X) {
                    xVelocity = 0.05F * -facing.getFrontOffsetX();
                    xOffset = 0.1D * -facing.getFrontOffsetX();
                } else {
                    zVelocity = 0.05F * -facing.getFrontOffsetZ();
                    zOffset = 0.1D * -facing.getFrontOffsetZ();
                }
                double xParticle = pos.getX() + 0.5D + xOffset;
                double yParticle = pos.getY() + 1.1D;
                double zParticle = pos.getZ() + 0.5D + zOffset;
                worldObj.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, xParticle, yParticle, zParticle, xVelocity, 0.05F, zVelocity);
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
        smooshingBlock = Block.getBlockById(access.getInteger("block"));
        smooshingMeta = access.getInteger("smooshingMeta");
        running = access.getBoolean("running");
        blockBreakerMode = access.getBoolean("blockBreakerMode");
        hasBeenSet = access.getBoolean("hasBeenSet");
        markForResync();
    }

    @Override
    public void update() {
        if (!isInitialized) {
            EnumFacing facing = worldObj.getBlockState(pos).getValue(BlockSmasher.FACING);
            addSideToGaugeBlacklist(facing);
            EnumFacing[] directions = new EnumFacing[4];
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
                  SoundType.ANVIL.getVolume(), 0.9F);
            }
            if (extendedTicks > 15) {
                worldObj.playSound(null, soundPos, Steamcraft.SOUND_LEAK, SoundCategory.BLOCKS, 2F, 0.9F);
            }
            if (extendedTicks == 5) {
                float pitch = (0.75F * (float) Math.random() * 0.1F);
                worldObj.playSound(null, soundPos, SoundEvents.BLOCK_STONE_BREAK, SoundCategory.BLOCKS, 0.5F, pitch);
            }
            if (extendedTicks > 0 && extendedTicks < 6 && smooshingBlock != null) {
                SoundType smooshingSoundType = smooshingBlock.getSoundType(worldObj.getBlockState(soundPos), worldObj, soundPos, null);
                SoundEvent breakSound = smooshingSoundType.getBreakSound();
                worldObj.playSound(null, soundPos, breakSound, SoundCategory.BLOCKS,
                  smooshingSoundType.getVolume(), smooshingSoundType.getPitch());
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
                markForResync();
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
                    markForResync();
                    isActive = true;
                    isBreaking = true;
                }
            }

            BlockPos middleBlockPos = new BlockPos(x, y, z);
            //handle processing
            if (isActive) {
                // if we haven't spun up yet, do it.
                if (isBreaking) {
                    if (!hasSomethingToSmash() && spinup < 40 && isPrimary()) {
                        shouldStop = true;
                        int[] tc = getTarget(2);
                        BlockPos position = new BlockPos(tc[0], y, tc[1]);
                        TileEntitySmasher partner = (TileEntitySmasher) worldObj.getTileEntity(position);
                        if (partner != null) {
                            partner.shouldStop = true;
                        }
                        return;
                    }
                    if (spinup < 41) {
                        // spinup complete. SMAASH!
                        if (spinup == 40) {
                            IBlockState middleBlockState = worldObj.getBlockState(middleBlockPos);
                            Block middleBlock = middleBlockState.getBlock();
                            if (hasSomethingToSmash()) {
                                spinup++;
                                if (isPrimary()) {
                                    smooshingBlock = middleBlock;
                                    smooshingMeta = middleBlock.getMetaFromState(middleBlockState);
                                    smooshedStack = smooshingBlock.getDrops(worldObj, middleBlockPos, middleBlockState, 0);
                                    markForResync();
                                    worldObj.setBlockState(middleBlockPos, ROCK_SMASHER_DUMMY.getBlock().getDefaultState());
                                }
                            } else {
                                if (hasPartner()) {
                                    int[] pc = getTarget(2);
                                    BlockPos partnerPosition = new BlockPos(pc[0], y, pc[1]);
                                    TileEntitySmasher partner = (TileEntitySmasher) worldObj.getTileEntity(partnerPosition);
                                    //noinspection ConstantConditions
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
                        if (extendedTicks == 3 && isPrimary() && !worldObj.isRemote) {
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
                        markForResync();
                        extendedTicks = 0;
                        if (worldObj.getBlockState(middleBlockPos).getBlock() == ROCK_SMASHER_DUMMY.getBlock()) {
                            worldObj.setBlockToAir(middleBlockPos);
                        }
                    }
                }
                // Sync.
            } else if (worldObj.getBlockState(middleBlockPos).getBlock() == ROCK_SMASHER_DUMMY.getBlock() && isPrimary()) {
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
                        if (blockState.getBlockHardness(worldObj, targetPosition) < 50F && isPrimary()) {
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

    private boolean isPrimary() {
        return worldObj.getBlockState(pos).getValue(BlockSmasher.FACING).getAxisDirection() == EnumFacing.AxisDirection.NEGATIVE;
    }

    private void spawnItems(BlockPos position) {
        if (smooshedStack != null) {
            for (ItemStack stack : smooshedStack) {
            	ItemStack output = SmasherRegistry.getOutput(stack);
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
        return !worldObj.isAirBlock(position) && block != Blocks.BEDROCK &&
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

    /**
     * @param distance The distance from the current smasher to check in. For example, getTarget(2) will check
     *                 X_Y where X is the current smasher, and Y is the space to check.
     * @return An array of the X and Z coordinates (no Y because the smasher cannot face up/down) and the opposite
     *         facing horizontal index.
     */
    private int[] getTarget(int distance) {
        EnumFacing facing = worldObj.getBlockState(pos).getValue(BlockSmasher.FACING);
        EnumFacing opposite = facing.getOpposite();
        BlockPos target = pos.offset(facing, distance);
        return new int[] { target.getX(), target.getZ(), opposite.getHorizontalIndex() };
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
                    smasher.markForResync();
                }
            }
            markForResync();
        } else {
            int steam = getSteamShare();
            getNetwork().split(this, true);
            EnumFacing[] directions = new EnumFacing[5];
            int i = 0;
            EnumFacing myFacing = worldObj.getBlockState(pos).getValue(BlockSmasher.FACING);
            for (EnumFacing direction : EnumFacing.VALUES) {
                if (direction != myFacing && direction != EnumFacing.UP) {
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
        GlStateManager.pushMatrix();
        int color = Minecraft.getMinecraft().thePlayer.isSneaking() ? 0xC6C6C6 : 0x777777;
        int x = event.getResolution().getScaledWidth() / 2 - 8;
        int y = event.getResolution().getScaledHeight() / 2 - 8;
        String loc = I18n.format("steamcraft.smasher." + blockBreakerMode);
        Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(loc, x + 15, y + 13, color);
        GlStateManager.popMatrix();
    }
}
