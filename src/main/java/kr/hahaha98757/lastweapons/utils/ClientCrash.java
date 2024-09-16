package kr.hahaha98757.lastweapons.utils;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class ClientCrash {
    public static boolean haveZombiesAddon = false;

    private short delay = 0;

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) throws GameEnd {
        if (event.phase != TickEvent.Phase.END) return;

        delay++;

        switch (delay) {
            case 100:
                Utils.addChat("§cEnds after 5 seconds...");
                break;
            case 120:
                Utils.addChat("§cEnds after 4 seconds...");
                break;
            case 140:
                Utils.addChat("§c§lEnds after 3 seconds...");
                break;
            case 160:
                Utils.addChat("§c§lEnds after 2 seconds...");
                break;
            case 180:
                Utils.addChat("§c§lEnds after 1 second...");
                break;
            case 200:
                Utils.addChat("§c§lThe game ends...");

                if (haveZombiesAddon)
                    throw new GameEnd("You are using Zombies Addon. Remove Last Weapons mod.");

                throw new GameEnd("Unknown Error.");
            default:
                break;
        }
    }

    public static void haveZombiesAddon() {
        haveZombiesAddon = true;
    }
}