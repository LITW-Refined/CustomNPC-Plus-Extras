package de.pilz.customnpcsadvanced.client.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

import de.pilz.customnpcsadvanced.api.TileEntityNpc;
import de.pilz.customnpcsadvanced.api.data.TileEntityNpcData;
import de.pilz.customnpcsadvanced.client.gui.roles.GuiEditTileEntityNpcTrader;
import de.pilz.customnpcsadvanced.client.gui.roles.GuiEditTileEntityNpcTransporter;
import de.pilz.customnpcsadvanced.network.NetworkManager;
import de.pilz.customnpcsadvanced.network.messages.client.MessageSaveTileEntity;
import noppes.npcs.client.NoppesUtil;
import noppes.npcs.client.gui.util.GuiNPCInterface2;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.GuiNpcTextField;
import noppes.npcs.client.gui.util.IGuiData;
import noppes.npcs.constants.EnumRoleType;
import noppes.npcs.containers.ContainerNPCTraderSetup;

public class GuiEditTileEntityNpcData extends GuiNPCInterface2 implements IGuiData {

    public final TileEntityNpc fakeNpc;
    public final TileEntityNpcData npcData;
    protected GuiNpcTextField tbName;
    protected EntityPlayer player;

    public GuiEditTileEntityNpcData(TileEntityNpc npc, TileEntityNpcData npcData, EntityPlayer player) {
        super(null);
        fakeNpc = npc;
        this.npcData = npcData;
        this.player = player;
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
        
    	this.addButton(new GuiNpcButton(1, guiLeft + 85 + 160, y += gap, 52, 20, "selectServer.edit"));
    	this.addButton(new GuiNpcButton(2, guiLeft + 85, y, 155, 20, new String[] { "role.none", "role.trader", "role.transporter" }, translateOrdinalToId(fakeNpc.advanced.role.ordinal())));
    	getButton(1).setEnabled(fakeNpc.advanced.role != EnumRoleType.None && fakeNpc.advanced.role != EnumRoleType.Postman);
    }

    @Override
    protected void actionPerformed(GuiButton guibutton) {
        GuiNpcButton button = (GuiNpcButton) guibutton;

        if (button.id == 0) {
            // Edit dialogs
            save();
            NoppesUtil.openGUI(player, new GuiEditTileEntityNpcDialogOptions(fakeNpc, npcData, this));
        }
        else if (button.id == 1) {
            // Edit role interface
            save();
            if (fakeNpc.advanced.role == EnumRoleType.Trader) {
                NoppesUtil.openGUI(player, new GuiEditTileEntityNpcTrader(fakeNpc, npcData, new ContainerNPCTraderSetup(fakeNpc, player)));
            } else if (fakeNpc.advanced.role == EnumRoleType.Transporter) {
                NoppesUtil.openGUI(player, new GuiEditTileEntityNpcTransporter(fakeNpc, npcData));
            }
        }
        else if (button.id == 2) {
            // Edit role type
            fakeNpc.advanced.setRole(npcData.setRole(EnumRoleType.values()[translateIdToOrdinal(button.getValue())]).ordinal());
            getButton(1).setEnabled(fakeNpc.advanced.role != EnumRoleType.None && fakeNpc.advanced.role != EnumRoleType.Postman);
            save(); // Ensure the server knowns about the new role for the fake npc
        }
    }

    @Override
    public void setGuiData(NBTTagCompound compound) {}

    @Override
    public void save() {
        npcData.setTitle(tbName.getText());
        NetworkManager.netWrap.sendToServer(new MessageSaveTileEntity(npcData));
    }

    public static int translateIdToOrdinal(int id) {
        switch(id) {
            // Traider
            case 1: return 1;
            // Transporter
            case 2: return 4;
        }
        return 0;
    }

    public static int translateOrdinalToId(int ordinal) {
        switch(ordinal) {
            // Traider
            case 1: return 1;
            // Transporter
            case 4: return 2;
        }
        return 0;
    }
}
