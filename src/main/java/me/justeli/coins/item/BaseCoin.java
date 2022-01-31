package me.justeli.coins.item;

import me.justeli.coins.config.Config;
import me.justeli.coins.util.Skull;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/** by Eli on January 30, 2022 **/
public class BaseCoin
{
    private final ItemStack withdrawnCoin;
    private final ItemStack droppedCoin;
    private final ItemStack otherCoin;

    private BaseCoin ()
    {
        String texture = Config.SKULL_TEXTURE;

        ItemStack baseCoin = texture == null || texture.isEmpty()? new ItemStack(Config.COIN_ITEM) : Skull.of(texture);
        ItemMeta baseCoinMeta = baseCoin.getItemMeta();

        if (Config.CUSTOM_MODEL_DATA > 0)
        {
            baseCoinMeta.setCustomModelData(Config.CUSTOM_MODEL_DATA);
        }
        if (Config.ENCHANTED_COIN)
        {
            baseCoinMeta.addEnchant(Enchantment.DURABILITY, 1, true);
            baseCoinMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }

        baseCoin.setItemMeta(baseCoinMeta);

        this.withdrawnCoin = MetaBuilder.of(baseCoin.clone()).data(CoinUtil.COINS_TYPE, CoinUtil.TYPE_WITHDRAWN).build();
        MetaBuilder droppedCoinItem = MetaBuilder.of(baseCoin.clone()).name(Config.DROPPED_COIN_NAME).data(CoinUtil.COINS_TYPE, CoinUtil.TYPE_DROPPED);

        if (Config.DROP_EACH_COIN)
        {
            droppedCoinItem.data(CoinUtil.COINS_WORTH, 1D);
        }

        this.droppedCoin = droppedCoinItem.build();
        this.otherCoin = MetaBuilder.of(baseCoin.clone()).name(Config.DROPPED_COIN_NAME).data(CoinUtil.COINS_TYPE, CoinUtil.TYPE_OTHER).build();
    }

    public static BaseCoin initialize ()
    {
        return new BaseCoin();
    }

    public ItemStack dropped ()
    {
        return this.droppedCoin.clone();
    }

    public ItemStack withdrawn ()
    {
        return this.withdrawnCoin.clone();
    }

    public MetaBuilder other ()
    {
        return MetaBuilder.of(this.otherCoin.clone());
    }
}
