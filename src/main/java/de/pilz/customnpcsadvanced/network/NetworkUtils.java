package de.pilz.customnpcsadvanced.network;

import java.io.IOException;

import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTSizeTracker;
import net.minecraft.nbt.NBTTagCompound;

import com.google.common.base.Charsets;

import io.netty.buffer.ByteBuf;

public class NetworkUtils {

    public static void writeNBT(ByteBuf buffer, NBTTagCompound compound) throws IOException {
        byte[] bytes = CompressedStreamTools.compress(compound);
        buffer.writeShort((short) bytes.length);
        buffer.writeBytes(bytes);
    }

    public static NBTTagCompound readNBT(ByteBuf buffer) throws IOException {
        byte[] bytes = new byte[buffer.readShort()];
        buffer.readBytes(bytes);
        return CompressedStreamTools.func_152457_a(bytes, new NBTSizeTracker(2097152L));
    }

    public static void writeString(ByteBuf buffer, String s) {
        byte[] bytes = s.getBytes(Charsets.UTF_8);
        buffer.writeShort((short) bytes.length);
        buffer.writeBytes(bytes);
    }

    public static String readString(ByteBuf buffer) {
        try {
            byte[] bytes = new byte[buffer.readShort()];
            buffer.readBytes(bytes);
            return new String(bytes, Charsets.UTF_8);
        } catch (IndexOutOfBoundsException ex) {
            return null;
        }
    }
}
