package de.pilz.customnpcsadvanced.network.handlers.server;

import net.minecraft.entity.player.EntityPlayer;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import de.pilz.customnpcsadvanced.CustomNpcPlusExtras;
import de.pilz.customnpcsadvanced.feature.TileEntityNpcManager;
import de.pilz.customnpcsadvanced.network.messages.client.MessageToggleEnableAdvWand;

public class HandlerToggleEnableAdvWand implements IMessageHandler<MessageToggleEnableAdvWand, IMessage> {

    @Override
    public IMessage onMessage(MessageToggleEnableAdvWand message, MessageContext ctx) {
        EntityPlayer player = CustomNpcPlusExtras.proxy.getPlayer(ctx);
        String uuid = player.getUniqueID()
            .toString();
        boolean enabled = !TileEntityNpcManager.Instance.enableAdvWand.getOrDefault(uuid, false);
        TileEntityNpcManager.Instance.enableAdvWand.put(uuid, enabled);
        return null;
    }
}
