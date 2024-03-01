package de.pilz.customnpcsadvanced.api.data;

import java.util.HashMap;
import java.util.UUID;

import de.pilz.customnpcsadvanced.api.data.roles.RoleInfo;
import de.pilz.customnpcsadvanced.api.data.roles.RoleTraderInfo;
import de.pilz.customnpcsadvanced.api.data.roles.RoleTransporterInfo;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import noppes.npcs.constants.EnumRoleType;
import noppes.npcs.controllers.data.Dialog;
import noppes.npcs.controllers.data.DialogOption;

public class TileEntityNpcData {

    public static final String NBT_KEY_ROLE_TYPE = "RoleType";
    public static final String NBT_KEY_ROLE_INFO = "RoleInfo";
    public static final String NBT_KEY_DIALOGS = "Dialogs";
    public static final String NBT_KEY_DIALOGS_SLOT = "Slot";
    public static final String NBT_KEY_DIALOGS_OPTION = "Option";
    public static final String NBT_KEY_NPCNAME = "Title";
    public static final String NBT_KEY_ID = "Id";
    public static final String NBT_KEY_POSITION_X = "PosX";
    public static final String NBT_KEY_POSITION_Y = "PosY";
    public static final String NBT_KEY_POSITION_Z = "PosZ";
    public static final int DIALOG_OPTIONS_COUNT = 10;
    public static final String DEFAULT_NPC_NAME = "Fake Entity";

    public Integer posX = 0;
    public Integer posY = 0;
    public Integer posZ = 0;
    protected String id = UUID.randomUUID()
        .toString();
    protected String title = DEFAULT_NPC_NAME;
    protected HashMap<Integer, DialogOption> dialogs = new HashMap<Integer, DialogOption>();
    protected EnumRoleType role = EnumRoleType.None;
    protected RoleInfo roleInfo = null;

    public TileEntityNpcData() {}

    public TileEntityNpcData(NBTTagCompound nbt) {
        this();
        readFromNBT(nbt);
    }

    public TileEntityNpcData(TileEntity te) {
        this();
        setPosition(te);
    }

    public HashMap<Integer, DialogOption> getDialogOptions() {
        return dialogs;
    }

    public void setDialogOptions(HashMap<Integer, DialogOption> options) {
        dialogs.clear();
        for (int slot : options.keySet()) {
            dialogs.put(slot, options.get(slot));
        }
    }

