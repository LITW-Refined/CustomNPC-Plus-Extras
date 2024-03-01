package de.pilz.customnpcsadvanced.network.handlers.server;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import de.pilz.customnpcsadvanced.CustomNpcPlusExtras;
import de.pilz.customnpcsadvanced.api.TileEntityNpc;
import de.pilz.customnpcsadvanced.api.data.roles.RoleInfo;
import de.pilz.customnpcsadvanced.feature.TileEntityNpcManager;
import de.pilz.customnpcsadvanced.network.messages.client.MessageSaveTileEntity;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.entity.EntityNPCInterface;

public class HandlerSaveTileEntity implements IMessageHandler<MessageSaveTileEntity, IMessage> {

    public HandlerSaveTileEntity() {}

    @Override
    public IMessage onMessage(MessageSaveTileEntity message, MessageContext ctx) {
        if (message.npcData != null) {
            EntityNPCInterface npcInterface = NoppesUtilServer.getEditingNpc(CustomNpcPlusExtras.proxy.getPlayer(ctx));

            if (npcInterface instanceof TileEntityNpc) {
                TileEntityNpc npc = (TileEntityNpc) npcInterface;

                // ------------------------------
                // Copy dialogs and other stuff here on server side because CNPC+ saves them there always.
                // ------------------------------

                // Dialogs
                message.npcData.setDialogOptions(npc.dialogs);

                // Role
                if (npc.advanced.role == message.npcData.getRole()) {
                    RoleInfo roleInfo = message.npcData.getRoleInfo();
                    if (roleInfo != null && npc.roleInterface != null) {
                        roleInfo.loadFromRoleInfo(npc.roleInterface);
                    }
                } else {
                    // Write back the new role changes, so in a next step the client can edit the role interface
                    npc.advanced.setRole(message.npcData.getRole().ordinal());
                }

                // ----------------
                // Save dialog data
                // ----------------

                TileEntityNpcManager.Instance.saveNpcData(message.npcData);
            }
        }

        return null;
    }
}
