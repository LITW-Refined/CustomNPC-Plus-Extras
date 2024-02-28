package de.pilz.customnpcsadvanced.network.messages.client;

import de.pilz.customnpcsadvanced.api.data.TileEntityNpcData;
import de.pilz.customnpcsadvanced.network.messages.BaseMessageTileEntityNpcData;

public class MessageSaveTileEntity extends BaseMessageTileEntityNpcData {

    public MessageSaveTileEntity(TileEntityNpcData npcData) {
        super(npcData);
    }
}
