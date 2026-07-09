package jfxpos.controller;

import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import java.math.BigDecimal;
import jfxpos.models.Trx;

public class SaleViewBinder {

    public static void bind(SaleController controller, Trx newTrx, ChangeListener<BigDecimal> grandTotalListener) {
        newTrx.grandTotalProperty().addListener(grandTotalListener);
        
        if (controller.grandTotalValueLabel != null) {
            controller.grandTotalValueLabel.textProperty().bind(
                    Bindings.format("%,.0f", newTrx.grandTotalProperty()));
        }
        if (controller.grandTotalQtyLabel != null) {
            controller.grandTotalQtyLabel.textProperty().bind(
                    Bindings.format("%,d", newTrx.qtyProperty()));
        }
        if (controller.channelNameLabel != null) {
            controller.channelNameLabel.textProperty().bind(newTrx.channelNameProperty());
        }
        if (controller.customerIdLabel != null) {
            controller.customerIdLabel.textProperty().bind(
                    Bindings.createStringBinding(
                            () -> {
                                long cid = newTrx.customerIdProperty().get();
                                return cid == 0 ? "" : String.valueOf(cid);
                            },
                            newTrx.customerIdProperty()));
        }
        if (controller.customerTypeLabel != null) {
            controller.customerTypeLabel.textProperty().bind(
                    Bindings.createStringBinding(
                            () -> {
                                int ctid = newTrx.customerTypeIdProperty().get();
                                return ctid == 0 ? "" : String.valueOf(ctid);
                            },
                            newTrx.customerTypeIdProperty()));
        }
        if (controller.customerNameLabel != null) {
            controller.customerNameLabel.textProperty().bind(
                    Bindings.createStringBinding(
                            () -> {
                                String cname = newTrx.customerNameProperty().get();
                                return cname == null ? "" : cname;
                            },
                            newTrx.customerNameProperty()));
        }
        if (controller.salespersonNikLabel != null) {
            controller.salespersonNikLabel.textProperty().bind(
                    Bindings.createStringBinding(
                            () -> {
                                String nik = newTrx.salespersonNikProperty().get();
                                return nik == null ? "" : nik;
                            },
                            newTrx.salespersonNikProperty()));
        }
        if (controller.salespersonNameLabel != null) {
            controller.salespersonNameLabel.textProperty().bind(
                    Bindings.createStringBinding(
                            () -> {
                                String name = newTrx.salespersonNameProperty().get();
                                return name == null ? "" : name;
                            },
                            newTrx.salespersonNameProperty()));
        }
        if (controller.customerDiscountLabel != null) {
            controller.customerDiscountLabel.textProperty().bind(
                    Bindings.format("%,.0f", newTrx.customerDiscountProperty()));
        }
        if (controller.promoItemCountLabel != null) {
            controller.promoItemCountLabel.textProperty().bind(Bindings.format("%,d", newTrx.promoItemCountProperty()));
        }
        if (controller.promoItemIdLabel != null) {
            controller.promoItemIdLabel.textProperty().bind(Bindings.createStringBinding(
                    () -> {
                        long id = newTrx.promoItemIdProperty().get();
                        return id == 0 ? "" : String.valueOf(id);
                    },
                    newTrx.promoItemIdProperty()));
        }
        if (controller.promoItemNameLabel != null) {
            controller.promoItemNameLabel.textProperty().bind(newTrx.promoItemNameProperty());
        }
        if (controller.promoItemDescrLabel != null) {
            controller.promoItemDescrLabel.textProperty().bind(newTrx.promoItemDescrProperty());
        }

        if (controller.promoPaymCountLabel != null) {
            controller.promoPaymCountLabel.textProperty().bind(Bindings.format("%,d", newTrx.promoPaymCountProperty()));
        }
        if (controller.promoPaymIdLabel != null) {
            controller.promoPaymIdLabel.textProperty().bind(Bindings.createStringBinding(
                    () -> {
                        long id = newTrx.promoPaymIdProperty().get();
                        return id == 0 ? "" : String.valueOf(id);
                    },
                    newTrx.promoPaymIdProperty()));
        }
        if (controller.promoPaymNameLabel != null) {
            controller.promoPaymNameLabel.textProperty().bind(newTrx.promoPaymNameProperty());
        }
        if (controller.promoPaymDescrLabel != null) {
            controller.promoPaymDescrLabel.textProperty().bind(newTrx.promoPaymDescrProperty());
        }

        if (controller.promoNextTxCountLabel != null) {
            controller.promoNextTxCountLabel.textProperty()
                    .bind(Bindings.format("%,d", newTrx.promoNextTxCountProperty()));
        }
        if (controller.promoNextTxIdLabel != null) {
            controller.promoNextTxIdLabel.textProperty().bind(Bindings.createStringBinding(
                    () -> {
                        long id = newTrx.promoNextTxIdProperty().get();
                        return id == 0 ? "" : String.valueOf(id);
                    },
                    newTrx.promoNextTxIdProperty()));
        }
        if (controller.promoNextTxNameLabel != null) {
            controller.promoNextTxNameLabel.textProperty().bind(newTrx.promoNextTxNameProperty());
        }
        if (controller.promoNextTxDescrLabel != null) {
            controller.promoNextTxDescrLabel.textProperty().bind(newTrx.promoNextTxDescrProperty());
        }
    }

