package com.carsongames.cgspeccycle;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;

import java.util.Objects;

public class InvClose implements Listener {
    CGSpeccycle main = CGSpeccycle.getInstance();

    @EventHandler
    public void onOpenInventory(InventoryCloseEvent event) {

        {
            try {
                if (event.getPlayer() == main.current) {
                    Bukkit.getLogger().info(event.getPlayer().getName());
                    Bukkit.getLogger().info(main.current.getName());
                    main.getBot().closeInventory();
                }
            }
            catch (Exception e){

            }


        }

    }
}
