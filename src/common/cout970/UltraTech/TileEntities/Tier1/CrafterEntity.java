package common.cout970.UltraTech.TileEntities.Tier1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import common.cout970.UltraTech.lib.UT_Utils;
import common.cout970.UltraTech.machines.containers.CrafterContainer;
import common.cout970.UltraTech.misc.CrafterRecipe;
import common.cout970.UltraTech.misc.InventoryCrafter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.oredict.OreDictionary;

public class CrafterEntity extends TileEntity implements IInventory{

	public ItemStack[] inventory;//0 == result
	public InventoryCrafter craft;
	public CrafterRecipe saves;
	public Map<Integer,Boolean> found = new HashMap<Integer,Boolean>();


	public CrafterEntity(){
		inventory = new ItemStack[10];
		craft = new InventoryCrafter(this, 3, 3);
		saves = new CrafterRecipe();
	}

	public void update() {
		if(!worldObj.isRemote){}
			inventory[0] = CraftingManager.getInstance().findMatchingRecipe(craft, worldObj);
			canCraft();
	}

	public void craftItem() {
		UT_Utils.sendPacket(this, 0, 0, 1);
	}
	
	public void canCraft(){
		 found = new HashMap<Integer,Boolean>();
		 Map<Integer,Integer> already = new HashMap<Integer,Integer>();
		 for(int c = 0; c < 9;c++){
			 if(craft.getStackInSlot(c)== null){
				 found.put(c, false);
			 }else{
				 for(int i = 0; i < this.getSizeInventory();i++){
					 if(equal(this.getStackInSlot(i),craft.getStackInSlot(c),c)){
						 boolean cant;
						 if(already.containsKey(i)){
							 cant = false;
							 int aux = already.get(i);
							 if(aux < getStackInSlot(i).stackSize){
								 already.remove(i);
								 already.put(i, aux+1);
								 cant = true;
							 }
						 }else{
							 already.put(i, 1);
							 cant = true;
						 }
						 if(cant){
							 found.put(c, false);
							 break;
						 }
					 }
				 }
				 if(!found.containsKey(c)){
					 List<TileEntity> t = UT_Utils.getTiles(worldObj, xCoord, yCoord, zCoord);
					 List<IInventory> in = new ArrayList<IInventory>();
					 for(TileEntity tile : t)if(tile instanceof IInventory)in.add((IInventory) tile);
					 int aux2 = 9;
					 for(IInventory inv : in){
						 if(inv instanceof CrafterEntity){
							 for(int i = 1; i < inv.getSizeInventory();i++){
								 if(equal(inv.getStackInSlot(i),craft.getStackInSlot(c),c)){
									 boolean cant = true;
									 if(already.containsKey(i+aux2)){
										 cant = false;
										 int aux = already.get(i+aux2);
										 if(aux < inv.getStackInSlot(i).stackSize){
											 already.remove(i+aux2);
											 already.put(i, aux+1);
											 cant = true;
										 }
									 }else{
										 already.put(i+aux2, 1);
										 cant = true;
									 }
									 if(cant){
										 found.put(c, false);
										 break;
									 }
								 }
							 }
						 }else for(int i = 0; i < inv.getSizeInventory();i++){
							 if(equal(inv.getStackInSlot(i),craft.getStackInSlot(c),c)){
								 boolean cant = true;
								 if(already.containsKey(i+aux2)){
									 cant = false;
									 int aux = already.get(i+aux2);
									 if(aux < inv.getStackInSlot(i).stackSize){
										 already.remove(i+aux2);
										 already.put(i, aux+1);
										 cant = true;
									 }
								 }else{
									 already.put(i+aux2, 1);
									 cant = true;
								 }
								 if(cant){
									 found.put(c, false);
									 break;
								 }
							 }
						 }
						 aux2 += inv.getSizeInventory();
						 if(found.containsKey(c))break;
					 }
				 }
				 if(!found.containsKey(c)){
					 found.put(c, true);
				 }
			 }
		 }
	 }
	
	public boolean allFound() {
		for(int x=0;x<9;x++){
			if(found.containsKey(x)){
				if(found.get(x))return false;
			}
		}
		return true;
	}
	
