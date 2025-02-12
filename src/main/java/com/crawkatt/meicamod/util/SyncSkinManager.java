package com.crawkatt.meicamod.util;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.util.DefaultSkinHelper;
import net.minecraft.util.Identifier;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class SyncSkinManager {
    // Almacena las skins en caché para evitar alcanzar el límite de velocidad de los servidores de sesión de skins.
    // Mantener los valores durante más tiempo, para que se carguen rápido si se cargan muchos TEs con el mismo jugador, o cuando se cargan otros chunks con el mismo jugador
    // Prioridad de carga de skins: Cache (la más rápida), NetworkPlayer (sólo disponible cuando el jugador está online y en la misma dimensión que el shell, rápida), SessionService (lenta) y sólo disponible si se ha establecido UUID
    private static final Cache<String, Identifier> skinCache = CacheBuilder.newBuilder()
            .expireAfterAccess(15, TimeUnit.MINUTES)
            .build();
    private static final Cache<String, Set<Consumer<Identifier>>> callbackMap = CacheBuilder.newBuilder()
            .expireAfterWrite(15, TimeUnit.SECONDS)
            .build();

    public static void get(String playerName, UUID playerUUID, Consumer<Identifier> callback) {
        Identifier loc = skinCache.getIfPresent(playerName);
        if (loc != null) {
            callback.accept(loc);
            return;
        }

        ClientPlayNetworkHandler networkHandler = MinecraftClient.getInstance().getNetworkHandler();
        PlayerListEntry playerInfo = networkHandler == null ? null : networkHandler.getPlayerListEntry(playerUUID);
        if (playerInfo != null) { // Cargar desde el network player
            loc = playerInfo.getSkinTexture();
            if (loc != DefaultSkinHelper.getTexture(playerInfo.getProfile().getId())) {
                callback.accept(loc);
                skinCache.put(playerName, loc);
                return;
            }
        }

        if (playerUUID == null) return; // No hay mucho que podamos hacer aquí :(

        synchronized (callbackMap) {
            Set<Consumer<Identifier>> consumers = callbackMap.getIfPresent(playerName);
            if (consumers == null) {
                // Hacer una llamada por usuario - de nuevo protección de límite de tarifa
                GameProfile profile = new GameProfile(playerUUID, playerName); // Creación simple de GameProfile
                MinecraftClient.getInstance().getSkinProvider().loadSkin(profile, (type, location, profileTexture) -> {
                    if (type == MinecraftProfileTexture.Type.SKIN) {
                        synchronized (callbackMap) {
                            Set<Consumer<Identifier>> consumerSet = callbackMap.getIfPresent(playerName);
                            if (consumerSet != null)
                                consumerSet.forEach(consumer -> consumer.accept(location));
                            callbackMap.invalidate(playerName);
                            callbackMap.cleanUp();
                        }
                        skinCache.put(playerName, location);
                    }
                }, true);

                HashSet<Consumer<Identifier>> newSet = new HashSet<>();
                newSet.add(callback);
                callbackMap.put(playerName, newSet);
            } else {
                consumers.add(callback);
            }
        }
    }
}
