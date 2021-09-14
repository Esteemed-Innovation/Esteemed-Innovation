package eiteam.esteemedinnovation.modules.transport.steam;

import eiteam.esteemedinnovation.api.network.ITransporter;
import eiteam.esteemedinnovation.api.network.Network;
import eiteam.esteemedinnovation.api.network.steam.ISteamTransporter;
import eiteam.esteemedinnovation.api.tags.FluidTags;
import eiteam.esteemedinnovation.base.EsteemedInnovation;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.capability.templates.FluidTank;

import java.util.*;

public class SteamNetwork extends Network {
    /**
     * A generic steamed DamageSource. The DamageSource counts as Fire damage. Use this for any damage that is caused by
     * steam directly.
     */
    public static final DamageSource STEAMED_DAMAGE = new DamageSource(EsteemedInnovation.MODID + ":steamed").setFireDamage();
    
    private final FluidTank fluidTank = new FluidTank(FluidAttributes.BUCKET_VOLUME, fluidStack -> FluidTags.fluidSteam.contains(fluidStack.getFluid()));
    
    public SteamNetwork(World world) {
        super(world);
    }
    
    public FluidTank getFluidTank() {
        return fluidTank;
    }
    
    @Override
    public ResourceLocation getType() {
        return null;
    }
    
    @Override
    public void update() {
        if (getPressure() > 1.2F) {
            Iterator<Map.Entry<BlockPos, ITransporter>> iter = transporters.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry<BlockPos, ITransporter> entry = iter.next();
                ISteamTransporter trans = (ISteamTransporter)entry.getValue();
                if (!trans.getWorld().isRemote && shouldExplode(oneInX(getPressure(), trans.getPressureResistance()))) {
                    trans.shouldExplode();
                }
            }
        }
    }
    
    @Override
    public void onTransporterAdded(ITransporter transporter) {
        ISteamTransporter st = (ISteamTransporter) transporter;
        fluidTank.setCapacity(fluidTank.getCapacity() + st.getCapacity());
    }
    
    @Override
    public void onTransporterRemoved(ITransporter transporter) {
        ISteamTransporter st = (ISteamTransporter) transporter;
        fluidTank.setCapacity(fluidTank.getCapacity() - st.getCapacity());
    }
    
    @Override
    public CompoundNBT writeToNBT(CompoundNBT nbt) {
        CompoundNBT tank = new CompoundNBT();
        fluidTank.writeToNBT(tank);
        
        nbt.putInt("capacity", getCapacity());
        nbt.put("tank", tank);
        
        return super.writeToNBT(nbt);
    }

    public void addSteam(int amount) {
        getFluidTank().getFluid().grow(amount);
    }

    public void decrSteam(int amount) {
        getFluidTank().getFluid().shrink(amount);
        if (getSteam() < 0) {
            getFluidTank().getFluid().setAmount(0);
        }
    }

    /**
     * @return The total amount of steam currently contained in the network.
     */
    public int getSteam() {
        return getFluidTank().getFluidAmount();
    }

    /**
     * @return The total capacity of steam that the steam network can hold.
     */
    public int getCapacity() {
        return getFluidTank().getCapacity();
    }

    private int oneInX(float pressure, float resistance) {
        return Math.max(1, (int) Math.floor((double) (500.0F - (pressure / (1.1F + resistance) * 100))));
    }

    private boolean shouldExplode(int oneInX) {
        return oneInX <= 1 || world.rand.nextInt(oneInX - 1) == 0;
    }

    /**
     * @return The total pressure of the network. Calculated as steam / capacity in the default implementation.
     *         In the default implementation 1.2F pressure is considered dangerously high and capable of exploding.
     */
    public float getPressure() {
        float capacity = getCapacity();
        return capacity > 0 ? getSteam() / capacity : 0;

    }
}
