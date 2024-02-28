package de.pilz.customnpcsadvanced.api.data;

import java.util.HashSet;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.WorldSavedData;

public class TileEntityNpcDataContainer extends WorldSavedData {

    public static final Integer FILEVERSION = 1;
    public static final String NBT_KEY_FILEVERSION = "Version";
    public static final String NBT_KEY_NPC_DATA = "NpcData";
    public static final String FILENAME = "TileEntityNpcData";

    public HashSet<TileEntityNpcData> npcData = new HashSet<TileEntityNpcData>();

    public TileEntityNpcDataContainer(String p_i2141_1_) {
        super(p_i2141_1_);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbtFile) {
        npcData.clear();

        NBTTagList npcDataList = nbtFile.getTagList(NBT_KEY_NPC_DATA, 10);
        for (int i = 0; i < npcDataList.tagCount(); i++) {
            NBTTagCompound nbtData = npcDataList.getCompoundTagAt(i);
            TileEntityNpcData data = new TileEntityNpcData(nbtData);
            npcData.add(data);
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound nbtFile) {
        nbtFile.setInteger(NBT_KEY_FILEVERSION, FILEVERSION);

        NBTTagList npcDataList = new NBTTagList();
        for (TileEntityNpcData data : npcData) {
            NBTTagCompound nbtData = data.getAsNBT();
            if (!nbtData.hasNoTags()) {
                npcDataList.appendTag(nbtData);
            }
        }
        nbtFile.setTag(NBT_KEY_NPC_DATA, npcDataList);
    }
}
