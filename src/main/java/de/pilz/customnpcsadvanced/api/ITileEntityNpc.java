package de.pilz.customnpcsadvanced.api;

import de.pilz.customnpcsadvanced.api.data.TileEntityNpcData;

public interface ITileEntityNpc {

    public TileEntityNpcData getNpcData();

    public TileEntityNpcData getNpcData(boolean createIfNull);
}
