/*
 * Portions of this code are based on code from Sync https://github.com/iChun/Sync which is licensed under LGPL 3.0.
 * Copyright (c) 2007 iChun.
 * You can obtain a copy of the license at http://www.gnu.org/licenses/lgpl-3.0.html
*/

package com.crawkatt.meicamod.util;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.resources.ResourceLocation;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class SyncSkinManager {
    // Almacena las skins en caché para evitar alcanzar el límite de velocidad de los servidores de sesión de skins.
    // Mantener los valores durante más tiempo, para que se carguen rápido si se cargan muchos TEs con el mismo jugador, o cuando se cargan otros chunks con el mismo jugador
    // Prioridad de carga de skins: Cache (la más rápida), NetworkPlayer (sólo disponible cuando el jugador está online y en la misma dimensión que el shell, rápida), SessionService (lenta) y sólo disponible si se ha establecido UUID
    private static final Cache<String, ResourceLocation> skinCache = CacheBuilder.newBuilder()
            .expireAfterAccess(15, TimeUnit.MINUTES)
            .build();
    private static final Cache<String, Set<Consumer<ResourceLocation>>> callbackMap = CacheBuilder.newBuilder()
            .expireAfterWrite(15, TimeUnit.SECONDS)
            .build();

    public static void get(String playerName, UUID playerUUID, Consumer<ResourceLocation> callback) {
        ResourceLocation loc = skinCache.getIfPresent(playerName);
        if (loc != null) {
            callback.accept(loc);
            return;
        }

        ClientPacketListener networkHandler = Minecraft.getInstance().getConnection();
        PlayerInfo playerInfo = networkHandler == null ? null : networkHandler.getPlayerInfo(playerUUID);
        if (playerInfo != null) { // Cargar desde el network player
            loc = playerInfo.getSkinLocation();
            if (loc != DefaultPlayerSkin.getDefaultSkin(playerInfo.getProfile().getId())) {
                callback.accept(loc);
                skinCache.put(playerName, loc);
                return;
            }
        }

        if (playerUUID == null) return; // No hay mucho que podamos hacer aquí :(

        synchronized (callbackMap) {
            Set<Consumer<ResourceLocation>> consumers = callbackMap.getIfPresent(playerName);
            if (consumers == null) {
                // Hacer una llamada por usuario - de nuevo protección de límite de tarifa
                GameProfile profile = new GameProfile(playerUUID, playerName); // Creación simple de GameProfile
                Minecraft.getInstance().getSkinManager().registerSkins(profile, (type, location, profileTexture) -> {
                    if (type == MinecraftProfileTexture.Type.SKIN) {
                        synchronized (callbackMap) {
                            Set<Consumer<ResourceLocation>> consumerSet = callbackMap.getIfPresent(playerName);
                            if (consumerSet != null)
                                consumerSet.forEach(consumer -> consumer.accept(location));
                            callbackMap.invalidate(playerName);
                            callbackMap.cleanUp();
                        }
                        skinCache.put(playerName, location);
                    }
                }, true);

                HashSet<Consumer<ResourceLocation>> newSet = new HashSet<>();
                newSet.add(callback);
                callbackMap.put(playerName, newSet);
            } else {
                consumers.add(callback);
            }
        }
    }
}