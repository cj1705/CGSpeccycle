package com.carsongames.cgspeccycle;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.*;


public final class CGSpeccycle extends JavaPlugin {
    ScoreHelper helper;
    String nobot = "Please Shut down the server and enter the bot details in config.yml!";
    int sec = 0;
    int configsec = 0;
    String BOT = " ";
    static CGSpeccycle instance;

    public Player current = null;
    public Player last = null;


    @Override
    public void onEnable() {
        instance = this;

        getServer().getPluginManager().registerEvents(new InvOpen(), this);
        getServer().getPluginManager().registerEvents(new InvClose(), this);
        getLogger().info("Spectating BOT has started!");
        File configFile = new File(this.getDataFolder(), "config.yml");
        if(!configFile.exists()){
            SetConfigDefault();
        }else{
            try{
                if(getConfig().getString("BOT") == null || getConfig().getString("interval") == null){
                    getLogger().severe(nobot);
                    getServer().getPluginManager().disablePlugin(this);

                }


                    BOT = getConfig().getString("BOT");
                configsec = Integer.parseInt(getConfig().getString("interval"));
                sec = configsec;
                StartPlugin();

            }
            catch (Exception e){
                e.printStackTrace();
            }

        }



    }
    public void SetConfigDefault(){
        getLogger().info("Creating Config File");
        this.saveDefaultConfig();
        getLogger().info(nobot);
        getServer().getPluginManager().disablePlugin(this);

    }
    public void StartPlugin(){
        Timer();
        Timer2();
        UpdateScoreBoard();
    }



    @Override
    public void onDisable() {
        getLogger().info("Spectating BOT has stopped!");

    }


    public Player getNext() {

        ScoreHelper.removeScore(Objects.requireNonNull(getBot()));
        ArrayList<String> players = new ArrayList<String>();
        for (Player player : getServer().getOnlinePlayers()) {

                players.add(player.getName());
        }

        players.remove(getBot().getName());
        int min = 0;
        int max = players.size() -1;
        int index = (int) (Math.random() * (max - min + 1) + min);
      //  getLogger().info("Index: " + index);
        try {
            Player next = getServer().getPlayer(players.get(index));
            assert next != null;
     //       getLogger().info("Selected: " + next.getName());
            if (current != null) {
                last = current;
            }
            if(next.getName() == getBot().getName()){
                next = null;
            }
            if(next.getGameMode() == GameMode.SPECTATOR){
                next = null;
            }
            current = next;
            return current;
        } catch (Exception e) {
            if (last != null) {
                return last;
            } else {
                return null;
            }


        }

    }
    public Player getBot(){

        try{
            return getServer().getPlayer(BOT);
        }
        catch (Exception e){


           return null;
        }
    }
    public void Timer() {


            Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, () -> {

                sec = sec -1;
                if(sec <= 0) {
                    try {
                        Objects.requireNonNull(getBot()).closeInventory();
                        if (getGamemode() != GameMode.SPECTATOR) {
                            getBot().setGameMode(GameMode.SPECTATOR);

                        }


                        getBot().setSpectatorTarget(getNext());
                        setBotInv();
                        sec = configsec;
                    } catch (NullPointerException e) {
                    }


                }



                }, 0, 20);

    }
    public void setBotInv(){
        Objects.requireNonNull(getBot()).getInventory().clear();
        ItemStack[] current_items = current.getInventory().getContents();
        getBot().getInventory().setContents(current_items);



    }
    public void Timer2() {

        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, () -> {
            try {
                if(getBot().getSpectatorTarget() == null){
                    getBot().setSpectatorTarget(current);
                }
                if (getBot().isDead()) {
                    getBot().spigot().respawn();
                }
                if (getGamemode() == GameMode.SURVIVAL) {
                    Objects.requireNonNull(getBot()).getInventory().clear();
                    getBot().setGameMode(GameMode.SPECTATOR);

                }
            } catch (Exception e){
    e.printStackTrace();
            }

        }, 0, 1);
    }




    public GameMode getGamemode(){
        return Objects.requireNonNull(getBot()).getGameMode();
    }


    public void UpdateScoreBoard() {

            Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, () -> {
                try {
                if (ScoreHelper.hasScore(Objects.requireNonNull(getBot()))) {


                    helper.setTitle("-----Spectating BOT-----");
                    if(current == null){
                        helper.setSlot(6, "Current Player : No one");

                    }
                    else {
                        helper.setSlot(6, "Current Player : " + current.getName());
                    }
                    if (current.getHealth() == 0.0) {
                        helper.setSlot(5, "Player Health  : " + "Dead");
                    } else {
                        helper.setSlot(5, "Player Health  : " + Math.round(current.getHealth()) + " HP");
                    }
                    helper.setSlot(4,"Player Hunger Level : " + current.getFoodLevel());
                    helper.setSlot(3, "Player Experience : " + current.getExpToLevel() + " XP");
                    helper.setSlot(2, "Player Location : " + "X" + (int) current.getLocation().getX() + " Y" + (int) current.getLocation().getY() + " Z" + (int) current.getLocation().getZ());
                    helper.setSlot(1, ChatColor.GOLD + "Next Player in : " + sec + " seconds");
                } else {
                    helper = ScoreHelper.createScore(getBot());
                }
                }catch (NullPointerException e){
                }
            }, 0, 10);



    }




    public static CGSpeccycle getInstance() {
        return instance;
    }
    }


