package jfxpos.controller;

import java.math.BigDecimal;
import java.util.logging.Logger;
import jfxpos.models.Trx;
import jfxpos.models.TrxItem;
import jfxpos.models.InputSearchMode;
import jfxpos.repository.ChannelRepository;
import jfxpos.repository.PromoItemRepository;
import jfxpos.repository.PromoPaymentRepository;
import jfxpos.repository.PromoNextTxRepository;
import jfxpossyn.model.Item;
import jfxpossyn.repository.ItemRepository;

public class SaleService {
    private static final Logger logger = Logger.getLogger(SaleService.class.getName());
    private final ChannelRepository channelRepo = new ChannelRepository();
    private final PromoItemRepository promoItemRepo = new PromoItemRepository();
    private final PromoPaymentRepository promoPaymentRepo = new PromoPaymentRepository();
    private final PromoNextTxRepository promoNextTxRepo = new PromoNextTxRepository();
    private final ItemRepository itemRepo = new ItemRepository();

    public TrxItem processItemSearch(InputSearchMode searchMode, String searchText) throws Exception {
        Item item = null;
        if (searchMode == InputSearchMode.BARCODE) {
            item = findItemByBarcode(searchText);
        } else if (searchMode == InputSearchMode.ART) {
            java.util.List<Item> items = findItemsByArticle(searchText);
            if (items != null && !items.isEmpty()) {
                item = items.get(0); // Default to first item for backward compatibility / basic search
            }
        }
        return createTrxItemFromItem(item);
    }

    public Item findItemByBarcode(String barcode) throws Exception {
        return itemRepo.findByBarcode(barcode);
    }

    public java.util.List<Item> findItemsByArticle(String article) throws Exception {
        return itemRepo.findAllByArticle(article);
    }

    public TrxItem createTrxItemFromItem(Item item) {
        if (item == null) {
            return null;
        }

        TrxItem trxItem = new TrxItem();
        trxItem.setItemId(item.getItemId());
        trxItem.setItemArt(item.getItemArt());
        trxItem.setItemCol(item.getItemCol());
        trxItem.setItemSize(item.getItemSize());
        trxItem.setItemDescr(item.getItemDescr());
        trxItem.setItemPriceGross(item.getItemPriceGross());
        trxItem.setItemPrice(item.getItemPrice());
        trxItem.setItemDisc(item.getItemDisc());
        trxItem.setItemIsSpecialPrice(item.isItemIsSpecialPrice());

        // Default qty is 1
        trxItem.setQty(1);

        // Recalculate line totals
        BigDecimal priceNett = trxItem.getItemPrice().subtract(trxItem.getDiscValue());
        trxItem.setPriceGross(trxItem.getItemPriceGross());
        trxItem.setPriceNett(priceNett);
        trxItem.setSubtotalGross(trxItem.getPriceGross().multiply(BigDecimal.valueOf(trxItem.getQty())));
        trxItem.setSubtotalDiscount(trxItem.getDiscValue().multiply(BigDecimal.valueOf(trxItem.getQty())));
        trxItem.setSubtotalNett(trxItem.getPriceNett().multiply(BigDecimal.valueOf(trxItem.getQty())));
        trxItem.setTotal(trxItem.getSubtotalNett());
        trxItem.setGrandTotal(trxItem.getTotal());

        return trxItem;
    }

    public Trx startNewTransaction() {
        Trx newTrx = new Trx();
        newTrx.setSubtotal(BigDecimal.ZERO);
        newTrx.setQty(0);

        try {
            java.util.List<jfxpos.models.Channel> channels = channelRepo.findAll();
            if (channels != null && !channels.isEmpty()) {
                jfxpos.models.Channel smallestIdChannel = channels.stream()
                        .min(java.util.Comparator.comparingInt(c -> c.getId()))
                        .orElse(null);
                if (smallestIdChannel != null) {
                    newTrx.setChannelId(smallestIdChannel.getId());
                    newTrx.setChannelName(smallestIdChannel.getChannelName());
                }
            }
        } catch (Exception e) {
            logger.severe("Failed to load default channel for new transaction: " + e.getMessage());
        }

        try {
            int activePromoItemCount = promoItemRepo.getActivePromoCount();
            int activePromoPaymCount = promoPaymentRepo.getActivePromoCount();
            int activePromoNextTxCount = promoNextTxRepo.getActivePromoCount();

            newTrx.setPromoItemCount(activePromoItemCount);
            newTrx.setPromoPaymCount(activePromoPaymCount);
            newTrx.setPromoNextTxCount(activePromoNextTxCount);

            if (activePromoItemCount > 0) {
                newTrx.setPromoItem(promoItemRepo.getDefaultPromo());
            } else {
                newTrx.setPromoItem(null);
            }

            if (activePromoPaymCount > 0) {
                newTrx.setPromoPaym(promoPaymentRepo.getDefaultPromo());
            } else {
                newTrx.setPromoPaym(null);
            }

            if (activePromoNextTxCount > 0) {
                newTrx.setPromoNextTx(promoNextTxRepo.getDefaultPromo());
            } else {
                newTrx.setPromoNextTx(null);
            }
        } catch (Exception e) {
            logger.severe("Failed to load active promo counts for new transaction: " + e.getMessage());
        }

        return newTrx;
    }
}
