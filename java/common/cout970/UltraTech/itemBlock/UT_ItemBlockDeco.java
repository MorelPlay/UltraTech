package common.cout970.UltraTech.itemBlock;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class UT_ItemBlockDeco extends UT_ItemBlock{

	public UT_ItemBlockDeco(Block par1) {
		super(par1);
	}

	public final static String[] subNames = {
		"White","Black","Blue","Steal Blue","Cyan","Sea Green","Green","Light Green","Yellow","Orange","Red","Purple","Pink","Blue Violet"
	};
	
	@Override
	public boolean getIsRepairable(ItemStack par1ItemStack,
			ItemStack par2ItemStack) {
		return false;
	}
	
	@Override
	public String getUnlocalizedName(ItemStack itemstack) {
		return getUnlocalizedName() + ".deco."+itemstack.getItemDamage();
	}

	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack is, EntityPlayer p, List l, boolean par4) {
		super.addInformation(is, p, l, par4);
		try{
			l.add("Use a 3D painter to change the color");
		}catch(Exception e){}
	}
	
}
