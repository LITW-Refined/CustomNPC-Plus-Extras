package de.pilz.customnpcsadvanced.network.handlers;

import net.minecraft.client.Minecraft;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import de.pilz.customnpcsadvanced.CustomNpcPlusExtras;
import de.pilz.customnpcsadvanced.client.gui.EnumGuiType;
import de.pilz.customnpcsadvanced.network.messages.MessageOpenGuiEditTileEntityOk;

public class HandlerOpenGuiEditTileEntityOk implements IMessageHandler<MessageOpenGuiEditTileEntityOk, IMessage> {

    @Override
    public IMessage onMessage(MessageOpenGuiEditTileEntityOk message, MessageContext ctx) {
        Minecraft mc = Minecraft.getMinecraft();
        mc.thePlayer.openGui(
            CustomNpcPlusExtras.Instance,
            EnumGuiType.EditTileEntityNpc.ordinal(),
            mc.theWorld,
            message.posX,
            message.posY,
            message.posZ);
        return null;
    }
}
