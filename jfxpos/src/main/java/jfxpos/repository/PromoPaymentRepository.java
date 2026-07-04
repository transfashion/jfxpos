package jfxpos.repository;

import jfxpos.models.PromoPayment;
import jfxpos.util.PosLogger;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class PromoPaymentRepository {
	private static final Logger logger = PosLogger.createLogger(PromoPaymentRepository.class.getName());

	public List<PromoPayment> findAll() {
		List<PromoPayment> promos = new ArrayList<>();
		// Mock data because table is not defined in DB yet
		promos.add(new PromoPayment(1, "Promo Bank BCA CC - Potongan 50k", new BigDecimal("50000")));
		promos.add(new PromoPayment(2, "Promo Bank Mandiri - Potongan 30k", new BigDecimal("30000")));
		promos.add(new PromoPayment(3, "Promo QRIS Gopay - Potongan 10k", new BigDecimal("10000")));
		return promos;
	}

	public PromoPayment findById(int id) {
		return findAll().stream()
				.filter(p -> p.getId() == id)
				.findFirst()
				.orElse(null);
	}

	public int getActivePromoCount() {
		return findAll().size();
	}
}
