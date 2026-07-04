package jfxpos.repository;

import jfxpos.models.PromoItem;
import jfxpos.util.PosLogger;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class PromoItemRepository {
	private static final Logger logger = PosLogger.createLogger(PromoItemRepository.class.getName());

	public List<PromoItem> findAll() {
		List<PromoItem> promos = new ArrayList<>();
		// Mock data because table is not defined in DB yet
		promos.add(new PromoItem(1, "Promo Diskon 10% (Min Belanja 100k)"));
		promos.add(new PromoItem(2, "Promo Cashback 20k (Khusus Member)"));
		promos.add(new PromoItem(3, "Promo Buy 1 Get 1 Free"));
		return promos;
	}

	public PromoItem findById(int id) {
		return findAll().stream()
				.filter(p -> p.getId() == id)
				.findFirst()
				.orElse(null);
	}
}
