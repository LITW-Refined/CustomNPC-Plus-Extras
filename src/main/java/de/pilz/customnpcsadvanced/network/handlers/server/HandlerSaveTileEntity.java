package de.pilz.customnpcsadvanced.network.handlers.server;

import net.minecraft.entity.player.EntityPlayerMP;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import de.pilz.customnpcsadvanced.feature.TileEntityNpcManager;
import de.pilz.customnpcsadvanced.network.messages.client.MessageSaveTileEntity;

public class HandlerSaveTileEntity implements IMessageHandler<MessageSaveTileEntity, IMessage> {

    @Override
    public IMessage onMessage(MessageSaveTileEntity message, MessageContext ctx) {
        EntityPlayerMP player = ctx.getServerHandler().playerEntity;

        if (player != null && message.npcData != null) {
            TileEntityNpcManager.Instance.saveNpcData(message.npcData);
        }

        return null;
    }
}
