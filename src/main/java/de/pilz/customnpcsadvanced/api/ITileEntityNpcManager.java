package de.pilz.customnpcsadvanced.api;

import net.minecraft.tileentity.TileEntity;

import de.pilz.customnpcsadvanced.api.data.TileEntityNpcData;
import de.pilz.customnpcsadvanced.feature.TileEntityNpcManager;

public interface ITileEntityNpcManager {

    public TileEntityNpcData getNpcData(TileEntity te, boolean createIfNull);

    public TileEntityNpcData getNpcData(String id);

    public TileEntityNpcData[] getAllNpcData();

    public void saveNpcData(TileEntityNpcData newData);

    public void removeNpcData(TileEntity te);

    public void removeNpcData(TileEntityNpcData newData);

    public static ITileEntityNpcManager getInstance() {
        return TileEntityNpcManager.Instance;
    }
}
