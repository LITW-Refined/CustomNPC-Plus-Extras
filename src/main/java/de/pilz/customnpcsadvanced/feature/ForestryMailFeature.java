package de.pilz.customnpcsadvanced.feature;

import java.util.UUID;

import net.minecraft.server.MinecraftServer;

import com.google.common.base.Charsets;
import com.mojang.authlib.GameProfile;

import forestry.api.mail.IMailAddress;
import forestry.api.mail.PostManager;

public class ForestryMailFeature {

    public static IMailAddress getRecipient(String recipientName) {
        GameProfile gameProfile = MinecraftServer.getServer()
            .func_152358_ax()
            .func_152655_a(recipientName);
        if (gameProfile == null) {
            gameProfile = new GameProfile(
                UUID.nameUUIDFromBytes(("OfflinePlayer:" + recipientName).getBytes(Charsets.UTF_8)),
                recipientName);
        }
        return PostManager.postRegistry.getMailAddress(gameProfile);
    }
}