	private boolean equal(ItemStack a, ItemStack b,int slot) {
		if(a == null && b == null)return true;
		if(a != null && b == null)return false;
		if(a == null && b != null)return false;
		if(OreDictionary.itemMatches(a, b, true))return true;
		if(OreDictionary.getOreID(a)!= -1 && OreDictionary.getOreID(b)!= -1){
			if(OreDictionary.getOreID(a) == OreDictionary.getOreID(b))return true;
		}
		//remplace and check
		InventoryCrafter c = new InventoryCrafter(this, 3, 3);
		for(int r = 0;r<9;r++){
			c.setInventorySlotContents(r, craft.getStackInSlot(r));
		}
		c.setInventorySlotContents(slot, a);
		ItemStack g = CraftingManager.getInstance().findMatchingRecipe(c, worldObj);
		ItemStack h = CraftingManager.getInstance().findMatchingRecipe(craft, worldObj);
		if(g == null)return false;
		if(h == null)return false;
		if(OreDictionary.itemMatches(g, h, true))return true;
		return false;
	}

	public void craft() {
		if(allFound()){
			addItemStack(inventory[0]);
			for(int c = 0; c < 9;c++){
				if(craft.getStackInSlot(c)!= null){
					for(int i = 1; i < this.getSizeInventory();i++){
						if(equal(this.getStackInSlot(i),craft.getStackInSlot(c),c)){
							useItemToCraft(this, i);
							break;
						}
					}
					List<TileEntity> t = UT_Utils.getTiles(worldObj, xCoord, yCoord, zCoord);
					List<IInventory> in = new ArrayList<IInventory>();
					for(TileEntity tile : t)if(tile instanceof IInventory)in.add((IInventory) tile);
					for(IInventory inv : in){
						if(inv instanceof CrafterEntity){
							for(int i = 1; i < inv.getSizeInventory();i++){
								if(equal(inv.getStackInSlot(i),craft.getStackInSlot(c),c)){
									useItemToCraft(inv, i);
									break;
								}
							}
						}else for(int i = 0; i < inv.getSizeInventory();i++){
							if(equal(inv.getStackInSlot(i),craft.getStackInSlot(c),c)){
								useItemToCraft(inv, i);
								break;
							}
						}
					}
				}
			}
		}
	}
	
	public void useItemToCraft(IInventory inv, int slot){
		if(inv instanceof CrafterEntity && slot == 0)return;
		ItemStack item = inv.getStackInSlot(slot);
		if(item != null){
			if (item.getItem().hasContainerItem())
			{
				ItemStack itemContainer = item.getItem().getContainerItemStack(item);
				if(itemContainer == null){
					inv.decrStackSize(slot, 1);
					return;
				}else if(itemContainer.isItemStackDamageable() && itemContainer.getItemDamage() > itemContainer.getMaxDamage()){
					itemContainer = null;
					inv.setInventorySlotContents(slot, itemContainer);
					return;
				}else{
					inv.setInventorySlotContents(slot, itemContainer);
				}
			}else{
				inv.decrStackSize(slot, 1);
			}
		}
	}

	public boolean addItemStack(ItemStack i){
		if(i == null)return true;
		for(int s = 1;s < this.getSizeInventory();s++){	
			if (this.inventory[s] == null)
			{
				this.inventory[s] = i.copy();
				return true;
			}
			else if (this.inventory[s].isItemEqual(i))
			{
				if(inventory[s].stackSize + i.stackSize <= getInventoryStackLimit()){
				inventory[s].stackSize += i.stackSize;
				return true;
				}
			}
		}
		return false;
	}
	
	public void emptyCraft() {
		for(int i = 0; i < 9;i++){
			craft.setInventorySlotContents(i, null);
			UT_Utils.sendPacket(this, i, 0 ,0);
		}
	}
	
	//Inventory
	
	@Override
	public int getSizeInventory() {
		return inventory.length;
	}
	
	 public void onInventoryChanged(){
		 super.onInventoryChanged();
		 update();
	 }

	@Override
	public ItemStack getStackInSlot(int i) {
		return inventory[i];
	}

