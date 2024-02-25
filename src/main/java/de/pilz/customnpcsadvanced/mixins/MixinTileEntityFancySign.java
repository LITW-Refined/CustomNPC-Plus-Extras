package de.pilz.customnpcsadvanced.mixins;

import java.util.HashMap;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import de.pilz.customnpcsadvanced.te.IMixinTileEntityFancySign;
import jds.bibliocraft.tileentities.TileEntityFancySign;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import noppes.npcs.controllers.data.Dialog;
import noppes.npcs.controllers.data.DialogOption;

@Mixin(TileEntityFancySign.class)
public abstract class MixinTileEntityFancySign extends TileEntity implements ISidedInventory, IMixinTileEntityFancySign {

    private static final String NBT_KEY_QUESTS = "NpcQuests";
    private static final String NBT_KEY_NPCNAME = "NpcName";
    private static final int DIALOG_OPTIONS_COUNT = 10;
    private static final String DEFAULT_NPC_NAME = "Fancy Sign";

    private HashMap<Integer, DialogOption> cnpcpextras$dialogs = new HashMap<Integer, DialogOption>();
    private String cnpcpextras$npcname = DEFAULT_NPC_NAME;

    @Unique
    public HashMap<Integer, DialogOption> getDialogOptions() {
        return cnpcpextras$dialogs;
    }

    @Unique
    public void setDialogOptions(HashMap<Integer, DialogOption> options) {
        cnpcpextras$dialogs = options;
    }
    
    @Unique
    public Dialog getDialog(EntityPlayer player) {
        for (DialogOption option : cnpcpextras$dialogs.values()) {
            if (option.isAvailable(player)) {
                return option.getDialog();
            }
        }
        return null;
    }

    @Unique
    public String getNpcName() {
        return cnpcpextras$npcname;
    }

    @Unique
    public void setNpcName(String name) {
        cnpcpextras$npcname = name;
    }

    @Shadow(remap = false)
    public abstract boolean isLocked();

    @Shadow(remap = false)
    public abstract String getLockee();

    @Inject(
        method = "readFromNBT(Lnet/minecraft/nbt/NBTTagCompound;)V",
        at = @At("RETURN"),
        cancellable = true)
    public void cnpcpextras$readEntityFromNBT(NBTTagCompound compound, CallbackInfo callback) {
        if (compound.hasKey(NBT_KEY_NPCNAME)) {
            cnpcpextras$npcname = compound.getString(NBT_KEY_NPCNAME);
        }

        if (compound.hasKey(NBT_KEY_QUESTS)) {
            NBTTagList tagList = compound.getTagList(NBT_KEY_QUESTS, DIALOG_OPTIONS_COUNT);
            cnpcpextras$dialogs = new HashMap<Integer, DialogOption>();
            
            for (int i = 0; i < tagList.tagCount(); i++) {
                NBTTagCompound nbttagcompound = tagList.getCompoundTagAt(i);
                int slot = nbttagcompound.getInteger("DialogSlot");
                DialogOption option = new DialogOption();
                option.readNBT(nbttagcompound.getCompoundTag("NPCDialog"));
                cnpcpextras$dialogs.put(slot, option);
            }
        }
    }

    @Inject(
        method = "writeToNBT(Lnet/minecraft/nbt/NBTTagCompound;)V",
        at = @At("RETURN"),
        cancellable = true)
    public void cnpcpextras$writeEntityToNBT(NBTTagCompound compound, CallbackInfo callback) {
        // Name
        if (cnpcpextras$npcname.equals(DEFAULT_NPC_NAME)) {
            compound.removeTag(NBT_KEY_NPCNAME);
        } else {
            compound.setString(NBT_KEY_NPCNAME, cnpcpextras$npcname);
        }

        // Dialogs
        NBTTagList nbttaglist = new NBTTagList();
		for (int slot : cnpcpextras$dialogs.keySet()) {
			NBTTagCompound nbttagcompound = new NBTTagCompound();
			nbttagcompound.setInteger("DialogSlot", slot);
			nbttagcompound.setTag("NPCDialog", cnpcpextras$dialogs.get(slot).writeNBT());
			nbttaglist.appendTag(nbttagcompound);
		}
		compound.setTag(NBT_KEY_QUESTS, nbttaglist);
    }
}
