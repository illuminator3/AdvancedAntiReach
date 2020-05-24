package me.illuminator3.aar;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class AARCommand
    implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        sender.sendMessage(new String[] {
                "§7§m---------------------------",
                "§6AAR §7developed by §eilluminator3",
                "§7Discord§8: §eilluminator3#0001",
                "§7§m---------------------------"
        });

        return true;
    }
}