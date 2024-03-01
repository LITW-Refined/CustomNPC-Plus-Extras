package de.pilz.customnpcsadvanced.api.data.roles;

import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.roles.RoleInterface;

public abstract class RoleInfo {

    public abstract void saveToRoleInterface(RoleInterface roleInterface);
    public abstract void loadFromRoleInfo(RoleInterface roleInterface);
    public abstract void readFromNBT(NBTTagCompound nbt);
    public abstract void writeToNBT(NBTTagCompound nbt);
}
