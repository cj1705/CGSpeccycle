package com.carsongames.cgspeccycle;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;

import java.util.Objects;

public class InvOpen implements Listener {
    CGSpeccycle main = CGSpeccycle.getInstance();

    @EventHandler
    public void onOpenInventory(InventoryOpenEvent event) {
       {
           try {
               if (event.getPlayer() == main.current) {
                   Bukkit.getLogger().info(event.getPlayer().getName());
                   Bukkit.getLogger().info(main.current.getName());
                   main.getBot().openInventory(event.getInventory());
               }
           }
           catch (Exception e){

           }


        }

    }
}
