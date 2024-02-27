package de.pilz.customnpcsadvanced.network;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import de.pilz.customnpcsadvanced.CustomNpcPlusExtras;
import de.pilz.customnpcsadvanced.network.handlers.HandlerOpenGuiEditTileEntityOk;
import de.pilz.customnpcsadvanced.network.handlers.HandlerOpenGuiEditTileEntityRequest;
import de.pilz.customnpcsadvanced.network.handlers.HandlerSaveTileEntity;
import de.pilz.customnpcsadvanced.network.messages.MessageOpenGuiEditTileEntityOk;
import de.pilz.customnpcsadvanced.network.messages.MessageOpenGuiEditTileEntityRequest;
import de.pilz.customnpcsadvanced.network.messages.MessageSaveTileEntity;

public class NetworkManager {

    public static final SimpleNetworkWrapper netWrap = NetworkRegistry.INSTANCE
        .newSimpleChannel(CustomNpcPlusExtras.MODID);

    public static void init() {
        netWrap.registerMessage(
            HandlerOpenGuiEditTileEntityRequest.class,
            MessageOpenGuiEditTileEntityRequest.class,
            0,
            Side.SERVER);
        netWrap.registerMessage(
            HandlerOpenGuiEditTileEntityOk.class,
            MessageOpenGuiEditTileEntityOk.class,
            1,
            Side.CLIENT);
        netWrap.registerMessage(HandlerSaveTileEntity.class, MessageSaveTileEntity.class, 2, Side.SERVER);
    }
}
