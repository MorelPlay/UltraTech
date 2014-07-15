package common.cout970.UltraTech.client.gui;

import org.lwjgl.opengl.GL11;

import common.cout970.UltraTech.TileEntities.utility.ReactorControllerEntity;
import common.cout970.UltraTech.network.Net_Utils;
import common.cout970.UltraTech.packets.PacketController;
import common.cout970.UltraTech.util.UT_Utils;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;

public class ControllerGui extends GuiContainer {

	private ReactorControllerEntity entity;

	public ControllerGui(Container par1Container, InventoryPlayer inventory,ReactorControllerEntity tile) {
		super(par1Container);
		entity = tile;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

		this.mc.renderEngine.bindTexture(new ResourceLocation("ultratech:textures/gui/controller.png"));
		int xStart = (width - xSize) / 2;
		int yStart = (height - ySize) / 2;
		this.drawTexturedModalRect(xStart, yStart, 0, 0, xSize, ySize);
		this.drawTexturedModalRect(xStart+23, yStart+55, 176, (!entity.useHeat) ? 0 : 14, 14, 14);
		
		if(entity.Reactor != null){
			if(entity.useHeat){
				String heat = "Stop reactor at "+entity.maxheat+"C";
				String start ="Restart reactor at "+entity.minheat+"C";
				this.drawCenteredString(fontRendererObj, heat, xStart+82, yStart+18, UT_Utils.RGBtoInt(255, 255, 255));
				this.drawCenteredString(fontRendererObj, start, xStart+88, yStart+30, UT_Utils.RGBtoInt(255, 255, 255));
			}else{
				String heat = "Redstone signal is "+!entity.on;
				this.drawCenteredString(fontRendererObj, heat, xStart+86, yStart+18, UT_Utils.RGBtoInt(255, 255, 255));
			}
		}else{
			this.drawCenteredString(fontRendererObj, "Reactor not found", xStart+85, yStart+25, UT_Utils.RGBtoInt(255, 255, 255));
		}
		
		 //NAME
        this.drawCenteredString(fontRendererObj, "Reactor Controller", xStart+85, yStart+4, UT_Utils.RGBtoInt(255, 255, 255));
	}
	
	protected void mouseClicked(int par1, int par2, int par3)
	{
		super.mouseClicked(par1, par2, par3);
		int xStart = (width - xSize) / 2;
		int yStart = (height - ySize) / 2;
		if(isIn(par1, par2, xStart+22, yStart+52, 18, 18)){
			Net_Utils.PipeLine.sendToServer(new PacketController(entity,entity.useHeat));
		}
	}
    
    public boolean isIn(int mx, int my, int x, int y, int w, int h){
		if(mx > x && mx < x+w){
			if(my > y && my < y+h){
				return true;
			}
		}
		return false;
	}

}
