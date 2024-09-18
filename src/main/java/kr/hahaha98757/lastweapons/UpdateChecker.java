package kr.hahaha98757.lastweapons;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import kr.hahaha98757.lastweapons.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.commons.io.IOUtils;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.util.Objects;

@SuppressWarnings("CallToPrintStackTrace")
public class UpdateChecker {
    private static final String URL_TEXT = "§9§nClick here to download.";
    private static final String URL = "https://github.com/hahaha98757/last-weapons/releases";
    private static final String SHOW_URL_TEXT = "Open download URL.";
    private static final String VERSION_URL = "https://raw.githubusercontent.com/hahaha98757/last-weapons/master/versions.json";

    private static SSLContext ctx;
    static {
        try {
            KeyStore myKeyStore = KeyStore.getInstance("JKS");

            myKeyStore.load(UpdateChecker.class.getResourceAsStream("/mykeystore.jks"), "changeit".toCharArray());
            KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            kmf.init(myKeyStore, null);
            tmf.init(myKeyStore);
            ctx = SSLContext.getInstance("TLS");
            ctx.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
        } catch (Exception e) {
            e.printStackTrace();
            ctx = null;
        }
    }

    @SubscribeEvent
    public void playerJoinWorld(EntityJoinWorldEvent event) {
        if (event.entity != Minecraft.getMinecraft().thePlayer) return;

        MinecraftForge.EVENT_BUS.unregister(this);

        new Thread(() -> {
            try {
                URL url = new URL(VERSION_URL);
                HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
                if (connection != null && ctx != null) {
                    connection.setSSLSocketFactory(ctx.getSocketFactory());
                }
                Objects.requireNonNull(connection).setRequestMethod("GET");
                connection.setConnectTimeout(60000);
                connection.setReadTimeout(60000);

                InputStream inputStream = connection.getInputStream();
                String jsonResponse = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
                inputStream.close();

                JsonObject json = new JsonParser().parse(jsonResponse).getAsJsonObject();
                String latestVersion = json.get("version").getAsString();
                String changelogUrl = json.get("log").getAsString();

                Minecraft.getMinecraft().addScheduledTask(() -> {
                    if (!latestVersion.equals(LastWeapons.VERSION))
                        Utils.addChatWithURL(Utils.LINE + "\nA new version is available. ", URL_TEXT, URL, SHOW_URL_TEXT, "\n§rCurrent version: " + LastWeapons.VERSION + "\nLatest version: " + latestVersion + "\n" + Utils.LINE);
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}