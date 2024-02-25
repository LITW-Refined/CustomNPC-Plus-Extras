package de.pilz.customnpcsadvanced;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.gtnewhorizon.gtnhmixins.ILateMixinLoader;
import com.gtnewhorizon.gtnhmixins.LateMixin;

@LateMixin
public class LateMixinLoader implements ILateMixinLoader {
    
    @Override
    public String getMixinConfig() {
        return "mixins.customnpcsadvanced.late.json";
    }

    @Override
    public List<String> getMixins(Set<String> loadedMods) {
        List<String> list = new ArrayList<String>();

        // BiblioCraft
        // if (loadedMods.contains("bibliocraft")) {
            list.add("MixinBlockFancySign");
            list.add("MixinTileEntityFancySign");
        // }

        return list;
    }
}