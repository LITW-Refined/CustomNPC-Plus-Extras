package de.pilz.customnpcsadvanced.client.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.nbt.NBTTagCompound;
import de.pilz.customnpcsadvanced.api.TileEntityNpc;
import de.pilz.customnpcsadvanced.api.data.TileEntityNpcData;
import de.pilz.customnpcsadvanced.network.NetworkManager;
import de.pilz.customnpcsadvanced.network.messages.client.MessageSaveTileEntity;
import noppes.npcs.client.NoppesUtil;
import noppes.npcs.client.gui.util.GuiNPCInterface2;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.GuiNpcTextField;
import noppes.npcs.client.gui.util.IGuiData;

public class GuiEditTileEntityNpcData extends GuiNPCInterface2 implements IGuiData {

    private GuiNpcTextField tbName;
    private TileEntityNpc fakeNpc;
    private TileEntityNpcData npcData;

    public GuiEditTileEntityNpcData(TileEntityNpc npc, TileEntityNpcData npcData) {
        super(null);
        fakeNpc = npc;
        this.npcData = npcData;
    }

    @Override
    public void initGui() {
        super.initGui();

        int y = guiTop + 10;
        int gap = 21;

        tbName = new GuiNpcTextField(0, this, guiLeft + 85, y, 214, 20, fakeNpc.display.name);
        tbName.setText(fakeNpc.display.getName());
        this.addTextField(tbName);
        this.addButton(new GuiNpcButton(0, guiLeft + 85, y += gap, 214, 20, "gui.dialogOptions"));
    }

    @Override
    protected void actionPerformed(GuiButton guibutton) {
        GuiNpcButton button = (GuiNpcButton) guibutton;

        if (button.id == 0) {
            save();
            NoppesUtil.openGUI(player, new GuiEditTileEntityNpcDialogOptions(fakeNpc, npcData, this));
        }
    }

    @Override
    public void setGuiData(NBTTagCompound compound) {}

    @Override
    public void save() {
        npcData.setTitle(tbName.getText());
        NetworkManager.netWrap.sendToServer(new MessageSaveTileEntity(npcData));
    }
}
