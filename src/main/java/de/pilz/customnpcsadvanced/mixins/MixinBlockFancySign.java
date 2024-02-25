package de.pilz.customnpcsadvanced.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import de.pilz.customnpcsadvanced.feature.AdvancedDialogManager;
import jds.bibliocraft.blocks.BlockFancySign;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

@Mixin(BlockFancySign.class)
public abstract class MixinBlockFancySign extends BlockContainer {

    protected MixinBlockFancySign(Material p_i45386_1_) {
        super(p_i45386_1_);
    }

    @Inject(
        method = "onBlockActivated(Lnet/minecraft/world/World;IIILnet/minecraft/entity/player/EntityPlayer;IFFF)Z",
        at = @At("RETURN"),
        cancellable = true)
    public void cnpcpextras$onBlockActivated(World world, int i, int j, int k, EntityPlayer player, int face, float hitX, float hitY, float hitZ, CallbackInfoReturnable<Boolean> callback) {
        if (callback.getReturnValueZ() || world.isRemote) {
            return;
        }

        if (AdvancedDialogManager.Instance.checkBlockActivate(world, i, j, k, player, face, hitX, hitY, hitZ)) {
            callback.setReturnValue(true);
        }
    }
}
