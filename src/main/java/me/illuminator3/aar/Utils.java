package me.illuminator3.aar;

import org.bukkit.command.CommandExecutor;

public class Utils
{
    public static double cut(double value, int precision)
    {
        String s = Double.toString(value);

        return Double.parseDouble(s.substring(0, s.indexOf(".") + precision + 1));
    }

    public static CommandExecutor createMessageExecutor(String... messages)
    {
        return (a0, a1, a2, a3) -> {
            a0.sendMessage(messages);

            return true;
        };
    }
}