package de.pilz.customnpcsadvanced.network.messages;

public class MessageOpenGuiEditTileEntityRequest extends BaseMessageTileEntity {

    public MessageOpenGuiEditTileEntityRequest() {}

    public MessageOpenGuiEditTileEntityRequest(int posX, int posY, int posZ) {
        super(posX, posY, posZ);
    }
}
