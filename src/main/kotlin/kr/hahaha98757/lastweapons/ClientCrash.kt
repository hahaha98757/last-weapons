package kr.hahaha98757.lastweapons

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent

class ClientCrash {
    companion object {
        var hasZombiesAddon = false
        var update = false
    }
    private var delay = 0

    @SubscribeEvent
    fun onClientTick(event: TickEvent.ClientTickEvent) {
        if (event.phase != TickEvent.Phase.END) return

        delay++

        when (delay) {
            100 -> addChat("§cEnds after 5 seconds...")
            120 -> addChat("§cEnds after 4 seconds...")
            140 -> addChat("§c§lEnds after 3 seconds...")
            160 -> addChat("§c§lEnds after 2 seconds...")
            180 -> addChat("§c§lEnds after 1 seconds...")
            200 -> {
                addChat("§c§lThe game ends...")
                if (hasZombiesAddon)
                    throw RuntimeException("You are using Zombies Addon. Remove Last Weapons mod.")
                if (update)
                    throw RuntimeException("Update Last Weapons. URL: https://github.com/hahaha98757/last-weapons/releases")
                throw RuntimeException("Unknown error.")
            }
        }
    }
}