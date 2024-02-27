package de.pilz.customnpcsadvanced.network.handlers;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import de.pilz.customnpcsadvanced.api.ITileEntityNpc;
import de.pilz.customnpcsadvanced.network.messages.MessageSaveTileEntity;

public class HandlerSaveTileEntity implements IMessageHandler<MessageSaveTileEntity, IMessage> {

    @Override
    public IMessage onMessage(MessageSaveTileEntity message, MessageContext ctx) {
        EntityPlayerMP player = ctx.getServerHandler().playerEntity;

        TileEntity tile = (TileEntity) player.worldObj.getTileEntity(message.posX, message.posY, message.posZ);
        ITileEntityNpc tilem = (ITileEntityNpc) tile;

        if (tilem != null) {
            tilem.getNpcData(true)
                .setTitle(message.name);
            player.worldObj.markBlockForUpdate(message.posX, message.posY, message.posZ);
        }

        return null;
    }
}
