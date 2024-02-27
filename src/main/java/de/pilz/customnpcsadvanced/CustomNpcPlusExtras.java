package de.pilz.customnpcsadvanced;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import de.pilz.customnpcsadvanced.network.NetworkManager;

@Mod(
    modid = CustomNpcPlusExtras.MODID,
    version = Tags.VERSION,
    name = "CustomNPC+ Advancements",
    acceptedMinecraftVersions = "[1.7.10]")
public class CustomNpcPlusExtras {

    public static final String MODID = "customnpcsadvanced";
    public static final Logger LOG = LogManager.getLogger(MODID);

    @Instance
    public static CustomNpcPlusExtras Instance;

    @SidedProxy(
        clientSide = "de.pilz.customnpcsadvanced.ClientProxy",
        serverSide = "de.pilz.customnpcsadvanced.CommonProxy")
    public static CommonProxy proxy;

    @Mod.EventHandler
    // preInit "Run before anything else. Read your config, create blocks, items, etc, and register them with the
    // GameRegistry." (Remove if not needed)
    public void preInit(FMLPreInitializationEvent event) {
        proxy.preInit(event);
    }

    @Mod.EventHandler
    // load "Do your mod setup. Build whatever data structures you care about. Register recipes." (Remove if not needed)
    public void init(FMLInitializationEvent event) {
        NetworkManager.init();
        NetworkRegistry.INSTANCE.registerGuiHandler(this, proxy);
        proxy.init(event);
    }

    @Mod.EventHandler
    // postInit "Handle interaction with other mods, complete your setup based on this." (Remove if not needed)
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }

    @Mod.EventHandler
    // register server commands in this event handler (Remove if not needed)
    public void serverStarting(FMLServerStartingEvent event) {
        proxy.serverStarting(event);
    }
}
