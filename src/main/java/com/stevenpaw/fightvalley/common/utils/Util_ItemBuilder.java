package com.stevenpaw.fightvalley.common.utils;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerProfile;

import java.util.Arrays;
import java.util.List;

public class Util_ItemBuilder {

    private final ItemStack item;
    private final ItemMeta itemMeta;

    /**
     * Baue ItemBuilder auf mit einem Material
     * @param material (Material) = Das Material
     */
    public Util_ItemBuilder(Material material) {
        item = new ItemStack(material);
        itemMeta = item.getItemMeta();
        assert itemMeta != null;
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
    }

    /**
     * Setze den Schaden des Items
     * @param damage (Integer) = Schaden
     */
    public Util_ItemBuilder setDamage(int damage) {
        ((Damageable) itemMeta).setDamage(damage);
        return this;
    }

    /**
     * Setze die Anzahl der Items
     * @param amount (Integer) = Anzahl
     */
    public Util_ItemBuilder setAmount(int amount) {
        item.setAmount(amount);
        return this;
    }

    /**
     * Setze den Namen des Items
     * @param name (String) = Name
     */
    public Util_ItemBuilder setDisplayName(String name) {
        itemMeta.setDisplayName(name);
        return this;
    }

    /**
     * Setze die Custom Model Data des Items
     * @param amount (Integer) = Custom Model Data
     */
    public Util_ItemBuilder setCustomModelData(int amount) {
        itemMeta.setCustomModelData(amount);
        return this;
    }

    /**
     * Setze die Lore des Items; Mehrere Zeilen durch Komma trennen
     * @param lore (String) = Lore
     */
    public Util_ItemBuilder setLore(String... lore) {
        itemMeta.setLore(Arrays.asList(lore));
        return this;
    }

    /**
     * Setze Verzauberungs-Aussehen des Items
     */
    public Util_ItemBuilder setEnchanted() {
        itemMeta.addEnchant(Enchantment.FIRE_ASPECT, 10,true);
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        return this;
    }

    public Util_ItemBuilder setEnchantment(Enchantment enchantment, int level) {
        itemMeta.addEnchant(enchantment, level, true);
        return this;
    }

    /**
     * Setze die Lore des Items aus einer Liste
     * @param lore (List) = Liste
     */
    public Util_ItemBuilder setLoreFromArray(List<String> lore) {
        itemMeta.setLore(lore);
        return this;

    }

    /**
     * Setzt eine custom texture für einen Skull
     * @param skincode (String): MinecraftAdresse (von https://minecraft-heads.com/)
     */
    public Util_ItemBuilder setCustomSkull(String skincode) {
        SkullMeta meta = (SkullMeta) itemMeta;
        assert meta != null;
        PlayerProfile profile = Util_PlayerProfile.getProfile("https://textures.minecraft.net/texture/"+ skincode);
        meta.setOwnerProfile(profile);
        item.setItemMeta(meta);
        return this;
    }

    public ItemStack build() {
        item.setItemMeta(itemMeta);
        return item;
    }
}
