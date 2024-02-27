package de.pilz.customnpcsadvanced.te;

import de.pilz.customnpcsadvanced.data.TileEntityNpcData;

public interface ITileEntityNpc {

    public TileEntityNpcData getNpcData();

    public TileEntityNpcData getNpcData(boolean createIfNull);
}
