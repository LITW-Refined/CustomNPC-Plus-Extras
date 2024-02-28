package de.pilz.customnpcsadvanced.network;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import de.pilz.customnpcsadvanced.CustomNpcPlusExtras;
import de.pilz.customnpcsadvanced.network.handlers.client.HandlerOpenGuiEditTileEntity;
import de.pilz.customnpcsadvanced.network.handlers.server.HandlerSaveTileEntity;
import de.pilz.customnpcsadvanced.network.messages.client.MessageSaveTileEntity;
import de.pilz.customnpcsadvanced.network.messages.server.MessageOpenGuiEditTileEntity;

public class NetworkManager {

    private static int curMsgId = 0;

    public static final SimpleNetworkWrapper netWrap = NetworkRegistry.INSTANCE
        .newSimpleChannel(CustomNpcPlusExtras.MODID);

    public static void init() {
        netWrap.registerMessage(HandlerSaveTileEntity.class, MessageSaveTileEntity.class, curMsgId++, Side.SERVER);
    }

    public static void initClient() {
        netWrap.registerMessage(
            HandlerOpenGuiEditTileEntity.class,
            MessageOpenGuiEditTileEntity.class,
            curMsgId++,
            Side.CLIENT);
    }
}
