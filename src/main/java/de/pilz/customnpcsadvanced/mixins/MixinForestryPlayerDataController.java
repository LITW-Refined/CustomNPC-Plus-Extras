package de.pilz.customnpcsadvanced.mixins;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import de.pilz.customnpcsadvanced.configuration.ConfigMail;
import de.pilz.customnpcsadvanced.feature.ForestryMailFeature;
import forestry.api.mail.EnumAddressee;
import forestry.api.mail.ILetter;
import forestry.api.mail.IMailAddress;
import forestry.api.mail.PostManager;
import noppes.npcs.controllers.PlayerDataController;
import noppes.npcs.controllers.data.PlayerMail;

@Mixin(PlayerDataController.class)
public class MixinForestryPlayerDataController {

    @Inject(
        method = "addPlayerMessage(Ljava/lang/String;Lnoppes/npcs/controllers/data/PlayerMail;)V",
        at = @At("HEAD"),
        cancellable = true,
        remap = false)
    public void customnpcsplusextra$addPlayerMessage(String username, PlayerMail mail, CallbackInfo callback) {
        if (ConfigMail.sendMailViaForestry) {
            IMailAddress sender = ForestryMailFeature.getRecipient(mail.sender);
            IMailAddress recipient = ForestryMailFeature.getRecipient(username);
            ILetter letter = PostManager.postRegistry.createLetter(sender, recipient);

            // Create signed book for our letter message as it's too long for a Forestry Mail
            ItemStack book = new ItemStack(Items.written_book);
            book.setTagInfo("author", new NBTTagString(mail.sender));
            book.setTagInfo("title", new NBTTagString(mail.subject));

            // Write book pages
            NBTTagList bookPages = new NBTTagList();
            for (int i = 0; i < mail.getPageCount(); i++) {
                bookPages.appendTag(new NBTTagString(mail.getPageText()[i]));
            }
            book.setTagInfo("pages", bookPages);

            // Attach book to mail attachments
            letter.addAttachment(book);

            // Set letter text
            String letterText = mail.getSubject();
            letterText += "\n----------";
            letterText += "\n" + StatCollector.translateToLocal("letter.seeattachedletter");
            letter.setText(letterText);

            // Attachments
            for (int i = 0; i < mail.items.length; i++) {
                ItemStack item = mail.items[i];
                if (item != null) {
                    letter.addAttachment(item);
                }
            }

            // Send messages
            ItemStack letterStack = PostManager.postRegistry.createLetterStack(letter);
            World world = MinecraftServer.getServer()
                .getEntityWorld();
            PostManager.postRegistry.getCarrier(EnumAddressee.PLAYER)
                .deliverLetter(world, PostManager.postRegistry.getPostOffice(world), recipient, letterStack, false);

            // Don't send original message
            if (!ConfigMail.sendMailViaCusotmNPCs) {
                callback.cancel();
            }
        }
    }
}
