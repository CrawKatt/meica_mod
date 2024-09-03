package com.crawkatt.meicamod.command;

import com.crawkatt.meicamod.entity.ModEntities;
import com.crawkatt.meicamod.entity.custom.player_clone.PlayerCloneEntity;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class SpawnClonesCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("spawnclones")
                .requires(source -> source.hasPermission(2))
                .executes(context -> execute(context, (Player) context.getSource().getEntity())) // Usa el jugador que ejecuta el comando si no hay argumento
                .then(Commands.argument("target", EntityArgument.player())
                        .executes(context -> execute(context, EntityArgument.getPlayer(context, "target")))) // Usa el jugador proporcionado en el argumento
        );
    }

    private static int execute(CommandContext<CommandSourceStack> context, Player targetPlayer) {
        CommandSourceStack source = context.getSource();
        if (targetPlayer != null) {
            spawnPlayerClonesAround(targetPlayer);
            source.sendSuccess(() -> Component.literal("Clones spawned around " + targetPlayer.getName().getString() + "!"), true);
        } else {
            source.sendFailure(Component.literal("No valid player specified"));
        }

        return Command.SINGLE_SUCCESS;
    }

    private static void spawnPlayerClonesAround(LivingEntity entity) {
        if (entity.level() instanceof ServerLevel serverLevel) {
            BlockPos pos = entity.blockPosition();
            PlayerCloneEntity clone = new PlayerCloneEntity(ModEntities.PLAYER_CLONE.get(), serverLevel);
            clone.moveTo(
                    pos.getX() + serverLevel.random.nextInt(10) - 5,
                    pos.getY(),
                    pos.getZ() + serverLevel.random.nextInt(10) - 5
            );

            if (entity instanceof Player player) {
                clone.copyInventory(player);
                clone.copyArmor(player);
                clone.setTarget(player);
            }

            serverLevel.addFreshEntity(clone);
        }
    }
}
