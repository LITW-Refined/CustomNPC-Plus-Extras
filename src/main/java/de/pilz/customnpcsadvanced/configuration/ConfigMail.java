package de.pilz.customnpcsadvanced.configuration;

import com.gtnewhorizon.gtnhlib.config.Config;

import de.pilz.customnpcsadvanced.CustomNpcPlusExtras;

@Config(modid = CustomNpcPlusExtras.MODID, category = "mail")
public class ConfigMail {

    @Config.Comment("If enabled, Mails from CustomNPCs will be sent via Forestry.\nUse this if you prefer Forestry mail system on your server and you wont use two mail systems at the same time.")
    @Config.DefaultBoolean(false)
    public static boolean sendMailViaForestry;

    @Config.Comment("When enabled, Mails will also be sent via CustomNPCs. Only takes affect if `sendMailViaForestry` is enabled.")
    @Config.DefaultBoolean(false)
    public static boolean sendMailViaCusotmNPCs;
}
