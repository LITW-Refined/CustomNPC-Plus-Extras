package de.pilz.customnpcsadvanced;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import de.pilz.customnpcsadvanced.client.gui.EnumGuiType;
import de.pilz.customnpcsadvanced.feature.TileEntityNpcManager;
import de.pilz.customnpcsadvanced.network.NetworkManager;

public class ClientProxy extends CommonProxy {

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if (ID <= EnumGuiType.values().length) {
            EnumGuiType gui = EnumGuiType.values()[ID];

            switch (gui) {
                case EditTileEntityNpc: {
                    return TileEntityNpcManager.Instance.OpenEditorGui(player, world, x, y, z);
                }
            }
        }

        return null;
    }

    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);
        NetworkManager.initClient();
    }
}
