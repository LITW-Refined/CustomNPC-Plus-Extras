package de.pilz.customnpcsadvanced.api;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import de.pilz.customnpcsadvanced.api.data.TileEntityNpcData;
import de.pilz.customnpcsadvanced.api.data.roles.RoleInfo;
import noppes.npcs.client.EntityUtil;
import noppes.npcs.entity.EntityNPCInterface;

public class TileEntityNpc extends EntityNPCInterface {

    public TileEntityNpcData npcData;

    public TileEntityNpc(EntityPlayer player, World world, TileEntityNpcData npcData) {
        super(world);

        // Use player as base for most data
        if (player != null) {
            EntityUtil.Copy(player, this);
        }

        // Adjust position
        setPosition(npcData.posX, npcData.posY, npcData.posZ);

        // Name
        display.setName(npcData.getTitle());

        // Dialogs
        dialogs = npcData.getDialogOptions();
        
        // Role type
        advanced.setRole(npcData.getRole().ordinal());
        
        // Role info
        RoleInfo roleInfo = npcData.getRoleInfo();
        if (roleInfo != null && roleInterface != null) {
            roleInfo.saveToRoleInterface(roleInterface);
        }
    }

    @Override
    public int getEntityId() {
        return npcData.getId()
            .hashCode();
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
    public void onUpdate() {}

    @Override
    public boolean interact(EntityPlayer player) {
        return false;
    }
}
