package de.pilz.customnpcsadvanced;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import de.pilz.customnpcsadvanced.client.gui.EnumGuiType;
import de.pilz.customnpcsadvanced.feature.AdvancedTileEntityManager;

public class ClientProxy extends CommonProxy {

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if (ID <= EnumGuiType.values().length) {
            EnumGuiType gui = EnumGuiType.values()[ID];

            switch (gui) {
                case EditTileEntityNpc: {
                    return AdvancedTileEntityManager.Instance.OpenEditorGui(player, world, x, y, z);
                }
            }
        }

        return null;
    }
}
