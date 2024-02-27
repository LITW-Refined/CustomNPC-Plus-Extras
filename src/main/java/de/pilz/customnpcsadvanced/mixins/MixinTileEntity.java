package de.pilz.customnpcsadvanced.mixins;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import de.pilz.customnpcsadvanced.data.TileEntityNpcData;
import de.pilz.customnpcsadvanced.te.ITileEntityNpc;

@Mixin(TileEntity.class)
public abstract class MixinTileEntity implements ITileEntityNpc {

    private TileEntityNpcData npcData = null;

    @Unique
    public TileEntityNpcData getNpcData() {
        return npcData;
    }

    @Unique
    public TileEntityNpcData getNpcData(boolean createIfNull) {
        if (npcData == null && createIfNull) {
            npcData = new TileEntityNpcData();
        }
        return getNpcData();
    }

    @Inject(method = "readFromNBT(Lnet/minecraft/nbt/NBTTagCompound;)V", at = @At("RETURN"), cancellable = true)
    public void cnpcpextras$readFromNBT(NBTTagCompound compound, CallbackInfo callback) {
        npcData = TileEntityNpcData.loadFromNBT(compound, false);
    }

    @Inject(method = "writeToNBT(Lnet/minecraft/nbt/NBTTagCompound;)V", at = @At("RETURN"), cancellable = true)
    public void cnpcpextras$writeToNBT(NBTTagCompound compound, CallbackInfo callback) {
        TileEntityNpcData.savetoNBT(compound, npcData);
    }

    @Inject(
        method = "onDataPacket(Lnet/minecraft/network/NetworkManager;Lnet/minecraft/network/play/server/S35PacketUpdateTileEntity;)V",
        at = @At("RETURN"),
        cancellable = true,
        remap = false)
    public void cnpcpextras$onDataPacket(NetworkManager compound, S35PacketUpdateTileEntity packet,
        CallbackInfo callback) {
        npcData = TileEntityNpcData.loadFromNBT(packet.func_148857_g(), false);
    }

    @Inject(
        method = "getDescriptionPacket()Lnet/minecraft/network/Packet;",
        at = @At("RETURN"),
        cancellable = true,
        remap = false)
    public void cnpcpextras$getDescriptionPacket(CallbackInfoReturnable<Packet> callback) {
        Packet packetRaw = callback.getReturnValue();

        if (packetRaw instanceof S35PacketUpdateTileEntity) {
            S35PacketUpdateTileEntity packet = (S35PacketUpdateTileEntity) packetRaw;
            NBTTagCompound nbt = packet.func_148857_g();

            if (nbt != null) {
                TileEntityNpcData.savetoNBT(nbt, npcData);
            }
        }
    }
}