	@Override
	public ItemStack decrStackSize(int slot, int amount) {
		ItemStack itemStack = getStackInSlot(slot);
		if (itemStack != null) {
			if (itemStack.stackSize <= amount) {
				setInventorySlotContents(slot, null);
			}
			else {
				itemStack = itemStack.splitStack(amount);
				if (itemStack.stackSize == 0) {
					setInventorySlotContents(slot, null);
				}
			}
		}
		return itemStack;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slot) {
		if (inventory[slot] != null) {
			ItemStack itemStack = inventory[slot];
			inventory[slot] = null;
			return itemStack;
		}
		else
			return null;
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack itemStack) {
		inventory[slot] = itemStack;

		if (itemStack != null && itemStack.stackSize > this.getInventoryStackLimit()) {
			itemStack.stackSize = this.getInventoryStackLimit();
		}

		this.onInventoryChanged();
	}

	@Override
	public String getInvName() {
		return "Crafter";
	}

	@Override
	public boolean isInvNameLocalized() {
		return false;
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer entityplayer) {
		return true;
	}

	@Override
	public void openChest() {}

	@Override
	public void closeChest() {}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack) {
		return i < 1 ? false : true;
	}
	
	//Save & Load

	@Override
	public void readFromNBT(NBTTagCompound nbtTagCompound) {

		super.readFromNBT(nbtTagCompound);
		//inv 1
		NBTTagList tagList = nbtTagCompound.getTagList("Inventory");
		inventory = new ItemStack[this.getSizeInventory()];
		for (int i = 0; i < tagList.tagCount(); ++i) {
			NBTTagCompound tagCompound = (NBTTagCompound) tagList.tagAt(i);
			byte slot = tagCompound.getByte("Slot");
			if (slot >= 0 && slot < inventory.length) {
				inventory[slot] = ItemStack.loadItemStackFromNBT(tagCompound);
			}
		}
		//inv 2
		NBTTagList tagList2 = nbtTagCompound.getTagList("Inventory2");
		craft = new InventoryCrafter(this, 3, 3);
		for (int i = 0; i < tagList2.tagCount(); ++i) {
			NBTTagCompound tagCompound = (NBTTagCompound) tagList2.tagAt(i);
			byte slot = tagCompound.getByte("Slot");
			if (slot >= 0 && slot < craft.getSizeInventory()) {
				craft.setInventorySlotContents(slot, ItemStack.loadItemStackFromNBT(tagCompound));
			}
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound nbtTagCompound) {

		super.writeToNBT(nbtTagCompound);
		//inv 1
		NBTTagList tagList = new NBTTagList();
		for (int currentIndex = 0; currentIndex < inventory.length; ++currentIndex) {
			if (inventory[currentIndex] != null) {
				NBTTagCompound tagCompound = new NBTTagCompound();
				tagCompound.setByte("Slot", (byte) currentIndex);
				inventory[currentIndex].writeToNBT(tagCompound);
				tagList.appendTag(tagCompound);
			}
		}
		nbtTagCompound.setTag("Inventory", tagList);
		//inv 2
		NBTTagList tagList2 = new NBTTagList();
		for (int currentIndex = 0; currentIndex < craft.getSizeInventory(); ++currentIndex) {
			if (craft.getStackInSlot(currentIndex) != null) {
				NBTTagCompound tagCompound = new NBTTagCompound();
				tagCompound.setByte("Slot", (byte) currentIndex);
				craft.getStackInSlot(currentIndex).writeToNBT(tagCompound);
				tagList2.appendTag(tagCompound);
			}
		}
		nbtTagCompound.setTag("Inventory2", tagList2);
	}
	

	//Synchronization

	public void sendGUINetworkData(CrafterContainer crafterContainer,
			ICrafting iCrafting) {
		for(int g=0; g < 9; g++){
			if(found.containsKey(g))iCrafting.sendProgressBarUpdate(crafterContainer, g, (found.get(g))? 1 : 0);
		}
	}

	public void getGUINetworkData(int i, int j) {
		if(found.containsKey(i)){
			found.remove(i);
			found.put(i, (j==1));
		}else{
			found.put(i, (j==1));
		}
	}
	
	@Override
	public Packet getDescriptionPacket() {
		NBTTagCompound nbt = new NBTTagCompound();
		this.writeToNBT(nbt);
		return new Packet132TileEntityData(this.xCoord, this.yCoord, this.zCoord, 0, nbt);
	}

	@Override
	public void onDataPacket(INetworkManager net, Packet132TileEntityData pkt) {
		this.readFromNBT(pkt.data);
		this.update();
	}

}