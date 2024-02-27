package de.pilz.customnpcsadvanced.client.gui;

import de.pilz.customnpcsadvanced.network.NetworkManager;
import de.pilz.customnpcsadvanced.network.messages.MessageSaveTileEntity;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.client.NoppesUtil;
import noppes.npcs.client.gui.advanced.GuiNPCDialogNpcOptions;
import noppes.npcs.client.gui.util.GuiNPCInterface2;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.GuiNpcTextField;
import noppes.npcs.client.gui.util.IGuiData;
import noppes.npcs.entity.EntityNPCInterface;

public class GuiEditFancySignNpc extends GuiNPCInterface2 implements IGuiData {

    private GuiNpcTextField tbName;
    private EntityNPCInterface fakeNpc;
    private int tilePosX;
    private int tilePosY;
    private int tilePosZ;

    public GuiEditFancySignNpc(EntityNPCInterface npc, int tilePosX, int tilePosY, int tilePosZ) {
        super(null);
        fakeNpc = npc;
        this.tilePosX = tilePosX;
        this.tilePosY = tilePosY;
        this.tilePosZ = tilePosZ;
    }

    @Override
    public void initGui(){
    	super.initGui();

		int y = guiTop + 10;
		int gap = 21;

        tbName = new GuiNpcTextField(0, this, guiLeft + 85, y, 214, 20, fakeNpc.display.name);
        tbName.setText(fakeNpc.display.getName());
        this.addTextField(tbName);
        this.addButton(new GuiNpcButton(0, guiLeft + 85, y += gap, 214, 20, "gui.dialogs"));
    }

    @Override
	protected void actionPerformed(GuiButton guibutton) {
        GuiNpcButton button = (GuiNpcButton)guibutton;

        if (button.id == 0) {
            save();
            NoppesUtil.openGUI(player, new GuiNPCDialogNpcOptions(npc, this));
        }
    }

    @Override
    public void setGuiData(NBTTagCompound compound) {
    }

    @Override
    public void save() {
        NetworkManager.netWrap.sendToServer(new MessageSaveTileEntity(tilePosX, tilePosY, tilePosZ, tbName.getText()));
        player.worldObj.markBlockForUpdate(tilePosX, tilePosY, tilePosZ);
    }
}
