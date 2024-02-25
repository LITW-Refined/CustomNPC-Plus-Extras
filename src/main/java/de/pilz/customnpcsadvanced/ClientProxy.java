package de.pilz.customnpcsadvanced;

import de.pilz.customnpcsadvanced.client.gui.EnumGuiType;
import de.pilz.customnpcsadvanced.client.gui.GuiEditFancySignNpc;
import de.pilz.customnpcsadvanced.te.IMixinTileEntityFancySign;
import jds.bibliocraft.tileentities.TileEntityFancySign;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import noppes.npcs.client.EntityUtil;
import noppes.npcs.entity.EntityDialogNpc;

public class ClientProxy extends CommonProxy {

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if (ID <= EnumGuiType.values().length) {
            EnumGuiType gui = EnumGuiType.values()[ID];

            switch (gui) {
                case EditFancySignNpc: {
                    TileEntityFancySign tile = (TileEntityFancySign)world.getTileEntity(x, y, z);
                    IMixinTileEntityFancySign tilem = (IMixinTileEntityFancySign)tile;
                    EntityDialogNpc npc = new EntityDialogNpc(tile.getWorldObj());
                    EntityUtil.Copy(player, npc);
                    npc.display.setName(tilem.getNpcName());
                    npc.dialogs = tilem.getDialogOptions();
                    return new GuiEditFancySignNpc(npc, x, y, z);
                }
            }
        }

        return null;
    }
}
