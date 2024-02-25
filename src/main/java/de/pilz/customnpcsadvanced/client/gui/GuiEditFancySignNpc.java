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

    public GuiEditFancySignNpc(EntityNPCInterface npc) {
        super(npc);
    }

    @Override
    public void initGui(){
    	super.initGui();

		int y = guiTop + 10;
		int gap = 21;

        tbName = new GuiNpcTextField(0, this, guiLeft + 85, y, 214, 20, npc.display.name);
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
        NetworkManager.netWrap.sendToServer(new MessageSaveTileEntity((int)npc.posX, (int)npc.posY, (int)npc.posZ, tbName.getText()));
    }
}
