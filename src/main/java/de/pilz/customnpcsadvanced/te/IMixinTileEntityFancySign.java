package de.pilz.customnpcsadvanced.te;

import java.util.HashMap;

import net.minecraft.entity.player.EntityPlayer;
import noppes.npcs.controllers.data.Dialog;
import noppes.npcs.controllers.data.DialogOption;

public interface IMixinTileEntityFancySign {
    public HashMap<Integer, DialogOption> getDialogOptions(); 
    public void setDialogOptions(HashMap<Integer, DialogOption> options);
    public Dialog getDialog(EntityPlayer player);
    public String getNpcName();
    public void setNpcName(String name);
}