    public static void unbind(SaleController controller, Trx oldTrx, ChangeListener<BigDecimal> grandTotalListener) {
        oldTrx.grandTotalProperty().removeListener(grandTotalListener);
        if (controller.grandTotalValueLabel != null) {
            controller.grandTotalValueLabel.textProperty().unbind();
        }
        if (controller.grandTotalQtyLabel != null) {
            controller.grandTotalQtyLabel.textProperty().unbind();
        }
        if (controller.channelNameLabel != null) {
            controller.channelNameLabel.textProperty().unbind();
        }
        if (controller.customerIdLabel != null) {
            controller.customerIdLabel.textProperty().unbind();
        }
        if (controller.customerTypeLabel != null) {
            controller.customerTypeLabel.textProperty().unbind();
        }
        if (controller.customerNameLabel != null) {
            controller.customerNameLabel.textProperty().unbind();
        }
        if (controller.salespersonNikLabel != null) {
            controller.salespersonNikLabel.textProperty().unbind();
        }
        if (controller.salespersonNameLabel != null) {
            controller.salespersonNameLabel.textProperty().unbind();
        }
        if (controller.customerDiscountLabel != null) {
            controller.customerDiscountLabel.textProperty().unbind();
        }
        if (controller.promoItemCountLabel != null) {
            controller.promoItemCountLabel.textProperty().unbind();
        }
        if (controller.promoItemIdLabel != null) {
            controller.promoItemIdLabel.textProperty().unbind();
        }
        if (controller.promoItemNameLabel != null) {
            controller.promoItemNameLabel.textProperty().unbind();
        }
        if (controller.promoItemDescrLabel != null) {
            controller.promoItemDescrLabel.textProperty().unbind();
        }
        if (controller.promoPaymCountLabel != null) {
            controller.promoPaymCountLabel.textProperty().unbind();
        }
        if (controller.promoPaymIdLabel != null) {
            controller.promoPaymIdLabel.textProperty().unbind();
        }
        if (controller.promoPaymNameLabel != null) {
            controller.promoPaymNameLabel.textProperty().unbind();
        }
        if (controller.promoPaymDescrLabel != null) {
            controller.promoPaymDescrLabel.textProperty().unbind();
        }
        if (controller.promoNextTxCountLabel != null) {
            controller.promoNextTxCountLabel.textProperty().unbind();
        }
        if (controller.promoNextTxIdLabel != null) {
            controller.promoNextTxIdLabel.textProperty().unbind();
        }
        if (controller.promoNextTxNameLabel != null) {
            controller.promoNextTxNameLabel.textProperty().unbind();
        }
        if (controller.promoNextTxDescrLabel != null) {
            controller.promoNextTxDescrLabel.textProperty().unbind();
        }
    }

    public static void clear(SaleController controller) {
        if (controller.grandTotalValueLabel != null) {
            controller.grandTotalValueLabel.setText("0");
        }
        if (controller.grandTotalQtyLabel != null) {
            controller.grandTotalQtyLabel.setText("0");
        }
        if (controller.channelNameLabel != null) {
            controller.channelNameLabel.setText("");
        }
        if (controller.customerIdLabel != null) {
            controller.customerIdLabel.setText("");
        }
        if (controller.customerTypeLabel != null) {
            controller.customerTypeLabel.setText("");
        }
        if (controller.customerNameLabel != null) {
            controller.customerNameLabel.setText("");
        }
        if (controller.customerDiscountLabel != null) {
            controller.customerDiscountLabel.setText("0");
        }
        if (controller.promoItemCountLabel != null) {
            controller.promoItemCountLabel.setText("0");
        }
        if (controller.promoItemIdLabel != null) {
            controller.promoItemIdLabel.setText("");
        }
        if (controller.promoItemNameLabel != null) {
            controller.promoItemNameLabel.setText("");
        }
        if (controller.promoItemDescrLabel != null) {
            controller.promoItemDescrLabel.setText("");
        }
        if (controller.promoPaymCountLabel != null) {
            controller.promoPaymCountLabel.setText("0");
        }
        if (controller.promoPaymIdLabel != null) {
            controller.promoPaymIdLabel.setText("");
        }
        if (controller.promoPaymNameLabel != null) {
            controller.promoPaymNameLabel.setText("");
        }
        if (controller.promoPaymDescrLabel != null) {
            controller.promoPaymDescrLabel.setText("");
        }
        if (controller.promoNextTxCountLabel != null) {
            controller.promoNextTxCountLabel.setText("0");
        }
        if (controller.promoNextTxIdLabel != null) {
            controller.promoNextTxIdLabel.setText("");
        }
        if (controller.promoNextTxNameLabel != null) {
            controller.promoNextTxNameLabel.setText("");
        }
        if (controller.promoNextTxDescrLabel != null) {
            controller.promoNextTxDescrLabel.setText("");
        }
    }
}