    public Dialog getDialog(EntityPlayer player) {
        for (DialogOption option : dialogs.values()) {
            if (option.isAvailable(player)) {
                return option.getDialog();
            }
        }
        return null;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPosition(int x, int y, int z) {
        posX = x;
        posY = y;
        posZ = z;
    }

    public void setPosition(TileEntity te) {
        setPosition(te.xCoord, te.yCoord, te.zCoord);
    }

    public EnumRoleType getRole() {
        return role;
    }

    public EnumRoleType setRole(EnumRoleType newRole) {
        if (role != newRole) {
            if (newRole == EnumRoleType.Trader) {
                roleInfo = new RoleTraderInfo();
            } else if (newRole == EnumRoleType.Transporter) {
                roleInfo = new RoleTransporterInfo();
            } else {
                roleInfo = null;
                newRole = EnumRoleType.None;
            }
            role = newRole;
        }
        return role;
    }

    public RoleInfo getRoleInfo() {
        return roleInfo;
    }

    public boolean equals(String id) {
        return this.id.equals(id);
    }

    public boolean equals(TileEntityNpcData other) {
        return other.equals(id);
    }

    public boolean equals(TileEntity other) {
        return other.xCoord == posX && other.yCoord == posY && other.zCoord == posZ;
    }

    public void clone(TileEntityNpcData newData) {
        NBTTagCompound nbt = new NBTTagCompound();
        newData.writeToNBT(nbt);
        readFromNBT(nbt);
    }

    public void readFromNBT(NBTTagCompound compound) {
        dialogs = new HashMap<Integer, DialogOption>();

        // Id
        id = compound.getString(NBT_KEY_ID);

        // Position
        posX = compound.getInteger(NBT_KEY_POSITION_X);
        posY = compound.getInteger(NBT_KEY_POSITION_Y);
        posZ = compound.getInteger(NBT_KEY_POSITION_Z);

        // Title
        if (compound.hasKey(NBT_KEY_NPCNAME)) {
            title = compound.getString(NBT_KEY_NPCNAME);
        } else {
            title = DEFAULT_NPC_NAME;
        }

        // Dialogs
        if (compound.hasKey(NBT_KEY_DIALOGS)) {
            NBTTagList tagList = compound.getTagList(NBT_KEY_DIALOGS, DIALOG_OPTIONS_COUNT);

            for (int i = 0; i < tagList.tagCount(); i++) {
                NBTTagCompound nbttagcompound = tagList.getCompoundTagAt(i);
                int slot = nbttagcompound.getInteger(NBT_KEY_DIALOGS_SLOT);
                DialogOption option = new DialogOption();
                option.readNBT(nbttagcompound.getCompoundTag(NBT_KEY_DIALOGS_OPTION));
                dialogs.put(slot, option);
            }
        }

        // Role type
        if (compound.hasKey(NBT_KEY_ROLE_TYPE)) {
            setRole(EnumRoleType.values()[compound.getInteger(NBT_KEY_ROLE_TYPE)]);
        } else {
            setRole(EnumRoleType.None);
        }

        // Role info
        if (compound.hasKey(NBT_KEY_ROLE_INFO) && roleInfo != null) {
            NBTTagCompound nbtRoleInfo = compound.getCompoundTag(NBT_KEY_ROLE_INFO);
            roleInfo.readFromNBT(nbtRoleInfo);
        } else {
            roleInfo = null;
        }
    }

    public NBTTagCompound getAsNBT() {
        NBTTagCompound compound = new NBTTagCompound();
        writeToNBT(compound);
        return compound;
    }

    public void writeToNBT(NBTTagCompound compound) {
        // Id
        compound.setString(NBT_KEY_ID, id);

        // Position
        compound.setInteger(NBT_KEY_POSITION_X, posX);
        compound.setInteger(NBT_KEY_POSITION_Y, posY);
        compound.setInteger(NBT_KEY_POSITION_Z, posZ);

        // Title
        if (title != null && !title.equals(DEFAULT_NPC_NAME)) {
            compound.setString(NBT_KEY_NPCNAME, title);
        } else {
            compound.removeTag(NBT_KEY_NPCNAME);
        }

        // Dialogs
        NBTTagList nbtDialogs = new NBTTagList();
        for (int slot : dialogs.keySet()) {
            NBTTagCompound nbttagcompound = new NBTTagCompound();
            nbttagcompound.setInteger(NBT_KEY_DIALOGS_SLOT, slot);
            nbttagcompound.setTag(
                NBT_KEY_DIALOGS_OPTION,
                dialogs.get(slot)
                    .writeNBT());
            nbtDialogs.appendTag(nbttagcompound);
        }
        if (nbtDialogs.tagCount() > 0) {
            compound.setTag(NBT_KEY_DIALOGS, nbtDialogs);
        } else {
            compound.removeTag(NBT_KEY_DIALOGS);
        }

        // Role type
        if (role != EnumRoleType.None) {
            compound.setInteger(NBT_KEY_ROLE_TYPE, role.ordinal());
        } else {
            compound.removeTag(NBT_KEY_ROLE_TYPE);
        }

        // Role info
        if (roleInfo != null) {
            NBTTagCompound nbtRoleInfo = new NBTTagCompound();
            roleInfo.writeToNBT(nbtRoleInfo);
            compound.setTag(NBT_KEY_ROLE_INFO, nbtRoleInfo);
        } else {
            compound.removeTag(NBT_KEY_ROLE_INFO);
        }
    }
}
