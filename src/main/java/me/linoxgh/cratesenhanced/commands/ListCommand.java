package me.linoxgh.cratesenhanced.commands;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import me.linoxgh.cratesenhanced.data.BlockPosition;
import me.linoxgh.cratesenhanced.data.Crate;
import me.linoxgh.cratesenhanced.data.CrateStorage;
import me.linoxgh.cratesenhanced.data.CrateType;
import me.linoxgh.cratesenhanced.utils.ItemUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ListCommand extends Command {

    private final CrateStorage crates;

    ListCommand(@NotNull CrateStorage crates) {
        this.crates = crates;
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String[] args) {
        if (!(sender.hasPermission("crates.list"))) {
            sender.sendMessage("§4You do not have enough permission to use this command.");
            return true;
        }

        if (args.length == 1) {
            sender.sendMessage("§e.*.-----_-----{ §3Crates §e}-----_-----.*.");
            sender.sendMessage("§9Crate Name §e- §9Location §e- §9Crate Type");
            for (Map.Entry<String, Crate> entry : crates.getCrates().entrySet()) {
                Crate crate = entry.getValue();
                BlockPosition pos = crate.getPos();
                String location = pos.getX() + ":" + pos.getY() + ":" + pos.getZ() + ":" + pos.getWorld();
                sender.sendMessage("§e- §f" + entry.getKey() + " §e- §f" + location + " §e- §f" + crate.getCrateType());
            }
            return true;
        }

        if (args.length != 2 && args.length != 3) return false;

        switch (args[1]) {
            case "crates":
                sender.sendMessage("§e.*.-----_-----{ §3Crates §e}-----_-----.*.");
                sender.sendMessage("§9Crate Name §e- §9Location §e- §9Crate Type");
                for (Map.Entry<String, Crate> entry : crates.getCrates().entrySet()) {
                    Crate crate = entry.getValue();
                    BlockPosition pos = crate.getPos();
                    String location = pos.getX() + ":" + pos.getY() + ":" + pos.getZ() + ":" + pos.getWorld();
                    sender.sendMessage("§e- §f" + entry.getKey() + " §e- §f" + location + " §e- §f" + crate.getCrateType());
                }
                return true;

            case "types":
                sender.sendMessage("§e.*.-----_-----{ §3Crates §e}-----_-----.*.");
                sender.sendMessage("§9Crate Types");
                for (Map.Entry<String, CrateType> entry : crates.getCrateTypes().entrySet()) {
                    ItemStack key = entry.getValue().getKey();
                    sender.sendMessage(Component.text("§e- §f" + entry.getKey() + " §e- §f")
                            .append(ItemUtil.displayName(key).hoverEvent(key.asHoverEvent()))
                            .append(Component.text(" §6x§9" + key.getAmount()))
                    );
                }
                return true;

            case "rewards":
                if (args.length != 3) return false;
                CrateType crate = crates.getCrateType(args[2]);
                if (crate == null) {
                    sender.sendMessage("§4Could not find the specified crate type.");
                    return true;
                }
                sender.sendMessage("§e.*.-----_-----{ §3Crates Enhanced §e}-----_-----.*.");
                sender.sendMessage("§9Reward §e- §9Weight");
                List<Map.Entry<ItemStack, Integer>> entries = new ArrayList<>(crate.getWeights().entrySet());
                entries.sort((e1,e2) -> {

                    int weightDiff = e1.getValue() - e2.getValue();
                    if (weightDiff != 0) return weightDiff;

                    String s1 = PlainComponentSerializer.plain().serialize(ItemUtil.displayName(e1.getKey()));
                    String s2 = PlainComponentSerializer.plain().serialize(ItemUtil.displayName(e2.getKey()));

                    return s1.compareTo(s2);
                });
                for (Map.Entry<ItemStack, Integer> entry : entries) {
                    ItemStack drop = entry.getKey();
                    if (drop == null) continue;
                    sender.sendMessage(Component.text("§e- §f")
                            .append(ItemUtil.displayName(drop).hoverEvent(drop.asHoverEvent()))
                            .append(Component.text(" §6x§9" + drop.getAmount()))
                            .append(Component.text(" §e- §f" + entry.getValue().toString()))
                    );
                }
                return true;

            default:
                return false;
        }
    }
}
