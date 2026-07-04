package jfxpos.controller;

import java.math.BigDecimal;
import java.util.logging.Logger;
import jfxpos.models.Trx;
import jfxpos.repository.ChannelRepository;
import jfxpos.repository.PromoItemRepository;
import jfxpos.repository.PromoPaymentRepository;
import jfxpos.repository.PromoNextTxRepository;

public class SaleService {
    private static final Logger logger = Logger.getLogger(SaleService.class.getName());
    private final ChannelRepository channelRepo = new ChannelRepository();
    private final PromoItemRepository promoItemRepo = new PromoItemRepository();
    private final PromoPaymentRepository promoPaymentRepo = new PromoPaymentRepository();
    private final PromoNextTxRepository promoNextTxRepo = new PromoNextTxRepository();

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
