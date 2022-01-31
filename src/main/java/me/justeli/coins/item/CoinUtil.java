package me.justeli.coins.item;

import me.justeli.coins.config.Config;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** by Eli on January 30, 2022 **/
public class CoinUtil
{
    // three types of tags:
    //  - coins-type: Integer (as ID)
    //  - coins-worth: Double
    //  - coins-random: Integer

    public static final String COINS_TYPE = "coins-type"; // 1 = dropped, 2 = withdrawn, 3 = other
    public static final int TYPE_DROPPED = 1;
    public static final int TYPE_WITHDRAWN = 2;
    public static final int TYPE_OTHER = 3;

    public static final String COINS_WORTH = "coins-worth";
    public static final String COINS_RANDOM = "coins-random";

    public static boolean isCoin (ItemStack item)
    {
        if (item == null)
            return false;

        if (MetaBuilder.of(item).data(COINS_TYPE, PersistentDataType.INTEGER).isPresent())
            return true;

        if (!Config.DETECT_LEGACY_COINS)
            return false;

        return isWithdrawnCoin(item);
    }

    public static boolean isDroppedCoin (ItemStack item)
    {
        if (item == null)
            return false;

        return MetaBuilder.of(item).data(COINS_TYPE, PersistentDataType.INTEGER).orElse(0) == TYPE_DROPPED;
    }

    public static boolean isWithdrawnCoin (ItemStack item)
    {
        if (item == null)
            return false;

        if (MetaBuilder.of(item).data(COINS_TYPE, PersistentDataType.INTEGER).orElse(0) == TYPE_WITHDRAWN)
            return true;

        if (!Config.DETECT_LEGACY_COINS || Config.LEGACY_WITHDRAWN_COIN_ENDING == null)
            return false;

        return name(item).map(name -> name.endsWith(Config.LEGACY_WITHDRAWN_COIN_ENDING)).orElse(false);
    }

    private static final Pattern VALUE_PATTERN = Pattern.compile("[0-9.]+");

    public static double getValue (ItemStack item)
    {
        if (item == null)
            return 0;

        Optional<Double> worth = MetaBuilder.of(item).data(COINS_WORTH, PersistentDataType.DOUBLE);

        if (worth.isPresent())
            return worth.get() * item.getAmount();

        if (!Config.DETECT_LEGACY_COINS)
            return 0;

        Optional<String> name = name(item);
        if (!name.isPresent())
            return 0;

        Matcher matcher = VALUE_PATTERN.matcher(ChatColor.stripColor(name.get()));
        if (matcher.find())
        {
            try { return NumberFormat.getInstance().parse(matcher.group(0)).doubleValue() * item.getAmount(); }
            catch (ParseException exception) { return 0; }
        }
        else
        {
            return 0;
        }
    }

    private static Optional<String> name (ItemStack item)
    {
        if (item == null || item.getItemMeta() == null || !item.getItemMeta().hasDisplayName())
            return Optional.empty();

        return Optional.of(item.getItemMeta().getDisplayName());
    }
}
