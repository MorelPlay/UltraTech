package api.cout970.UltraTech.FTpower;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import codechicken.lib.vec.Cuboid6;
import common.cout970.UltraTech.TileEntities.Tier2.PumpEntity;
import common.cout970.UltraTech.lib.UT_Utils;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * basic class for cables and thinks that not keep charge
 * @author Cout970
 *
 */
public class PowerInterface implements IPower{
	
	private Map<ForgeDirection, Boolean> connections = new HashMap<ForgeDirection, Boolean>();
    private TileEntity Parent; //machine
    private PowerNetwork net; //machines conected
    
    public PowerInterface(TileEntity p){
    	Parent = p;
    }

    public TileEntity getParent(){
    	return Parent;
    }
    
	public ForgeDirection[] getConnectableSides(){
		return ForgeDirection.VALID_DIRECTIONS;
	}

    public PowerNetwork getNetwork() {
    	return net;
    }
    
    public void setNetwork(PowerNetwork n) {
    	net = n;
    }
    
	public void MachineUpdate(){
		if(net == null){
			iterate();
		}
	}

    public void iterate(){
    	boolean hasNetwork = false;
    	for(ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS){
    		TileEntity e = PowerUtils.getRelative(Parent, dir);
    		if(e instanceof IPowerConductor){
    			PowerInterface p = ((IPowerConductor) e).getPower();
    			if(Arrays.asList(p.getConnectableSides()).contains(dir.getOpposite())){
    				if(p.getNetwork() != null){
    					if(!hasNetwork){
    						setNetwork(p.getNetwork());
    						hasNetwork = true;
    					}else{
    						net.mergeWith(p.getNetwork());
    					}
    				}
    			}
    		}
    	}
    	if(!hasNetwork){
    		setNetwork(PowerNetwork.create(this));
    	}
    	net.refresh();
    }

    public void onNetworkUpdate() {
    	net.onNetworkUpdate();
    }

    public void reconect(TileEntity tileEntity){
    	connections.clear();
    	for(ForgeDirection d:ForgeDirection.VALID_DIRECTIONS){
    		TileEntity t = UT_Utils.getRelative(tileEntity, d);
    		if(t instanceof IPowerConductor){
    			PowerInterface i =((IPowerConductor) t).getPower();
    			if(Arrays.asList(i.getConnectableSides()).contains(d.getOpposite())){
    				connections.put(d, true);
    			}else{
    				connections.put(d, false);
    			}
    		}else{
    			connections.put(d, false);
    		}
    	}
    }

	@Override
	public double addCharge(double amount) {
		return 0;
	}

	@Override
	public void removeCharge(double amount) {		
	}

	@Override
	public double getCharge() {
		return 0;
	}

	@Override
	public double maxCharge() {
		return 0;
	}

	@Override
	public double getFlow() {
		return 0;
	}

	public Map<ForgeDirection, Boolean> getConections() {
		return connections;
	}
}