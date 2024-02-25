package de.pilz.customnpcsadvanced.network.handlers;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import de.pilz.customnpcsadvanced.CustomNpcPlusExtras;
import de.pilz.customnpcsadvanced.network.messages.MessageSaveTileEntity;
import de.pilz.customnpcsadvanced.te.IMixinTileEntityFancySign;
import jds.bibliocraft.tileentities.TileEntityFancySign;
import net.minecraft.entity.player.EntityPlayerMP;

public class HandlerSaveTileEntity implements IMessageHandler<MessageSaveTileEntity, IMessage> {

    @Override
    public IMessage onMessage(MessageSaveTileEntity message, MessageContext ctx) {
        EntityPlayerMP player = ctx.getServerHandler().playerEntity;

        CustomNpcPlusExtras.LOG.warn("FANCY SIGN SAVE");
        TileEntityFancySign tile = (TileEntityFancySign)player.worldObj.getTileEntity(message.posX, message.posY, message.posZ);
        IMixinTileEntityFancySign tilem = (IMixinTileEntityFancySign)tile;
        tilem.setNpcName(message.name);
        player.worldObj.markBlockForUpdate(message.posX, message.posY, message.posZ);

        return null;
    }
}