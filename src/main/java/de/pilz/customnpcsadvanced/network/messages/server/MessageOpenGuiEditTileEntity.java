package de.pilz.customnpcsadvanced.network.messages.server;

import de.pilz.customnpcsadvanced.api.data.TileEntityNpcData;
import de.pilz.customnpcsadvanced.network.messages.BaseMessageTileEntityNpcData;

public class MessageOpenGuiEditTileEntity extends BaseMessageTileEntityNpcData {

    public MessageOpenGuiEditTileEntity() {}

    public MessageOpenGuiEditTileEntity(TileEntityNpcData npcData) {
        super(npcData);
    }
}
