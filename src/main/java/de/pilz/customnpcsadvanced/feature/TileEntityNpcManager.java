package de.pilz.customnpcsadvanced.feature;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import de.pilz.customnpcsadvanced.ClientProxy;
import de.pilz.customnpcsadvanced.CustomNpcPlusExtras;
import de.pilz.customnpcsadvanced.api.ITileEntityNpcManager;
import de.pilz.customnpcsadvanced.api.TileEntityNpc;
import de.pilz.customnpcsadvanced.api.data.TileEntityNpcData;
import de.pilz.customnpcsadvanced.client.gui.GuiEditTileEntityNpcData;
import de.pilz.customnpcsadvanced.network.NetworkManager;
import de.pilz.customnpcsadvanced.network.messages.client.MessageToggleEnableAdvWand;
import de.pilz.customnpcsadvanced.network.messages.server.MessageOpenGuiEditTileEntity;
import noppes.npcs.CustomItems;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.client.EntityUtil;
import noppes.npcs.config.ConfigMain;
import noppes.npcs.controllers.PlayerDataController;
import noppes.npcs.controllers.data.Dialog;
import noppes.npcs.controllers.data.DialogOption;
import noppes.npcs.controllers.data.PlayerData;
import noppes.npcs.entity.EntityDialogNpc;

public class TileEntityNpcManager implements ITileEntityNpcManager {

    public static final String FILENAME_CUSTOMNPCS = "customnpcs";
    public static final String FILENAME_ADDONS = "addons";
    public static final String FILENAME_NPCDATA = "npcdata";

    public static final TileEntityNpcManager Instance = new TileEntityNpcManager();

    protected File npcDataPath = null;
    protected HashMap<File, TileEntityNpcData> npcData = new HashMap<File, TileEntityNpcData>();
    public TileEntityNpcData editingNpc = null;
    public HashMap<String, Boolean> enableAdvWand = new HashMap<String, Boolean>();

