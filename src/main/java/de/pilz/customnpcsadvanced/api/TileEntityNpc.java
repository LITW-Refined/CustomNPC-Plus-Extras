package de.pilz.customnpcsadvanced.api;

import de.pilz.customnpcsadvanced.api.data.TileEntityNpcData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import noppes.npcs.entity.EntityNPCInterface;

public class TileEntityNpc extends EntityNPCInterface {

    public TileEntityNpcData npcData;

    public TileEntityNpc(World world, TileEntityNpcData npcData) {
        super(world);
        this.npcData = npcData;
        display.title = npcData.getTitle();
        dialogs = npcData.getDialogOptions();
    }

    @Override
    public int getEntityId() {
        return npcData.getId().hashCode();
    }
  
    @Override
    public boolean isInvisibleToPlayer(EntityPlayer player) {
       return true;
    }
 
    @Override
    public boolean isInvisible() {
       return true;
    }
 
    @Override
    public void onUpdate() {
    }
 
    @Override
    public boolean interact(EntityPlayer player) {
       return false;
    }
}