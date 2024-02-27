package de.pilz.customnpcsadvanced.feature;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import de.pilz.customnpcsadvanced.client.gui.GuiEditTileEntityNpcData;
import de.pilz.customnpcsadvanced.data.TileEntityNpcData;
import de.pilz.customnpcsadvanced.network.NetworkManager;
import de.pilz.customnpcsadvanced.network.messages.MessageOpenGuiEditTileEntityRequest;
import de.pilz.customnpcsadvanced.te.ITileEntityNpc;
import noppes.npcs.CustomItems;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.client.EntityUtil;
import noppes.npcs.controllers.data.Dialog;
import noppes.npcs.controllers.data.DialogOption;
import noppes.npcs.entity.EntityDialogNpc;

public class AdvancedTileEntityManager {

    public static final AdvancedTileEntityManager Instance = new AdvancedTileEntityManager();

    @SubscribeEvent
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.action != PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK || event.world.isRemote) {
            return;
        }

        TileEntity tile = (TileEntity) event.world.getTileEntity(event.x, event.y, event.z);
        ITileEntityNpc tilem = (ITileEntityNpc) tile;
        ItemStack heldItem = event.entityPlayer.getHeldItem();

        if (tile != null) {
            if (heldItem != null && heldItem.getItem() == CustomItems.wand) {
                NetworkManager.netWrap.sendToServer(new MessageOpenGuiEditTileEntityRequest(event.x, event.y, event.z));
                event.useBlock = Result.DENY;
                event.useItem = Result.DENY;
            } else if (!event.entityPlayer.isSneaking()) {
                TileEntityNpcData npcData = tilem.getNpcData();

                if (npcData != null) {
                    Dialog dialog = npcData.getDialog(event.entityPlayer);

                    if (dialog != null) {
                        DialogOption option = new DialogOption();
                        option.dialogId = dialog.id;
                        option.title = dialog.title;

                        EntityDialogNpc npc = new EntityDialogNpc(event.world);
                        EntityUtil.Copy(event.entityPlayer, npc);
                        npc.display.setName(npcData.getTitle());
                        npc.dialogs.put(0, option);

                        NoppesUtilServer.openDialog(event.entityPlayer, npc, dialog, 0);
                        event.useBlock = Result.DENY;
                        event.useItem = Result.DENY;
                    }
                }
            }
        }
    }

    public Object OpenEditorGui(EntityPlayer player, World world, int x, int y, int z) {
        TileEntity tile = (TileEntity) world.getTileEntity(x, y, z);
        ITileEntityNpc tilem = (ITileEntityNpc) tile;
        TileEntityNpcData npcData = tilem.getNpcData(true);
        EntityDialogNpc npc = new EntityDialogNpc(tile.getWorldObj());
        EntityUtil.Copy(player, npc);
        npc.display.setName(npcData.getTitle());
        npc.dialogs = npcData.getDialogOptions();
        return new GuiEditTileEntityNpcData(npc, x, y, z);
    }
}
