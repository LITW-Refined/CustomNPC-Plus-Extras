package de.pilz.customnpcsadvanced.feature;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.WorldEvent;

import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import de.pilz.customnpcsadvanced.api.ITileEntityNpcManager;
import de.pilz.customnpcsadvanced.api.TileEntityNpc;
import de.pilz.customnpcsadvanced.api.data.TileEntityNpcData;
import de.pilz.customnpcsadvanced.api.data.TileEntityNpcDataContainer;
import de.pilz.customnpcsadvanced.client.gui.GuiEditTileEntityNpcData;
import de.pilz.customnpcsadvanced.network.NetworkManager;
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

    public static final TileEntityNpcManager Instance = new TileEntityNpcManager();

    public TileEntityNpcDataContainer dataContainer = null;
    public TileEntityNpcData editingNpc = null;

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
                && (!ConfigMain.OpsOnly || NoppesUtilServer.isOp(player))) {
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

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        if (dataContainer == null) {
            dataContainer = (TileEntityNpcDataContainer) event.world
                .loadItemData(TileEntityNpcDataContainer.class, TileEntityNpcDataContainer.FILENAME);

            if (dataContainer == null) {
                dataContainer = new TileEntityNpcDataContainer(TileEntityNpcDataContainer.FILENAME);
                event.world.setItemData(TileEntityNpcDataContainer.FILENAME, dataContainer);
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

    @Override
    public TileEntityNpcData getNpcData(String id) {
        for (TileEntityNpcData data : dataContainer.npcData) {
            if (data.equals(id)) {
                return data;
            }
        }
        return null;
    }

    @Override
    public TileEntityNpcData getNpcData(TileEntity te, boolean createIfNull) {
        for (TileEntityNpcData data : dataContainer.npcData) {
            if (data.equals(te)) {
                return data;
            }
        }

        if (createIfNull) {
            return new TileEntityNpcData(te);
        }

        return null;
    }

    @Override
    public void saveNpcData(TileEntityNpcData newData) {
        boolean found = false;

        for (TileEntityNpcData data : dataContainer.npcData) {
            if (data.equals(newData)) {
                data.clone(newData);
                found = true;
            }
        }

        if (!found) {
            dataContainer.npcData.add(newData);
        }

        dataContainer.markDirty();
    }

    @Override
    public void removeNpcData(TileEntity te) {
        for (TileEntityNpcData data : dataContainer.npcData) {
            if (data.equals(te)) {
                dataContainer.npcData.remove(data);
                dataContainer.markDirty();
                break;
            }
        }
    }

    @Override
    public void removeNpcData(TileEntityNpcData newData) {
        for (TileEntityNpcData data : dataContainer.npcData) {
            if (data.equals(newData)) {
                dataContainer.npcData.remove(data);
                dataContainer.markDirty();
                break;
            }
        }
    }
}
