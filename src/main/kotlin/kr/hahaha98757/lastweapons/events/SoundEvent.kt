package kr.hahaha98757.lastweapons.events

import net.minecraft.client.audio.ISound
import net.minecraftforge.fml.common.eventhandler.Event

class SoundEvent(private val sound: ISound): Event() {
    fun getSoundName() = try {
            sound.soundLocation.toString().split(":")[1]
        } catch (e: Exception) {
            ""
        }
}