package flaxbeard.steamcraft.client.render.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

public class ModelChargingPad extends ModelBase {
    public ModelRenderer pole1;
    public ModelRenderer pole2;
    public ModelRenderer top1;
    public ModelRenderer top2;
    public ModelRenderer top3;
    public ModelRenderer top4;

    public ModelRenderer poleConnector1;
    public ModelRenderer poleConnector2;
    public ModelRenderer poleConnector3;
    public ModelRenderer poleConnector4;
    public ModelRenderer poleConnector5;
    public ModelRenderer poleConnector6;
    public ModelRenderer poleConnector7;
    public ModelRenderer poleConnector8;
    public ModelRenderer poleConnector9;
    public ModelRenderer poleConnector10;

    public ModelChargingPad() {
        this.pole1 = (new ModelRenderer(this, 24, 0)).setTextureSize(64, 64);
        pole1.addBox(0.5F, -4.99F, 3.0F, 2, 18, 2);
        this.pole2 = (new ModelRenderer(this, 24, 0)).setTextureSize(64, 64);
        pole2.addBox(13.5F, -4.99F, 3.0F, 2, 18, 2);
        this.top1 = (new ModelRenderer(this, 24, 0)).setTextureSize(64, 64);
        top1.addBox(0.5F, 13.0F, 3.0F, 3, 2, 2);
        this.top2 = (new ModelRenderer(this, 24, 0)).setTextureSize(64, 64);
        top2.addBox(12.5F, 13.0F, 3.0F, 3, 2, 2);
        this.top3 = (new ModelRenderer(this, 24, 0)).setTextureSize(64, 64);
        top3.addBox(3.5F, 12.5F, 2.5F, 1, 3, 3);
        this.top4 = (new ModelRenderer(this, 24, 0)).setTextureSize(64, 64);
        top4.addBox(11.5F, 12.5F, 2.5F, 1, 3, 3);
        this.poleConnector1 = (new ModelRenderer(this, 24, 0)).setTextureSize(64, 64);
        poleConnector1.addBox(0.01F, -4.0F, 2.5F, 3, 1, 3);
        this.poleConnector2 = (new ModelRenderer(this, 24, 0)).setTextureSize(64, 64);
        poleConnector2.addBox(12.99F, -4.0F, 2.5F, 3, 1, 3);
        this.poleConnector3 = (new ModelRenderer(this, 24, 0)).setTextureSize(64, 64);
        poleConnector3.addBox(0.01F, 1.0F, 2.5F, 3, 1, 3);
        this.poleConnector4 = (new ModelRenderer(this, 24, 0)).setTextureSize(64, 64);
        poleConnector4.addBox(12.99F, 1.0F, 2.5F, 3, 1, 3);
        this.poleConnector5 = (new ModelRenderer(this, 24, 0)).setTextureSize(64, 64);
        poleConnector5.addBox(0.01F, 6.0F, 2.5F, 3, 1, 3);
        this.poleConnector6 = (new ModelRenderer(this, 24, 0)).setTextureSize(64, 64);
        poleConnector6.addBox(12.99F, 6.0F, 2.5F, 3, 1, 3);
        this.poleConnector7 = (new ModelRenderer(this, 24, 0)).setTextureSize(64, 64);
        poleConnector7.addBox(0.01F, 11.0F, 2.5F, 3, 1, 3);
        this.poleConnector8 = (new ModelRenderer(this, 24, 0)).setTextureSize(64, 64);
        poleConnector8.addBox(12.99F, 11.0F, 2.5F, 3, 1, 3);
    }


    public void render(int progress) {


        this.pole1.render(0.0625F);
        this.pole2.render(0.0625F);
        this.poleConnector1.render(0.0625F);
        this.poleConnector2.render(0.0625F);
        this.poleConnector3.render(0.0625F);
        this.poleConnector4.render(0.0625F);
        this.poleConnector5.render(0.0625F);
        this.poleConnector6.render(0.0625F);
        this.poleConnector7.render(0.0625F);
        this.poleConnector8.render(0.0625F);
        this.top1.render(0.0625F);
        this.top2.render(0.0625F);
        this.top3.render(0.0625F);
        this.top4.render(0.0625F);
    }
}
