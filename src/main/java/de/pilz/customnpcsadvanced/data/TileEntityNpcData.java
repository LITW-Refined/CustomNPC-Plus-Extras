package de.pilz.customnpcsadvanced.data;

import java.util.HashMap;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import noppes.npcs.controllers.data.Dialog;
import noppes.npcs.controllers.data.DialogOption;

public class TileEntityNpcData {

    protected static final String NBT_KEY = "NpcData";
    protected static final String NBT_KEY_DIALOGS = "Dialogs";
    protected static final String NBT_KEY_DIALOGS_SLOT = "Slot";
    protected static final String NBT_KEY_DIALOGS_OPTION = "Option";
    protected static final String NBT_KEY_NPCNAME = "Title";
    protected static final int DIALOG_OPTIONS_COUNT = 10;
    protected static final String DEFAULT_NPC_NAME = "Fake Entity";

    protected String title = DEFAULT_NPC_NAME;
    protected HashMap<Integer, DialogOption> dialogs = new HashMap<Integer, DialogOption>();

    public TileEntityNpcData() {}

    public TileEntityNpcData(NBTTagCompound nbt) {
        this();
        readFromNBT(nbt);
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void readFromNBT(NBTTagCompound nbt) {
        dialogs = new HashMap<Integer, DialogOption>();

        if (nbt.hasKey(NBT_KEY)) {
            NBTTagCompound compound = nbt.getCompoundTag(NBT_KEY);

            // Title
            if (compound.hasKey(NBT_KEY_NPCNAME)) {
                title = compound.getString(NBT_KEY_NPCNAME);
            } else {
                title = DEFAULT_NPC_NAME;
            }
            de.pilz.customnpcsadvanced.CustomNpcPlusExtras.LOG.warn("READ " + title);

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
        } else {
            title = DEFAULT_NPC_NAME;
        }
    }

    public void writeToNBT(NBTTagCompound nbt) {
        NBTTagCompound compound = new NBTTagCompound();

        // Title
        if (title != null && !title.equals(DEFAULT_NPC_NAME)) {
            compound.setString(NBT_KEY_NPCNAME, title);
            de.pilz.customnpcsadvanced.CustomNpcPlusExtras.LOG.warn("WRITE " + title);
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
        }

        // Write to nbt
        if (compound.hasNoTags()) {
            nbt.removeTag(NBT_KEY);
        } else {
            nbt.setTag(NBT_KEY, compound);
        }
    }

    public static boolean hasNpcData(NBTTagCompound nbt) {
        return nbt.hasKey(NBT_KEY);
    }

    public static TileEntityNpcData loadFromNBT(NBTTagCompound nbt, boolean forceLoad) {
        TileEntityNpcData data;

        if (hasNpcData(nbt)) {
            data = new TileEntityNpcData(nbt);
        } else if (forceLoad) {
            data = new TileEntityNpcData();
        } else {
            data = null;
        }

        return data;
    }

    public static void savetoNBT(NBTTagCompound nbt, TileEntityNpcData data) {
        if (data != null) {
            data.writeToNBT(nbt);
        } else {
            nbt.removeTag(NBT_KEY);
        }
    }
}
