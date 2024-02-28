package de.pilz.customnpcsadvanced.network.handlers.client;

import net.minecraft.client.Minecraft;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import de.pilz.customnpcsadvanced.CustomNpcPlusExtras;
import de.pilz.customnpcsadvanced.client.gui.EnumGuiType;
import de.pilz.customnpcsadvanced.feature.TileEntityNpcManager;
import de.pilz.customnpcsadvanced.network.messages.server.MessageOpenGuiEditTileEntity;

public class HandlerOpenGuiEditTileEntity implements IMessageHandler<MessageOpenGuiEditTileEntity, IMessage> {

    @Override
    public IMessage onMessage(MessageOpenGuiEditTileEntity message, MessageContext ctx) {
        TileEntityNpcManager.Instance.editingNpc = message.npcData;
        Minecraft mc = Minecraft.getMinecraft();
        mc.thePlayer.openGui(
            CustomNpcPlusExtras.Instance,
            EnumGuiType.EditTileEntityNpc.ordinal(),
            mc.theWorld,
            message.npcData.posX,
            message.npcData.posY,
            message.npcData.posZ);
        return null;
    }
}
