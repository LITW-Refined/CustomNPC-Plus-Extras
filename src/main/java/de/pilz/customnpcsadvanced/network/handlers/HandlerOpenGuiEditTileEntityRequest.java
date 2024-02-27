package de.pilz.customnpcsadvanced.network.handlers;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import de.pilz.customnpcsadvanced.network.NetworkManager;
import de.pilz.customnpcsadvanced.network.messages.MessageOpenGuiEditTileEntityOk;
import de.pilz.customnpcsadvanced.network.messages.MessageOpenGuiEditTileEntityRequest;
import de.pilz.customnpcsadvanced.te.ITileEntityNpc;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.config.ConfigMain;

public class HandlerOpenGuiEditTileEntityRequest
    implements IMessageHandler<MessageOpenGuiEditTileEntityRequest, IMessage> {

    @Override
    public IMessage onMessage(MessageOpenGuiEditTileEntityRequest message, MessageContext ctx) {
        EntityPlayerMP player = ctx.getServerHandler().playerEntity;

        if (player != null && (!ConfigMain.OpsOnly || NoppesUtilServer.isOp(player))) {
            TileEntity tile = (TileEntity) player.worldObj.getTileEntity(message.posX, message.posY, message.posZ);
            ITileEntityNpc tilem = (ITileEntityNpc) tile;

            if (tilem != null) {
                NetworkManager.netWrap
                    .sendTo(new MessageOpenGuiEditTileEntityOk(message.posX, message.posY, message.posZ), player);
            }
        }

        return null;
    }
}
