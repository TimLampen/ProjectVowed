package me.vowed.api.shops;

import me.vowed.api.money.IMoney;
import me.vowed.api.player.PlayerWrapper;
import me.vowed.api.player.PlayerWrapperManager;
import me.vowed.api.plugin.Vowed;
import me.vowed.api.race.Race;
import me.vowed.api.race.races.RaceType;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by JPaul on 10/15/2015.
 */
public class ShopListener implements Listener
{
    Location shopLocation;
    int clickedSlot;
    ItemStack clickedItem;
    UniqueItem uniqueItem;
    boolean handlingItem;
    boolean isShift;
    boolean isRight;
    boolean isLeft;

    UniqueItem closeButton;
    ShopItem closeButtonItem;

    UniqueItem openButton;
    ShopItem openButtonItem;

    static HashMap<ItemStack, UniqueItem> uniqueItems = new HashMap<>();
    private HashMap<UUID, Boolean> isOwner = new HashMap<>();
    private HashMap<UUID, Boolean> isInShop = new HashMap<>();
    private HashMap<UUID, Boolean> handlingName = new HashMap<>();
    private HashSet<Inventory> inventories = new HashSet<>();

    @EventHandler
    public void on(PlayerJoinEvent joinEvent) throws IOException
    {
        Player player = joinEvent.getPlayer();

        isInShop.put(player.getUniqueId(), false);

        File data = new File("C:\\ProjectVowed\\plugins\\VowedCore\\Transactions\\DATA");
        if (!data.exists())
        {
            data.mkdir();
        }

        File nameList = new File("C:\\ProjectVowed\\plugins\\VowedCore\\Transactions\\DATA\\names.dataList");
        if (!nameList.exists())
        {
            nameList.createNewFile();
        }

        List<String> namesofFile = new ArrayList<>();
        Scanner scanner = new Scanner(nameList);
        while (scanner.hasNextLine())
        {
            namesofFile.add(scanner.nextLine());
        }

        FileWriter fileWriter = new FileWriter(nameList, true);
        PrintWriter printWriter = new PrintWriter(fileWriter);
        if (!namesofFile.contains(player.getName()))
        {
            printWriter.println(player.getName());
        }
        printWriter.close();
        fileWriter.close();
    }

    @EventHandler
    public void on(PlayerDropItemEvent dropItemEvent)
    {
        Player player = dropItemEvent.getPlayer();

        IShop shop = Vowed.getShopManager().createShop(player, player.getLocation().subtract(0, 1, 0), ShopType.GEAR_SHOP);
        shop.createShop();

        openButton = new UniqueItem(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 5), UUID.randomUUID().toString().toUpperCase().substring(0, 6));
        openButtonItem = new ShopItem(openButton);

