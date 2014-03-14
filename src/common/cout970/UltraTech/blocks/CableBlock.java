package common.cout970.UltraTech.blocks;

import java.util.List;

import common.cout970.UltraTech.TileEntities.Tier1.CableEntity;
import common.cout970.UltraTech.core.UltraTech;
import common.cout970.UltraTech.energy.api.ElectricConductor;
import common.cout970.UltraTech.energy.api.EnergyUtils;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class CableBlock extends BlockContainer{

	private Icon blockIcon1;
	private Icon blockIcon2;

	public CableBlock(int par1, Material par2Material) {
		super(par1, par2Material);
		setCreativeTab(UltraTech.techTab);
		setStepSound(soundMetalFootstep);
		setResistance(5);
		setUnlocalizedName("Cable");
	}

	public void registerIcons(IconRegister iconRegister){
		this.blockIcon = iconRegister.registerIcon("ultratech:cable0");
		this.blockIcon1 = iconRegister.registerIcon("ultratech:cable1");
		this.blockIcon2 = iconRegister.registerIcon("ultratech:cable2");
	}
	
	@SuppressWarnings("unchecked")
	public void getSubBlocks(int unknown, CreativeTabs tab, @SuppressWarnings("rawtypes") List subItems){
		for (int ix = 0; ix < 1; ix++) {
			subItems.add(new ItemStack(this, 1, ix));
		}
	}

	@SideOnly(Side.CLIENT)
	@Override
	public Icon getIcon(int par1, int meta)
	{		
		if(meta == 0)return this.blockIcon;
		if(meta == 1)return this.blockIcon1;
		if(meta == 2)return this.blockIcon2;
		return this.blockIcon;
	}
	
	public void onNeighborBlockChange(World w, int x, int y, int z, int side){
		ElectricConductor m = (ElectricConductor) w.getBlockTileEntity(x, y, z);
		if(m.getNetwork() != null)m.getNetwork().refresh();

	}
	
	@Override
	public TileEntity createNewTileEntity(World world) {
		return new CableEntity();
	}
	
	@Override
	public int getDamageValue(World par1World, int par2, int par3, int par4) {
		return par1World.getBlockMetadata(par2, par3, par4);
	}

	@Override
	public int damageDropped (int metadata) {
		return metadata;
	}
	
	@Override
	public void onBlockAdded(World w, int x, int y, int z) {
		super.onBlockAdded(w, x, y, z);
		EnergyUtils.onBlockAdded(w, x, y, z);
	}
	public void onBlockPreDestroy(World w, int x, int y, int z, int meta) {
		super.onBlockPreDestroy(w, x, y, z, meta);
		EnergyUtils.onBlockPreDestroy(w, x, y, z, meta);
	}
	
	public boolean isOpaqueCube()
	{
		return false;
	}
	
	public boolean renderAsNormalBlock()
	{
		return false;
	}
	
	@Override
	public int getRenderType() {
		return -1;
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getSelectedBoundingBoxFromPool(World w, int x, int y, int z) {
    	AxisAlignedBB bb = AxisAlignedBB.getBoundingBox(x + (0.0625 * 6), y + (0.0625 * 6), z + (0.0625 * 6), (x + 1) - (0.0625 * 6), (y + 1) - (0.0625 * 6), (z + 1) - (0.0625 * 6));
    	
    	boolean renderUp = EnergyUtils.canConnectTo(w.getBlockTileEntity(x, y, z), w.getBlockTileEntity(x, y + 1, z));
    	boolean renderDown = EnergyUtils.canConnectTo(w.getBlockTileEntity(x, y, z), w.getBlockTileEntity(x, y - 1, z));
    	boolean renderSouth = EnergyUtils.canConnectTo(w.getBlockTileEntity(x, y, z), w.getBlockTileEntity(x, y, z + 1));
    	boolean renderNorth = EnergyUtils.canConnectTo(w.getBlockTileEntity(x, y, z), w.getBlockTileEntity(x, y, z - 1));
    	boolean renderEast = EnergyUtils.canConnectTo(w.getBlockTileEntity(x, y, z), w.getBlockTileEntity(x + 1, y, z));
    	boolean renderWest = EnergyUtils.canConnectTo(w.getBlockTileEntity(x, y, z), w.getBlockTileEntity(x - 1, y, z));
    	
    	if(renderUp)   bb.maxY = y + 1;
    	if(renderDown) bb.minY = y;
    	if(renderSouth)bb.maxZ = z + 1;
    	if(renderNorth)bb.minZ = z;
    	if(renderEast) bb.maxX = x + 1;
    	if(renderWest) bb.minX = x;
    	
    	return bb;
    }
    
    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World w, int x, int y, int z) {
    	AxisAlignedBB bb = AxisAlignedBB.getBoundingBox(x + (0.0625 * 6), y + (0.0625 * 6), z + (0.0625 * 6), (x + 1) - (0.0625 * 6), (y + 1) - (0.0625 * 6), (z + 1) - (0.0625 * 6));
    	
    	boolean renderUp = EnergyUtils.canConnectTo(w.getBlockTileEntity(x, y, z), w.getBlockTileEntity(x, y + 1, z));
    	boolean renderDown = EnergyUtils.canConnectTo(w.getBlockTileEntity(x, y, z), w.getBlockTileEntity(x, y - 1, z));
    	boolean renderSouth = EnergyUtils.canConnectTo(w.getBlockTileEntity(x, y, z), w.getBlockTileEntity(x, y, z + 1));
    	boolean renderNorth = EnergyUtils.canConnectTo(w.getBlockTileEntity(x, y, z), w.getBlockTileEntity(x, y, z - 1));
    	boolean renderEast = EnergyUtils.canConnectTo(w.getBlockTileEntity(x, y, z), w.getBlockTileEntity(x + 1, y, z));
    	boolean renderWest = EnergyUtils.canConnectTo(w.getBlockTileEntity(x, y, z), w.getBlockTileEntity(x - 1, y, z));
    	
    	if(renderUp)   bb.maxY = y + 1;
    	if(renderDown) bb.minY = y;
    	if(renderSouth)bb.maxZ = z + 1;
    	if(renderNorth)bb.minZ = z;
    	if(renderEast) bb.maxX = x + 1;
    	if(renderWest) bb.minX = x;
    	
    	return bb;
    }
    
    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess w, int x, int y, int z) {
    	AxisAlignedBB bb = AxisAlignedBB.getBoundingBox((0.0625 * 6),(0.0625 * 6),(0.0625 * 6), (1) - (0.0625 * 6), (1) - (0.0625 * 6), (1) - (0.0625 * 6));
    	
    	boolean renderUp = EnergyUtils.canConnectTo(w.getBlockTileEntity(x, y, z), w.getBlockTileEntity(x, y + 1, z));
    	boolean renderDown = EnergyUtils.canConnectTo(w.getBlockTileEntity(x, y, z), w.getBlockTileEntity(x, y - 1, z));
    	boolean renderSouth = EnergyUtils.canConnectTo(w.getBlockTileEntity(x, y, z), w.getBlockTileEntity(x, y, z + 1));
    	boolean renderNorth = EnergyUtils.canConnectTo(w.getBlockTileEntity(x, y, z), w.getBlockTileEntity(x, y, z - 1));
    	boolean renderEast = EnergyUtils.canConnectTo(w.getBlockTileEntity(x, y, z), w.getBlockTileEntity(x + 1, y, z));
    	boolean renderWest = EnergyUtils.canConnectTo(w.getBlockTileEntity(x, y, z), w.getBlockTileEntity(x - 1, y, z));
    	
    	if(renderUp)   bb.maxY = 1;
    	if(renderDown) bb.minY = 0;
    	if(renderSouth)bb.maxZ = 1;
    	if(renderNorth)bb.minZ = 0;
    	if(renderEast) bb.maxX = 1;
    	if(renderWest) bb.minX = 0;
    	
    	setBlockBounds((float) bb.minX, (float) bb.minY, (float) bb.minZ, (float) bb.maxX, (float) bb.maxY, (float) bb.maxZ);
    }
    

}