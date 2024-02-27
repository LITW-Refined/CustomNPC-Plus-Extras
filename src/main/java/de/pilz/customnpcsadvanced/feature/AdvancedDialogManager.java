package de.pilz.customnpcsadvanced.feature;

import de.pilz.customnpcsadvanced.network.NetworkManager;
import de.pilz.customnpcsadvanced.network.messages.MessageOpenGuiEditTileEntityRequest;
import de.pilz.customnpcsadvanced.te.IMixinTileEntityFancySign;
import jds.bibliocraft.tileentities.TileEntityFancySign;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import noppes.npcs.CustomItems;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.client.EntityUtil;
import noppes.npcs.controllers.data.Dialog;
import noppes.npcs.controllers.data.DialogOption;
import noppes.npcs.entity.EntityDialogNpc;

public class AdvancedDialogManager {

    public static final AdvancedDialogManager Instance = new AdvancedDialogManager();

    public boolean checkBlockActivate(World world, int i, int j, int k, EntityPlayer player, int face, float hitX, float hitY, float hitZ) {
        TileEntityFancySign tile = (TileEntityFancySign)world.getTileEntity(i, j, k);
        IMixinTileEntityFancySign tilem = (IMixinTileEntityFancySign)tile;
        String playername = player.getDisplayName();
        boolean canEdit = tile != null && (!tile.isLocked() || playername.contains(tile.getLockee()));
        ItemStack heldItem = player.getHeldItem();

        if (tile != null) {
            boolean allowEdit = canEdit;

            if (allowEdit && heldItem != null && heldItem.getItem() == CustomItems.wand) {
                NetworkManager.netWrap.sendToServer(new MessageOpenGuiEditTileEntityRequest(i, j, k));
                return true;
            }
            else if (!player.isSneaking()) {
                Dialog dialog = tilem.getDialog(player);

                if (dialog != null) {
                    DialogOption option = new DialogOption();
                    option.dialogId = dialog.id;
                    option.title = dialog.title;

                    EntityDialogNpc npc = new EntityDialogNpc(world);
                    EntityUtil.Copy(player, npc);
                    npc.display.setName(tilem.getNpcName());
                    npc.dialogs.put(0, option);

                    NoppesUtilServer.openDialog(player, npc, dialog, 0);
                    return true;
                }
            }
        }

        return false;
    }
}
