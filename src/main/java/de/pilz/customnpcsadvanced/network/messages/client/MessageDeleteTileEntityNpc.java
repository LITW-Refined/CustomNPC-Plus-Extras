package de.pilz.customnpcsadvanced.network.messages.client;

import de.pilz.customnpcsadvanced.api.data.TileEntityNpcData;
import de.pilz.customnpcsadvanced.network.messages.BaseMessageTileEntityNpcData;

public class MessageDeleteTileEntityNpc extends BaseMessageTileEntityNpcData {

    public MessageDeleteTileEntityNpc() {}

    public MessageDeleteTileEntityNpc(TileEntityNpcData npcData) {
        super(npcData);
    }
}
