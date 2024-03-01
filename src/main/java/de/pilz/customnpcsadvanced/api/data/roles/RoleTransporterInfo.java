package de.pilz.customnpcsadvanced.api.data.roles;

import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.roles.RoleInterface;
import noppes.npcs.roles.RoleTransporter;

public class RoleTransporterInfo extends RoleInfo {

    public int transportId = -1;

    @Override
    public void saveToRoleInterface(RoleInterface roleInterface) {
        if (roleInterface instanceof RoleTransporter) {
            RoleTransporter i = (RoleTransporter)roleInterface;
            NBTTagCompound nbt = new NBTTagCompound();
            writeToNBT(nbt);
            i.readFromNBT(nbt);
        }
    }

    @Override
    public void loadFromRoleInfo(RoleInterface roleInterface) {
        if (roleInterface instanceof RoleTransporter) {
            RoleTransporter i = (RoleTransporter)roleInterface;
            NBTTagCompound nbt = new NBTTagCompound();
            i.writeToNBT(nbt);
            readFromNBT(nbt);
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        this.transportId = nbt.getInteger("TransporterId");
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        nbt.setInteger("TransporterId", this.transportId);
    }
}
