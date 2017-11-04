package com.mygdx.game.libgdx.Scene;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

/**
 * Created by YAN on 23/02/2017.
 */

public class PlayerSaveSerializer extends Serializer<PlayerSave> {


    @Override
    public void write(Kryo kryo, Output output, PlayerSave object) {

    }

    @Override
    public PlayerSave read(Kryo kryo, Input input, Class<PlayerSave> type) {
        return new PlayerSave(input.readByte(), true);
    }

}
