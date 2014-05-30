package common.cout970.UltraTech.machines.renders.items;

import org.lwjgl.opengl.GL11;

import common.cout970.UltraTech.models.ModelBoiler;
import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;

public class RenderBoilerItem implements IItemRenderer {
	
	private ModelBoiler model;
	private ResourceLocation texture = new ResourceLocation("ultratech:textures/misc/boiler.png");

	public RenderBoilerItem() {
		this.model = new ModelBoiler();
	}
	
	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type) {
		return true;
	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item,
			ItemRendererHelper helper) {
		return true;
	}
	
	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
		switch (type) {
            case ENTITY: {
                render(0.0F, -0.5F, 0.0F, 1.0F,false);
                return;
            }
            case EQUIPPED: {
                render(0.5F, 0F, 0.5F, 1.0F,false);
                return;
            }
            case INVENTORY: {
                render(0.0F, -0.5F, 0.0F, 1.0F,true);
                return;
            }
            case EQUIPPED_FIRST_PERSON: {
                render(0.5F, 0.0F, 0.5F, 1.0F,false);
                return;
            }
            default:
                return;
        }
	}
	
	
	public void render(float x, float y, float z, float scale, boolean rotate90Deg){
		GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glScalef(scale, scale, scale);
        GL11.glTranslatef(x, y, z);
        if(rotate90Deg){
            GL11.glRotatef(90F, 0, 1, 0);
        }else{
            GL11.glRotatef(0F, 0, 0, 0);
        }
        GL11.glRotatef(180F, 0, 0, 1);
        GL11.glTranslatef(0, -1.5f, 0);
        FMLClientHandler.instance().getClient().renderEngine.bindTexture(texture);
        model.render(null, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glPopMatrix();
	}
}