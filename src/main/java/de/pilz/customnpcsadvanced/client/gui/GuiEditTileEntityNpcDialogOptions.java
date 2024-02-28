package de.pilz.customnpcsadvanced.client.gui;

import net.minecraft.client.gui.GuiScreen;

import de.pilz.customnpcsadvanced.api.TileEntityNpc;
import de.pilz.customnpcsadvanced.api.data.TileEntityNpcData;
import de.pilz.customnpcsadvanced.network.NetworkManager;
import de.pilz.customnpcsadvanced.network.messages.client.MessageSaveTileEntity;
import noppes.npcs.client.gui.advanced.GuiNPCDialogNpcOptions;

public class GuiEditTileEntityNpcDialogOptions extends GuiNPCDialogNpcOptions {

    private TileEntityNpc fakeNpc;
    private TileEntityNpcData npcData;

    public GuiEditTileEntityNpcDialogOptions(TileEntityNpc npc, TileEntityNpcData npcData, GuiScreen parent) {
        super(npc, parent);
        fakeNpc = npc;
        this.npcData = npcData;
    }

    @Override
    public void save() {
        super.save();
        npcData.setDialogOptions(fakeNpc.dialogs);
        NetworkManager.netWrap.sendToServer(new MessageSaveTileEntity(npcData));
    }
}
