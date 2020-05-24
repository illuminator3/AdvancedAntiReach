package me.illuminator3.aar;

public class Utils
{
    public static double round(double value, int precision)
    {
        String s = Double.toString(value);

        return Double.parseDouble(s.substring(0, s.indexOf(".") + precision + 1));
    }
}