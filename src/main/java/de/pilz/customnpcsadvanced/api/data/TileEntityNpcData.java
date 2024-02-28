package de.pilz.customnpcsadvanced.api.data;

import java.util.HashMap;
import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;

import noppes.npcs.controllers.data.Dialog;
import noppes.npcs.controllers.data.DialogOption;

public class TileEntityNpcData {

    protected static final String NBT_KEY_DIALOGS = "Dialogs";
    protected static final String NBT_KEY_DIALOGS_SLOT = "Slot";
    protected static final String NBT_KEY_DIALOGS_OPTION = "Option";
    protected static final String NBT_KEY_NPCNAME = "Title";
    protected static final String NBT_KEY_ID = "Id";
    protected static final String NBT_KEY_POSITION_X = "PosX";
    protected static final String NBT_KEY_POSITION_Y = "PosY";
    protected static final String NBT_KEY_POSITION_Z = "PosZ";
    protected static final int DIALOG_OPTIONS_COUNT = 10;
    protected static final String DEFAULT_NPC_NAME = "Fake Entity";

    public Integer posX = 0;
    public Integer posY = 0;
    public Integer posZ = 0;
    protected String id = UUID.randomUUID()
        .toString();
    protected String title = DEFAULT_NPC_NAME;
    protected HashMap<Integer, DialogOption> dialogs = new HashMap<Integer, DialogOption>();

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
        dialogs = options;
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

    public boolean equals(String id) {
        return this.id == id;
    }

    public boolean equals(TileEntityNpcData other) {
        return other.equals(id);
    }

    public boolean equals(TileEntity other) {
        return other.xCoord == posX && other.yCoord == posY && other.zCoord == posZ;
    }

    public void clone(TileEntityNpcData newData) {
        posX = newData.posX;
        posY = newData.posY;
        posZ = newData.posZ;
        title = newData.title;
        dialogs.clear();
        dialogs.putAll(newData.dialogs);
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
    }
}
