package de.pilz.customnpcsadvanced;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import de.pilz.customnpcsadvanced.client.gui.EnumGuiType;
import de.pilz.customnpcsadvanced.feature.TileEntityNpcManager;

public class ClientProxy extends CommonProxy {

    public static final KeyBinding keyEnableAdvWand = new KeyBinding(
        StatCollector.translateToLocal("key.enableAdvWand"),
        Keyboard.KEY_F,
        "key.categories.gameplay");

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
        ClientRegistry.registerKeyBinding(keyEnableAdvWand);
    }

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
    public EntityPlayer getPlayer(MessageContext ctx) {
        return Minecraft.getMinecraft().thePlayer;
    }

    @Override
    public World getWorld(MessageContext ctx) {
        return FMLClientHandler.instance()
            .getWorldClient();
    }
}