        closeButton = new UniqueItem(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 14), UUID.randomUUID().toString().toUpperCase().substring(0, 6));
        closeButtonItem = new ShopItem(closeButton);

        shop.setOpen(false);
        handlingName.put(shop.getOwnerUUID(), true);
        shop.getOwner().sendMessage(ChatColor.YELLOW.toString() + ChatColor.BOLD + "Please enter your shop's name");
    }

    @EventHandler
    public void on(PlayerInteractEvent interactEvent)
    {
        Player player = interactEvent.getPlayer();
        PlayerWrapper playerWrapper = PlayerWrapperManager.getPlayerWrapper(player);
        Action action = interactEvent.getAction();

        if (action == Action.RIGHT_CLICK_BLOCK)
        {
            Block block = interactEvent.getClickedBlock();
            Location location = block.getLocation();

            if (block.getType() == Material.CHEST)
            {
                if (ShopUtils.isShop(block))
                {
                    interactEvent.setCancelled(true);

                    IShop shop = Vowed.getShopManager().getShop(location);
                    shopLocation = shop.getLocation(); //returns shop if the block clicked is shop, according to location

                    isOwner.put(player.getUniqueId(), Vowed.getShopManager().isOwner(shop, player));

                    if (shop.isOpen())
                    {
                        isInShop.put(player.getUniqueId(), true);
                        player.openInventory(shop.getInventory(playerWrapper));
                        inventories.add(shop.getInventory(playerWrapper));
                    } else
                    {
                        if (isOwner.get(player.getUniqueId()))
                        {
                            if (!handlingItem && !handlingName.get(player.getUniqueId()))
                            {
                                isInShop.put(player.getUniqueId(), true);
                                player.openInventory(shop.getInventory(playerWrapper));
                                inventories.add(shop.getInventory(playerWrapper));
                            } else if (handlingName.get(player.getUniqueId()))
                            {
                                player.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD + "Please type in a valid 16 character name, or enter ' cancel '");
                            }
                            else
                            {
                                player.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD + "Please type in a valid integer, or enter ' cancel '");
                            }
                        } else
                        {
                            player.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD + "Sorry, this shop is closed");
                        }
                    }
                }
            }
        }
        else if(action == Action.LEFT_CLICK_BLOCK){
            if(ShopUtils.isShop(interactEvent.getClickedBlock())){
                interactEvent.setCancelled(true);

                IShop shop = Vowed.getShopManager().getShop(interactEvent.getClickedBlock().getLocation());
                shop.destroyShop(player);
            }
        }
    }

    @EventHandler
    public void on(InventoryCloseEvent closeEvent)
    {
        Player player = (Player) closeEvent.getPlayer();
        PlayerWrapper playerWrapper = PlayerWrapperManager.getPlayerWrapper(player);
        Inventory inventory = closeEvent.getInventory();

        if (isInShop.get(player.getUniqueId()) != null)
        {
            if (isInShop.get(player.getUniqueId()))
            {
                IShop shop = Vowed.getShopManager().getShop(shopLocation);

                if (inventory.equals(shop.getInventory(playerWrapper)) && !handlingItem)
                {
                    isInShop.put(player.getUniqueId(), false);
                }
            }
        }
    }

    @EventHandler
    public void on(InventoryClickEvent clickEvent)
    {
        Player player = (Player) clickEvent.getWhoClicked();
        PlayerWrapper playerWrapper = PlayerWrapperManager.getPlayerWrapper(player);
        Inventory inventory = clickEvent.getClickedInventory();
        clickedSlot = clickEvent.getRawSlot();
        clickedItem = clickEvent.getCurrentItem();
        isShift = clickEvent.isShiftClick();
        isRight = clickEvent.isRightClick();
        isLeft = clickEvent.isLeftClick();

        if (isInShop.get(player.getUniqueId()) != null)
        {
            if (isInShop.get(player.getUniqueId()))
            {
                IShop shop = Vowed.getShopManager().getShop(shopLocation);
                PlayerWrapper ownerWrapper = PlayerWrapperManager.getPlayerWrapper(shop.getOwner());

                if (inventory != null)
                {
                    if (inventory.equals(shop.getInventory(playerWrapper)) || inventory.equals(shop.getOwnerInventory()))
                    {
                        clickEvent.setCancelled(true);

                        if (clickedItem != null && clickedItem.getType() != Material.AIR)
                        {
                            if (clickedItem.getType() == Material.STAINED_GLASS_PANE)
                            {
                                if (isOwner.get(player.getUniqueId()))
                                {
                                    if (ShopItem.getColour(clickedItem) == DyeColor.RED && isOwner.get(player.getUniqueId()))
                                    {
                                        shop.addItem(openButtonItem, 8);
                                        shop.setOpen(true);

                                        ShopItemManager.addShopItems(shop, openButtonItem);
                                        uniqueItems.put(openButtonItem.getShopItem(), openButton);

                                    } else if (ShopItem.getColour(clickedItem) == DyeColor.LIME && isOwner.get(player.getUniqueId()))
                                    {
                                        shop.addItem(closeButtonItem, 8);
                                        shop.setOpen(false);

                                        ShopItemManager.addShopItems(shop, closeButtonItem);
                                        uniqueItems.put(closeButtonItem.getShopItem(), closeButton);

                                        for (Inventory inventoryFinder : inventories)
                                        {
                                            closeInventories(inventoryFinder);
                                        }
                                    }
                                }
                            } else
                            {

                                Vowed.LOG.debug(clickedItem.toString());
                                ShopItem shopItem = ShopItemManager.getShopItem(shop, uniqueItems.get(clickedItem));

                                if (shopItem != null)
                                {
                                    if (isShift)
                                    {
                                        if (!shop.isOpen())
                                        {
                                            if (isOwner.get(player.getUniqueId()))
                                            { //returning item
                                                shop.removeItemWithPrice(shopItem);
                                                shopItem.removePrice();
                                                player.getInventory().addItem(shopItem.getShopItem());
                                            }
                                        } else
                                        {
                                            if (!isOwner.get(player.getUniqueId()))
                                            {//buying item
                                                IMoney buyerMoney = playerWrapper.getMoney();
                                                IMoney sellerMoney = ownerWrapper.getMoney();
                                                Vowed.LOG.info(Vowed.getRaceManager().getRace(player).getName());

                                                if (buyerMoney.getAmount() >= shopItem.getDwarfINTPrice())
                                                {
                                                    shop.removeItemWithPrice(shopItem);
                                                    shopItem.removePrice();
                                                    buyerMoney.subtract(Vowed.getMoneyManager().handleBuyerMoney(playerWrapper, shopItem));
                                                    sellerMoney.add(Vowed.getMoneyManager().handleSellerMoney(ownerWrapper, shopItem));
                                                    Vowed.LOG.info(String.valueOf(Vowed.getMoneyManager().handleBuyerMoney(playerWrapper, shopItem)));
                                                    player.getInventory().addItem(shopItem.getShopItem());

                                                    saveTransaction(playerWrapper, ownerWrapper, shopItem);
                                                }
                                            }
                                        }
                                    } else if (isRight)
                                    {
                                        if (clickedItem != null && clickedItem.getType() != Material.AIR)
                                        {
                                            if (isOwner.get(player.getUniqueId()))
                                            { //editing item
                                                handlingItem = true;

                                                player.closeInventory();
                                                player.sendMessage(ChatColor.YELLOW.toString() + ChatColor.BOLD + "Please enter the edited selling price");
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    } else if (inventory.equals(player.getInventory()) && isInShop.get(player.getUniqueId()))
                    {
                        clickEvent.setCancelled(true);

                        if (clickedItem != null && clickedItem.getType() != Material.AIR && !shop.isOpen())
                        {
                            if (isOwner.get(player.getUniqueId()))
                            { //setting price
                                if (isShift)
                                {
                                    isInShop.put(player.getUniqueId(), true);
                                    handlingItem = true;

                                    uniqueItem = new UniqueItem(clickedItem, UUID.randomUUID().toString().substring(0, 13).toUpperCase());

                                    ShopItem shopItem = new ShopItem(uniqueItem);
                                    shop.addItem(shopItem);

                                    player.getInventory().remove(shopItem.getShopItem());
                                    player.closeInventory();
                                    player.sendRawMessage(ChatColor.YELLOW.toString() + ChatColor.BOLD + "Please enter the selling price");
                                }
                            }
                        }
                    }
                }
            }
        }

    }

    @EventHandler
    public void on(AsyncPlayerChatEvent chatEvent)
    {
        Player player = chatEvent.getPlayer();
        PlayerWrapper playerWrapper = PlayerWrapperManager.getPlayerWrapper(player);

        if (isInShop.get(player.getUniqueId()) != null)
        {
            if (isInShop.get(player.getUniqueId()))
            {
                chatEvent.setCancelled(true);

                IShop shop = Vowed.getShopManager().getShop(shopLocation);

                if (isShift && handlingItem && isOwner.get(player.getUniqueId()) && !shop.isOpen())
                { //applying price
                    ShopItem shopItem = ShopItemManager.getShopItem(shop, uniqueItem);

                    if (shopItem != null)
                    {
                        if (chatEvent.getMessage().equalsIgnoreCase("cancel"))
                        {
                            handlingItem = false;
                            isInShop.put(player.getUniqueId(), false);
                            shop.removeItem(shopItem);
                            shopItem.removePrice();

                            player.getInventory().addItem(shopItem.getShopItem());
                        } else if (isInteger(chatEvent.getMessage()))
                        {
                            shop.removeItem(shopItem);

                            shopItem.
                                    addPrice(
                                    Integer.parseInt(chatEvent.getMessage()),
                                    shop);
                            shop.addItemWithPrice(shopItem);

                            player.getInventory().remove(shopItem.getShopItem());

                            ShopItemManager.addShopItems(shop, shopItem);
                            uniqueItems.put(shopItem.getShopItem(), shopItem.getUniqueItem());

                            player.openInventory(shop.getInventory(playerWrapper));

                            isInShop.put(player.getUniqueId(), true);
                            handlingItem = false;

                        } else if (!chatEvent.getMessage().equalsIgnoreCase("cancel"))
                        {
                            player.sendMessage(ChatColor.DARK_RED + "Please type in a valid Integer");
                        }
                    }
                } else if (isRight && handlingItem && isOwner.get(player.getUniqueId()) && !shop.isOpen())
                { //editing price
                    ShopItem shopItem = ShopItemManager.getShopItem(shop, uniqueItems.get(clickedItem));

                    if (shopItem != null)
                    {
                        if (chatEvent.getMessage().equalsIgnoreCase("cancel"))
                        {
                            handlingItem = false;
                            isInShop.put(player.getUniqueId(), false);
                        } else if (isInteger(chatEvent.getMessage()))
                        {
                            shop.removeItemWithPrice(shopItem);
                            shopItem.removePrice();

                            shopItem.addPrice(Integer.parseInt(chatEvent.getMessage()), shop);
                            shop.addItemWithPrice(shopItem);

                            player.openInventory(shop.getInventory(playerWrapper));

                            isInShop.put(player.getUniqueId(), true);
                            handlingItem = false;

                        } else if (!chatEvent.getMessage().equalsIgnoreCase("cancel"))
                        {
                            player.sendMessage(ChatColor.DARK_RED + "Please type in a valid Integer");
                        }
                    }
                }
            }
        }
        if (handlingName.get(player.getUniqueId()) != null)
        {
            if (handlingName.get(player.getUniqueId()))
            {
                IShop shop = Vowed.getShopManager().getShop(player);

                if (chatEvent.getMessage().length() <= 16)
                {
                    shop.changeName(chatEvent.getMessage());
                    handlingName.put(player.getUniqueId(), false);
                }
            }
        }
    }


    public static boolean isInteger(String s)
    {
        return isInteger(s, 10);
    }

    public static boolean isInteger(String s, int radix)
    {
        if (s.isEmpty()) return false;
        for (int i = 0; i < s.length(); i++)
        {
            if (i == 0 && s.charAt(i) == '-')
            {
                if (s.length() == 1) return false;
                else continue;
            }
            if (Character.digit(s.charAt(i), radix) < 0) return false;
        }
        return true;
    }

    public void closeInventories(Inventory inventory)
    {
        Iterator<HumanEntity> iterator = inventory.getViewers().iterator();
        while (iterator.hasNext())
        {
            HumanEntity humanEntity = iterator.next();
            iterator.remove();
            humanEntity.closeInventory();
        }
    }

    public void saveTransaction(PlayerWrapper buyer, PlayerWrapper seller, ShopItem shopItem)
    {
        try
        {
            File parentFolder = new File("C:\\Users\\JPaul\\Desktop\\Server\\plugins\\VowedCore\\Transactions");
            if (!parentFolder.exists())
            {
                parentFolder.mkdirs();
            }

            File buyerFolder = new File("C:\\Users\\JPaul\\Desktop\\Server\\plugins\\VowedCore\\Transactions\\" + buyer.getPlayer().getUniqueId());
            if (!buyerFolder.exists())
            {
                buyerFolder.mkdir();
            }

            File sellerFolder = new File("C:\\Users\\JPaul\\Desktop\\Server\\plugins\\VowedCore\\Transactions\\" + seller.getPlayer().getUniqueId());
            if (!sellerFolder.exists())
            {
                sellerFolder.mkdir();
            }

            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy_hh-mm-ss-a");
            Date date = new Date();

            String buyerPath = "C:\\Users\\JPaul\\Desktop\\Server\\plugins\\VowedCore\\Transactions\\" + buyer.getPlayer().getUniqueId() + "\\" + buyer.getPlayer().getUniqueId().toString() + "_" + dateFormat.format(date) + ".transactionDATA".replaceAll(" ", "");
            File buyerFile = new File(buyerPath);
            if (!buyerFile.exists())
            {
                buyerFile.createNewFile();
            }

            String sellerPath = "C:\\Users\\JPaul\\Desktop\\Server\\plugins\\VowedCore\\Transactions\\" + seller.getPlayer().getUniqueId() + "\\" + seller.getPlayer().getUniqueId().toString() + "_" + dateFormat.format(date) + ".transactionDATA".replaceAll(" ", "");
            File sellerFile = new File(sellerPath);
            if (!sellerFile.exists())
            {
                sellerFile.createNewFile();
            }

            int buyerBefore = (int) (buyer.getMoney().getAmount() + get(buyer, shopItem));
            int sellerBefore = (int) (seller.getMoney().getAmount() - get(seller, shopItem));

            FileWriter buyerFileWriter = new FileWriter(buyerFile, true);
            PrintWriter buyerWriter = new PrintWriter(buyerFileWriter);
            buyerWriter.println("Buyer: " + buyer.getPlayer().getName());
            buyerWriter.println("Seller: " + seller.getPlayer().getName());
            buyerWriter.println("Item: " + shopItem.getShopItem());
            buyerWriter.println("Item Dwarf Price: " + shopItem.getDwarfINTPrice());
            buyerWriter.println("Item Elf Price: " + shopItem.getElfINTPrice());
            buyerWriter.println("Item Human Price: " + shopItem.getHumanINTPrice());
            buyerWriter.println("Buyer (" +  buyer.getPlayer().getName() + ") money before: " + buyerBefore);
            buyerWriter.println("Seller (" +  seller.getPlayer().getName() + ") money before: " + sellerBefore);
            buyerWriter.println("Buyer (" + buyer.getPlayer().getName() + ") money after: " + buyer.getMoney().getAmount());
            buyerWriter.println("Seller (" + seller.getPlayer().getName() + ") money after: " + seller.getMoney().getAmount());
            buyerWriter.println("");

            buyerFileWriter.close();
            buyerWriter.close();

            FileWriter sellerFileWriter = new FileWriter(sellerFile, true);
            PrintWriter sellerWriter = new PrintWriter(sellerFileWriter);
            sellerWriter.println("Buyer: " + buyer.getPlayer().getName());
            sellerWriter.println("Seller: " + seller.getPlayer().getName());
            sellerWriter.println("Item: " + shopItem.getShopItem());
            sellerWriter.println("Item Dwarf Price: " + shopItem.getDwarfINTPrice());
            sellerWriter.println("Item Elf Price: " + shopItem.getElfINTPrice());
            sellerWriter.println("Item Human Price: " + shopItem.getHumanINTPrice());
            sellerWriter.println("Buyer (" +  buyer.getPlayer().getName() + ") money before: " + buyerBefore);
            sellerWriter.println("Seller (" +  seller.getPlayer().getName() + ") money before: " + sellerBefore);
            sellerWriter.println("Buyer (" + buyer.getPlayer().getName() + ") money after: " + buyer.getMoney().getAmount());
            sellerWriter.println("Seller (" + seller.getPlayer().getName() + ") money after: " + seller.getMoney().getAmount());
            sellerWriter.println("");

            sellerFileWriter.close();
            sellerWriter.close();
        } catch (IOException e)
        {
            e.printStackTrace();
        }

    }

    public int get(PlayerWrapper playerWrapper, ShopItem shopItem)
    {
        switch (playerWrapper.getRace().getType())
        {
            case DWARF:
                return shopItem.getDwarfINTPrice();

            case ELF:
                return shopItem.getElfINTPrice();

            case HUMAN:
                return shopItem.getHumanINTPrice();
        }

        throw new NullPointerException();
    }
}