    @SubscribeEvent
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.world.isRemote || event.action != PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK
            || event.isCanceled()) {
            return;
        }

        EntityPlayerMP player = (EntityPlayerMP) event.entityPlayer;
        TileEntity tile = (TileEntity) event.world.getTileEntity(event.x, event.y, event.z);
        ItemStack heldItem = player.getHeldItem();

        if (tile != null) {
            if (heldItem != null && heldItem.getItem() == CustomItems.wand
                && (!ConfigMain.OpsOnly || NoppesUtilServer.isOp(player))
                && enableAdvWand.getOrDefault(
                    player.getUniqueID()
                        .toString(),
                    false)) {
                TileEntityNpcData npcData = TileEntityNpcManager.Instance.getNpcData(tile, true);
                TileEntityNpc npc = new TileEntityNpc(event.world, npcData);
                PlayerData playerData = PlayerDataController.Instance.getPlayerData(player);
                playerData.editingNpc = npc;
                NetworkManager.netWrap.sendTo(new MessageOpenGuiEditTileEntity(npcData), player);
                event.useBlock = Result.DENY;
                event.useItem = Result.DENY;
            } else if (!player.isSneaking()
                && (heldItem == null /* || heldItem.getItemUseAction() == EnumAction.none */)) {
                    TileEntityNpcData npcData = TileEntityNpcManager.Instance.getNpcData(tile, false);

                    if (npcData != null) {
                        Dialog dialog = npcData.getDialog(player);

                        if (dialog != null) {
                            DialogOption option = new DialogOption();
                            option.dialogId = dialog.id;
                            option.title = dialog.title;

                            EntityDialogNpc npc = new EntityDialogNpc(event.world);
                            EntityUtil.Copy(player, npc);
                            npc.display.setName(npcData.getTitle());
                            npc.dialogs.put(0, option);

                            NoppesUtilServer.openDialog(player, npc, dialog, 0);
                            event.useBlock = Result.DENY;
                            event.useItem = Result.DENY;
                        }
                    }
                }
        }
    }

    public Object OpenEditorGui(EntityPlayer player, World world, int x, int y, int z) {
        TileEntity tile = (TileEntity) world.getTileEntity(x, y, z);
        TileEntityNpcData npcData = TileEntityNpcManager.Instance.editingNpc;

        if (tile != null && npcData != null && npcData.equals(tile)) {
            TileEntityNpc npc = new TileEntityNpc(tile.getWorldObj(), npcData);
            EntityUtil.Copy(player, npc);
            npc.display.setName(npcData.getTitle());
            npc.dialogs = npcData.getDialogOptions();
            if (PlayerDataController.Instance != null) {
                // Only do this on the server or in single player mode
                PlayerDataController.Instance.getPlayerData(player).editingNpc = npc;
            }
            return new GuiEditTileEntityNpcData(npc, npcData);
        }

        return null;
    }

    public void initNpcData() {
        // Clear current list
        npcData.clear();

        // Load json data files
        npcDataPath = new File(
            DimensionManager.getCurrentSaveRootDirectory() + File.separator
                + FILENAME_CUSTOMNPCS
                + File.separator
                + FILENAME_ADDONS
                + File.separator
                + CustomNpcPlusExtras.MODID
                + File.separator
                + FILENAME_NPCDATA);
        npcDataPath.mkdirs();
        File[] files = npcDataPath.listFiles();
        for (File file : files) {
            if (file.isFile() && file.getName()
                .toLowerCase()
                .endsWith(".json")) {
                npcData.put(file, null);
            }
        }
    }

    @SubscribeEvent
    public void onBlockBreak(BlockEvent.BreakEvent event) {
        if (event.block.hasTileEntity(event.blockMetadata)) {
            TileEntity te = event.world.getTileEntity(event.x, event.y, event.z);
            if (te != null) {
                removeNpcData(te);
            }
        }
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onKeyInputEvent(InputEvent.KeyInputEvent event) {
        if (ClientProxy.keyEnableAdvWand.getIsKeyPressed()
            && Keyboard.getEventKey() == ClientProxy.keyEnableAdvWand.getKeyCode()) {
            NetworkManager.netWrap.sendToServer(new MessageToggleEnableAdvWand());
        }
    }

    @Override
    public TileEntityNpcData[] getAllNpcData() {
        HashSet<TileEntityNpcData> list = new HashSet<TileEntityNpcData>();

        for (TileEntityNpcData data : npcData.values()) {
            if (data != null) {
                list.add(data);
            }
        }

        return list.toArray(new TileEntityNpcData[npcData.size()]);
    }

    @Override
    public TileEntityNpcData getNpcData(String id) {
        for (TileEntityNpcData data : npcData.values()) {
            if (data != null && data.equals(id)) {
                return data;
            }
        }
        return null;
    }

    @Override
    public TileEntityNpcData getNpcData(TileEntity te, boolean createIfNull) {
        // Query loaded npc data
        for (TileEntityNpcData data : npcData.values()) {
            if (data != null && data.equals(te)) {
                return data;
            }
        }

        // Load npc data from existing file
        for (File file : npcData.keySet()) {
            String fileName = getNpcFileName(te);
            if (fileName.equals(file.getName())) {
                TileEntityNpcData data = TileEntityNpcData.readFromFile(file);
                npcData.put(file, data);
                return data;
            }
        }

        // Create new npc data
        if (createIfNull) {
            return new TileEntityNpcData(te);
        }

        return null;
    }

    @Override
    public void saveNpcData(TileEntityNpcData newData) {
        boolean found = false;
        File output = null;

        for (HashMap.Entry<File, TileEntityNpcData> kvp : npcData.entrySet()) {
            if (kvp.getValue()
                .equals(newData)) {
                kvp.getValue()
                    .clone(newData);
                output = kvp.getKey();
                found = true;
            }
        }

        // Build new file path & add new data
        if (!found) {
            String fileName = getNpcFileName(newData);
            output = new File(npcDataPath, fileName);
            npcData.put(output, newData);
        }

        // Save file
        newData.saveToFile(output);
    }

    @Override
    public void removeNpcData(TileEntity te) {
        for (HashMap.Entry<File, TileEntityNpcData> kvp : npcData.entrySet()) {
            File file = kvp.getKey();
            TileEntityNpcData data = kvp.getValue();

            if (data != null && data.equals(te)) {
                npcData.remove(file);
                file.delete();
                break;
            }
        }
    }

    @Override
    public void removeNpcData(TileEntityNpcData newData) {
        for (HashMap.Entry<File, TileEntityNpcData> kvp : npcData.entrySet()) {
            File file = kvp.getKey();
            TileEntityNpcData data = kvp.getValue();

            if (data != null && data.equals(newData)) {
                npcData.remove(file);
                file.delete();
                break;
            }
        }
    }

    public static String getNpcFileName(TileEntity entity) {
        return getNpcFileName(entity.xCoord, entity.yCoord, entity.zCoord, entity.getWorldObj().provider.dimensionId);
    }

    public static String getNpcFileName(TileEntityNpcData entity) {
        return getNpcFileName(entity.posX, entity.posY, entity.posZ, entity.dimId);
    }

    public static String getNpcFileName(int x, int y, int z, int dimensionId) {
        return "npc.dim" + dimensionId + "." + x + "." + y + "." + z + ".json";
    }
}
