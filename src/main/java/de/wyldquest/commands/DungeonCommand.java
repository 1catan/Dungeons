package de.wyldquest.commands;

import de.wyldquest.utils.Npc;
import de.wyldquest.utils.NpcBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class DungeonCommand implements CommandExecutor {

    private NpcBuilder npcBuilder;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "You must be a player to use this command");
            return true;
        }
        Player player = (Player) sender;
        switch (args.length) {
            case 0:
                player.sendMessage(ChatColor.RED + "/dungeon create <name>");
                break;
            case 1:
                if(args[0].equalsIgnoreCase("setspawn")) {
                    File file = new File("plugins/Dungeons", "config.yml");
                    if(!file.exists()) {
                        try {
                            file.createNewFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    YamlConfiguration yml = YamlConfiguration.loadConfiguration(file);
                    yml.set("loc", player.getLocation());
                    try {
                        yml.save(file);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    player.sendMessage("Spawn has been set");
                    break;
                }
                if(args[0].equalsIgnoreCase("npc")) {
                    npcBuilder = new NpcBuilder();
                    Npc npc = new Npc(npcBuilder.getNPCs().get(0));
                    File file = new File("plugins/Dungeons/","npcs.yml");
                    FileConfiguration yml = YamlConfiguration.loadConfiguration(file);
                    npcBuilder.getNPCs().forEach(e -> player.sendMessage("Id: " +e));
                    /*player.sendMessage("NPC: " + npc.getName());
                    player.sendMessage("NPC: " + npc.getEntityId());
                    player.sendMessage("NPC: " + npc.getUUID());
                    player.sendMessage("NPC: " + npc.getSkinUUID());
                    player.sendMessage("NPC: " + npc.getSkinValue());
                    player.sendMessage("NPC: " + npc.getSkinSignature());
                    player.sendMessage("NPC: " + npc.getLocation());

                    /*npc.createNPC("Enzait", player, "Enzait");*/
                    break;
                }
            case 2:
                if(args[0].equalsIgnoreCase("npc")) {
                    if(args[1].equalsIgnoreCase("list")) {
                        npcBuilder = new NpcBuilder();
                        List<Integer> list = npcBuilder.getNPCs();
                        player.sendMessage("??7Id ??8| ??7Name");
                        for(int id : list) {
                            Npc npc = new Npc(id);
                            player.sendMessage(ChatColor.GRAY +""+ id + " " + npc.getName());
                        }
                    }
                }
            case 3:
                if(args[0].equalsIgnoreCase("npc")) {
                    if (args[1].equalsIgnoreCase("delete")) {
                        try {
                            int id = Integer.parseInt(args[2]);
                            npcBuilder = new NpcBuilder();
                            List<Integer> list = npcBuilder.getNPCs();
                            if(list.contains(id)) {
                                npcBuilder.removeNpc(id);
                                player.sendMessage("deleted npc " + id);
                            } else {
                                player.sendMessage(ChatColor.RED + "enter a valid id");
                            }
                        } catch (NumberFormatException e) {
                          player.sendMessage(ChatColor.RED + "enter a valid number");
                        }
                    }
                    if(args[1].equalsIgnoreCase("rotation")) {
                        try {
                            int id = Integer.parseInt(args[2]);
                            npcBuilder = new NpcBuilder();
                            List<Integer> list = npcBuilder.getNPCs();
                            if(list.contains(id)) {
                                File file = new File("plugins/Dungeons/","npcs.yml");
                                FileConfiguration yml = YamlConfiguration.loadConfiguration(file);
                                boolean rotation = yml.getBoolean("npc."+id+".lookclose");
                                rotation = !rotation;
                                yml.set("npc."+id+".lookclose", rotation);
                                player.sendMessage(ChatColor.GRAY +""+ id + " rotation is now " + (rotation ? "on" : "off"));
                                try {
                                    yml.save(file);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                player.sendMessage(ChatColor.RED + "enter a valid id");
                            }
                        } catch (NumberFormatException e) {
                            player.sendMessage(ChatColor.RED + "enter a valid number");
                        }
                    }
                }
                break;
            case 4:
                if(args[0].equalsIgnoreCase("npc")) {
                    if(args[1].equalsIgnoreCase("create")) {
                        String name = args[2];
                        String npcskin = args[3];
                        npcBuilder = new NpcBuilder();
                        for(Player all : Bukkit.getOnlinePlayers()) {
                            npcBuilder.createNPC(name, all, npcskin);
                        }
                    }
                }
            default:
                if(args[0].equalsIgnoreCase("create")) {
                    String name = "";
                    for(int i = 1; i < args.length; i++) {
                        name = name + " " + args[i];
                    }
                    player.sendMessage("Dungeon" + name + " was created");
                    File ordner = new File("plugins/Dungeons", name+".yml");
                    if(!ordner.exists()) {
                        try {
                            ordner.createNewFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                } else {
                    player.sendMessage(ChatColor.RED + "/dungeon create <name>");
                    break;
                }

        }

        return false;
    }
}
