package jfxpos.controller;

import javafx.stage.Stage;
import java.util.logging.Logger;
import jfxpos.models.Trx;

public class SaleDialogManager {
    private static final Logger logger = Logger.getLogger(SaleDialogManager.class.getName());

    public static void openChannelDialog(Stage parent, Trx trx) {
        if (trx == null) return;
        try {
            jfxpos.views.ChannelDialog dialog = new jfxpos.views.ChannelDialog(parent);
            if (trx.getChannelId() != null) {
                dialog.selectChannelById(trx.getChannelId());
            }
            dialog.openDialog();
            jfxpos.models.Channel selected = dialog.getSelectedChannel();
            if (selected != null) {
                logger.info("Selected Channel: " + selected.getChannelName());
                trx.setChannelId(selected.getId());
                trx.setChannelName(selected.getChannelName());
            }
        } catch (Exception e) {
            logger.severe("Failed to open ChannelDialog: " + e.getMessage());
        }
    }

    public static void openCustRegisterDialog(Stage parent, Trx trx) {
        if (trx == null) return;
        try {
            jfxpos.views.CustRegisterDialog dialog = new jfxpos.views.CustRegisterDialog(parent);
            Long cid = trx.getCustomerId();
            if (cid != null && cid != 0) {
                dialog.setCustomerId(String.valueOf(cid));
            }
            String cname = trx.getCustomerName();
            if (cname != null && !"NONE".equals(cname)) {
                dialog.setCustomerName(cname);
            }
            Integer cgender = trx.getCustomerGender();
            if (cgender != null && cgender != 0) {
                dialog.setCustomerGender(String.valueOf(cgender));
            }
            java.time.LocalDate cbirth = trx.getCustomerBirthdate();
            if (cbirth != null) {
                dialog.setCustomerBirthdate(cbirth);
            }

            dialog.openDialog();
            if (dialog.isSaved()) {
                String newId = dialog.getCustomerId();
                String newName = dialog.getCustomerName();
                String newGender = dialog.getCustomerGender();
                java.time.LocalDate newBirth = dialog.getCustomerBirthdate();
                logger.info("Registered Customer: ID=" + newId + ", Name=" + newName + ", Gender=" + newGender
                        + ", Birthdate=" + newBirth);
                trx.setCustomerId(Long.parseLong(newId));
                trx.setCustomerName(newName);
                trx.setCustomerGender(newGender != null && !newGender.isEmpty() ? Integer.parseInt(newGender) : 0);
                trx.setCustomerBirthdate(newBirth);
            }
        } catch (Exception e) {
            logger.severe("Failed to open CustRegisterDialog: " + e.getMessage());
        }
    }

    public static void openCustSearchDialog(Stage parent, Trx trx) {
        if (trx == null) return;
        try {
            jfxpos.views.CustSearchDialog dialog = new jfxpos.views.CustSearchDialog(parent);
            dialog.openDialog();
            jfxpos.models.Customer selected = dialog.getSelectedCustomer();
            if (selected != null) {
                logger.info("Selected Customer: ID=" + selected.getCustomerId() + ", Name=" + selected.getCustomerName());
                trx.setCustomer(selected);
            }
        } catch (Exception e) {
            logger.severe("Failed to open CustSearchDialog: " + e.getMessage());
        }
    }

    public static void openPromoItemDialog(Stage parent, Trx trx) {
        if (trx == null) return;
        try {
            jfxpos.views.PromoItemDialog dialog = new jfxpos.views.PromoItemDialog(parent);
            if (trx.getPromoItemId() != null) {
                dialog.selectPromoItemById(trx.getPromoItemId().intValue());
            }
            dialog.openDialog();
            jfxpos.models.PromoItem selected = dialog.getSelectedPromoItem();
            if (selected != null) {
                logger.info("Selected Promo Item: " + selected.getName());
                trx.setPromoItem(selected);
                trx.setPromoItemCount(1);
            }
        } catch (Exception e) {
            logger.severe("Failed to open PromoItemDialog: " + e.getMessage());
        }
    }

    public static void openPromoPaymentDialog(Stage parent, Trx trx) {
        if (trx == null) return;
        try {
            jfxpos.views.PromoPaymentDialog dialog = new jfxpos.views.PromoPaymentDialog(parent);
            if (trx.getPromoPaymId() != null) {
                dialog.selectPromoPaymentById(trx.getPromoPaymId().intValue());
            }
            dialog.openDialog();
            jfxpos.models.PromoPayment selected = dialog.getSelectedPromoPayment();
            if (selected != null) {
                logger.info("Selected Promo Payment: " + selected.getNote());
                trx.setPromoPaym(selected);
                trx.setPromoPaymCount(1);
            }
        } catch (Exception e) {
            logger.severe("Failed to open PromoPaymentDialog: " + e.getMessage());
        }
    }

    public static void openPromoNextTxDialog(Stage parent, Trx trx) {
        if (trx == null) return;
        try {
            jfxpos.views.PromoNextTxDialog dialog = new jfxpos.views.PromoNextTxDialog(parent);
            if (trx.getPromoNextTxId() != null) {
                dialog.selectPromoNextTxById(trx.getPromoNextTxId().intValue());
            }
            dialog.openDialog();
            jfxpos.models.PromoNextTx selected = dialog.getSelectedPromoNextTx();
            if (selected != null) {
                logger.info("Selected Promo Next Tx: " + selected.getNote());
                trx.setPromoNextTx(selected);
                trx.setPromoNextTxCount(1);
            }
        } catch (Exception e) {
            logger.severe("Failed to open PromoNextTxDialog: " + e.getMessage());
        }
    }
}
