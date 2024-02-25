package de.pilz.customnpcsadvanced.network.messages;

import de.pilz.customnpcsadvanced.network.NetworkUtils;
import io.netty.buffer.ByteBuf;

public class MessageSaveTileEntity extends BaseMessageTileEntity {

    public String name;

    public MessageSaveTileEntity() {}

    public MessageSaveTileEntity(int posX, int posY, int posZ, String name) {
        super(posX, posY, posZ);
        this.name = name;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        super.fromBytes(buf);
        name = NetworkUtils.readString(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        super.toBytes(buf);
        NetworkUtils.writeString(buf, name);
    }
}
