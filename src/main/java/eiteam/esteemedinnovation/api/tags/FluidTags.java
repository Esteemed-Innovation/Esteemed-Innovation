package eiteam.esteemedinnovation.api.tags;

import net.minecraft.fluid.Fluid;
import net.minecraft.tags.ITag.INamedTag;

import static eiteam.esteemedinnovation.api.util.TagUtility.*;
import static eiteam.esteemedinnovation.base.EsteemedInnovation.forgeLocation;

public class FluidTags {

    public static final INamedTag<Fluid> fluidSteam = createFluidWrapper(forgeLocation("steam"));
}
