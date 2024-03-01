package de.pilz.customnpcsadvanced.network.handlers.server;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import de.pilz.customnpcsadvanced.feature.TileEntityNpcManager;
import de.pilz.customnpcsadvanced.network.messages.client.MessageDeleteTileEntityNpc;

public class HandlerDeleteTileEntityNpc implements IMessageHandler<MessageDeleteTileEntityNpc, IMessage> {

    public HandlerDeleteTileEntityNpc() {}

    @Override
    public IMessage onMessage(MessageDeleteTileEntityNpc message, MessageContext ctx) {
        if (message.npcData != null) {
            TileEntityNpcManager.Instance.removeNpcData(message.npcData);
        }

        return null;
    }
}
