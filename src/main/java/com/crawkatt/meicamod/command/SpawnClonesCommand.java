package com.crawkatt.meicamod.command;

import com.crawkatt.meicamod.entity.ModEntities;
import com.crawkatt.meicamod.entity.custom.PlayerCloneEntity;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

public class SpawnClonesCommand {
    public static void register(
            CommandDispatcher<ServerCommandSource> dispatcher,
            CommandRegistryAccess commandRegistryAccess,
            CommandManager.RegistrationEnvironment registrationEnvironment
    ) {
        dispatcher.register(CommandManager.literal("spawnclones")
                .requires(serverCommandSource -> serverCommandSource.hasPermissionLevel(2))
                .executes(context -> execute(context, (PlayerEntity) context.getSource().getEntity()))
                .then(CommandManager.argument("target", EntityArgumentType.player())
                        .executes(context -> execute(context, EntityArgumentType.getPlayer(context, "target")))
                )
        );
    }

    private static int execute(CommandContext<ServerCommandSource> context, PlayerEntity targetPlayer) {
        ServerCommandSource source = context.getSource();
        if (targetPlayer != null) {
            spawnPlayerClonesAround(targetPlayer);
            source.sendFeedback(() -> Text.literal("clones spawned around " + targetPlayer.getName().getString() + "!"), true);
        } else {
            source.sendError(Text.literal("No valid player specified"));
        }

        return 1;
    }

    private static void spawnPlayerClonesAround(LivingEntity entity) {
        if (entity.getWorld() instanceof ServerWorld serverWorld) {
            BlockPos pos = entity.getBlockPos();
            PlayerCloneEntity clone = new PlayerCloneEntity(ModEntities.PLAYER_CLONE, serverWorld);
            clone.teleport(
                    pos.getX() + serverWorld.random.nextInt(10) - 5,
                    pos.getY(),
                    pos.getZ() + serverWorld.random.nextInt(10) - 5
            );

            if (entity instanceof PlayerEntity player) {
                clone.copyInventory(player);
                clone.copyArmor(player);
                clone.setTarget(player);
            }

            serverWorld.spawnEntity(clone);
        }
    }
}
