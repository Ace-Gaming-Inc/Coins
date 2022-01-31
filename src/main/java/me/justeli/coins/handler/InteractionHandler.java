package me.justeli.coins.handler;

import me.justeli.coins.item.CoinUtil;
import me.justeli.coins.util.Util;
import org.bukkit.block.Container;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * Created by Eli on 2/4/2017.
 */
public class InteractionHandler
        implements Listener
{
    @EventHandler
    public void coinPlace (PlayerInteractEvent event)
    {
        if (event.getAction() == Action.PHYSICAL)
            return;

        if (!CoinUtil.isWithdrawnCoin(event.getItem()))
            return;

        Player player = event.getPlayer();

        // because of .setAmount(0) AND Container, players have to drop coin instead
        if (!player.hasPermission("coins.withdraw"))
        {
            event.setCancelled(true);
            return;
        }

        if (event.getClickedBlock() == null || !(event.getClickedBlock().getState() instanceof Container))
        {
            event.setCancelled(true);
            double amount = CoinUtil.getValue(event.getItem());

            event.getItem().setAmount(0);

            PickupHandler.giveMoney(player, amount);
            Util.playCoinPickupSound(player);
        }
    }
}
