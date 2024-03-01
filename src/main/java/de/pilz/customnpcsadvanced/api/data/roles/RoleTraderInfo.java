package de.pilz.customnpcsadvanced.api.data.roles;

import java.util.HashMap;

import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.NBTTags;
import noppes.npcs.NpcMiscInventory;
import noppes.npcs.roles.RoleInterface;
import noppes.npcs.roles.RoleTrader;

public class RoleTraderInfo extends RoleInfo {
    public String marketName = "";
    public NpcMiscInventory inventoryCurrency = new NpcMiscInventory(36);
    public NpcMiscInventory inventorySold = new NpcMiscInventory(18);
    public boolean ignoreDamage = false;
    public boolean ignoreNBT = false;
    public boolean recordHistory = false;
    public int[] purchases = new int[18];
    public int[] disableSlot = new int[18];
    public HashMap<String, int[]> playerPurchases = new HashMap<String, int[]>();
    public HashMap<String, int[]> playerDisableSlot = new HashMap<String, int[]>();

    @Override
    public void saveToRoleInterface(RoleInterface roleInterface) {
        if (roleInterface instanceof RoleTrader) {
            RoleTrader i = (RoleTrader)roleInterface;
            NBTTagCompound nbt = new NBTTagCompound();
            writeToNBT(nbt);
            i.readFromNBT(nbt);
        }
    }

    @Override
    public void loadFromRoleInfo(RoleInterface roleInterface) {
        if (roleInterface instanceof RoleTrader) {
            RoleTrader i = (RoleTrader)roleInterface;
            NBTTagCompound nbt = new NBTTagCompound();
            i.writeToNBT(nbt);
            readFromNBT(nbt);
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        marketName = nbt.getString("TraderMarket");
        this.inventoryCurrency.setFromNBT(nbt.getCompoundTag("TraderCurrency"));
        this.inventorySold.setFromNBT(nbt.getCompoundTag("TraderSold"));
        this.ignoreDamage = nbt.getBoolean("TraderIgnoreDamage");
        this.ignoreNBT = nbt.getBoolean("TraderIgnoreNBT");
        this.recordHistory = nbt.getBoolean("RecordHistory");
        this.disableSlot = nbt.getIntArray("DisableSlot");
        this.playerDisableSlot = NBTTags.getStringIntegerArrayMap(nbt.getTagList("PlayerDisableSlot", 10), 18);
        
        if (this.recordHistory) {
           this.purchases = nbt.getIntArray("Purchases");
           this.playerPurchases = NBTTags.getStringIntegerArrayMap(nbt.getTagList("PlayerPurchases", 10), 18);
        }
  
        int i;
        if (this.purchases == null || this.purchases.length != 18) {
           this.purchases = new int[18];
  
           for(i = 0; i < this.purchases.length; ++i) {
              this.purchases[i] = 0;
           }
        }
  
        if (this.disableSlot == null || this.disableSlot.length != 18) {
           this.disableSlot = new int[18];
  
           for(i = 0; i < this.disableSlot.length; ++i) {
              this.disableSlot[i] = 0;
           }
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        nbt.setString("TraderMarket", this.marketName);
        nbt.setTag("TraderCurrency", this.inventoryCurrency.getToNBT());
        nbt.setTag("TraderSold", this.inventorySold.getToNBT());
        nbt.setBoolean("TraderIgnoreDamage", this.ignoreDamage);
        nbt.setBoolean("TraderIgnoreNBT", this.ignoreNBT);
        nbt.setBoolean("RecordHistory", this.recordHistory);
        nbt.setIntArray("DisableSlot", this.disableSlot);
        nbt.setTag("PlayerDisableSlot", NBTTags.nbtStringIntegerArrayMap(this.playerDisableSlot));

        if (this.recordHistory) {
            nbt.setIntArray("Purchases", this.purchases);
           nbt.setTag("PlayerPurchases", NBTTags.nbtStringIntegerArrayMap(this.playerPurchases));
        }
    }
}
