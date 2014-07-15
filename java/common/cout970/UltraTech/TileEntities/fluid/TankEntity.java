package common.cout970.UltraTech.TileEntities.fluid;

import common.cout970.UltraTech.network.Net_Utils;
import common.cout970.UltraTech.network.SyncTile;
import common.cout970.UltraTech.util.LogHelper;
import common.cout970.UltraTech.util.fluids.UT_Tank;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

public class TankEntity extends SyncTile implements IFluidHandler{

	private UT_Tank storage;
	private boolean FluidChange = true;

	public UT_Tank getTank(){
		if(storage == null)storage = new UT_Tank(32000, this);
		return storage;
	}
	
	public void updateEntity(){
		if(worldObj.getTotalWorldTime()%20 == 0 && FluidChange){
			FluidChange = false;
			sendNetworkUpdate();
		}
	}

	@Override
	public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
		int f = getTank().fill(resource, doFill);
		if(f > 0){
			FluidChange = true;
		}
		return f;
	}

	@Override
	public FluidStack drain(ForgeDirection from, FluidStack resource,
			boolean doDrain) {
		if(resource == null)return null;
		return drain(from, resource.amount, doDrain);
	}

	@Override
	public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
		FluidStack f = getTank().drain(maxDrain, doDrain);
		if(f != null && f.amount > 0 && doDrain){
			FluidChange = true;
		}
		return f;
	}

	@Override
	public boolean canFill(ForgeDirection from, Fluid fluid) {
		return getTank().fill(new FluidStack(fluid, 1), false) != 0;
	}

	@Override
	public boolean canDrain(ForgeDirection from, Fluid fluid) {
		return getTank().drain(1, false) != null;
	}

	@Override
	public FluidTankInfo[] getTankInfo(ForgeDirection from) {
		return new FluidTankInfo[]{new FluidTankInfo(getTank())};
	}

	@Override
	public void readFromNBT(NBTTagCompound nbtTagCompound) {
		super.readFromNBT(nbtTagCompound);
		getTank().readFromNBT(nbtTagCompound, "liquid");
	}

	@Override
	public void writeToNBT(NBTTagCompound nbtTagCompound) {
		super.writeToNBT(nbtTagCompound);
		getTank().writeToNBT(nbtTagCompound, "liquid");
	}

}
