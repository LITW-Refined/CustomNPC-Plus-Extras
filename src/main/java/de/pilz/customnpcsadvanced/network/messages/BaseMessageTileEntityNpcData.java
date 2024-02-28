package de.pilz.customnpcsadvanced.network.messages;

import java.io.IOException;

import net.minecraft.nbt.NBTTagCompound;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import de.pilz.customnpcsadvanced.api.data.TileEntityNpcData;
import de.pilz.customnpcsadvanced.network.NetworkUtils;
import io.netty.buffer.ByteBuf;

public class BaseMessageTileEntityNpcData implements IMessage {

    public TileEntityNpcData npcData;

    public BaseMessageTileEntityNpcData() {}

    public BaseMessageTileEntityNpcData(TileEntityNpcData npcData) {
        this();
        this.npcData = npcData;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        try {
            NBTTagCompound compound = NetworkUtils.readNBT(buf);
            if (compound != null) {
                npcData = new TileEntityNpcData();
                npcData.readFromNBT(compound);
            }
        } catch (IOException ex) {}
    }

    @Override
    public void toBytes(ByteBuf buf) {
        try {
            NBTTagCompound compound = new NBTTagCompound();
            npcData.writeToNBT(compound);
            NetworkUtils.writeNBT(buf, compound);
        } catch (IOException ex) {}
    }
}
