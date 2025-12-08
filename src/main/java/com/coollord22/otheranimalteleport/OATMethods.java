package com.coollord22.otheranimalteleport;

import com.coollord22.otheranimalteleport.assets.Verbosity;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class OATMethods {
	public static void teleportLeashedEnt(LivingEntity entity, Location to, Player p, OtherAnimalTeleport plugin) {
		String entID = "[Ent-" + entity.getEntityId() + "] ";

		plugin.log.logInfo(entID + "Received leashed-entity teleport. Attempting to null current leash holder.", Verbosity.HIGHEST);
		entity.setLeashHolder(null);

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
			handleInvulnerability(entity, plugin);

			plugin.log.logInfo(entID + "Teleporting entity " + entity.getType(), Verbosity.HIGH);
			if(!entity.teleport(to)) {
				throw new RuntimeException("Unsuccessful entity teleport");
			}

			Bukkit.getScheduler().runTaskLater(plugin, () -> {
				// Delay re-attaching of leash by 10 ticks to ensure entity pathfinding cant freeze
				plugin.log.logInfo(entID + "Re-attaching leash holder as " + p.getName() + ".", Verbosity.HIGHEST);
				entity.setLeashHolder(p);
			}, 10L);
		}, 5L);
	}

	public static void teleportTamedEnt(Entity entity, Location to, OtherAnimalTeleport plugin) {
		String entID = "[Ent-" + entity.getEntityId() + "] ";

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
			handleInvulnerability(entity, plugin);

			plugin.log.logInfo(entID + "Teleporting entity " + entity.getType(), Verbosity.HIGH);
			if(!entity.teleport(to)) {
				throw new RuntimeException("Unsuccessful entity teleport");
			}
		}, 5L);
	}

	private static void handleInvulnerability(Entity entity, OtherAnimalTeleport plugin) {
		String entID = "[Ent-" + entity.getEntityId() + "] ";
		boolean wasInvulnerable = entity.isInvulnerable();

		plugin.log.logInfo(entID + "Protecting entity with invulnerability and resistance.", Verbosity.HIGHEST);
		entity.setInvulnerable(true);

		Bukkit.getScheduler().runTaskLater(plugin, () -> {
			plugin.log.logInfo(entID + "Reverting invulnerability status.", Verbosity.HIGHEST);
			entity.setInvulnerable(wasInvulnerable);
		}, 30L);
	}
}
