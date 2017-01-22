package eiteam.esteemedinnovation.storage.item.canister;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

class ModelCanister extends ModelBase {
    private final ModelRenderer part1;
    private final ModelRenderer part2;
    private final ModelRenderer part3;

    ModelCanister() {
        part1 = new ModelRenderer(this, 0, 0);
        part1.setTextureSize(64, 64);
        part1.addBox(-1.5F, -2.0F, -1.5F, 3, 1, 3);

        part2 = new ModelRenderer(this, 0, 0);
        part2.setTextureSize(64, 64);
        part2.addBox(-2.0F, -1.0F, -2.0F, 4, 8, 4);

        part3 = new ModelRenderer(this, 0, 0);
        part3.setTextureSize(64, 64);
        part3.addBox(-1.5F, 7.0F, -1.5F, 3, 1, 3);
    }

    void renderAll() {
        part1.render(0.0625F);
        part2.render(0.0625F);
        part3.render(0.0625F);
    }
}
