package de.pilz.customnpcsadvanced.client.gui.roles;

import de.pilz.customnpcsadvanced.api.TileEntityNpc;
import de.pilz.customnpcsadvanced.api.data.TileEntityNpcData;
import de.pilz.customnpcsadvanced.network.NetworkManager;
import de.pilz.customnpcsadvanced.network.messages.client.MessageSaveTileEntity;
import noppes.npcs.client.gui.roles.GuiNpcTransporter;

public class GuiEditTileEntityNpcTransporter extends GuiNpcTransporter {

    public final TileEntityNpc fakeNpc;
    public final TileEntityNpcData npcData;

    public GuiEditTileEntityNpcTransporter(TileEntityNpc npc, TileEntityNpcData npcData) {
        super(npc);
        fakeNpc = npc;
        this.npcData = npcData;
    }

    @Override
    public void save() {
        super.save();
        // Also here, don't save ourselfe.
        NetworkManager.netWrap.sendToServer(new MessageSaveTileEntity(npcData));
    }
}
