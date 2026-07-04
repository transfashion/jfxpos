package jfxpos.repository;

import jfxpos.models.PromoNextTx;
import jfxpos.util.PosLogger;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class PromoNextTxRepository {
	private static final Logger logger = PosLogger.createLogger(PromoNextTxRepository.class.getName());

	public List<PromoNextTx> findAll() {
		List<PromoNextTx> promos = new ArrayList<>();
		// Mock data for Next Transaction promos
		promos.add(new PromoNextTx(1, "Voucher Belanja 50k untuk Transaksi Berikutnya"));
		promos.add(new PromoNextTx(2, "Diskon 15% untuk Transaksi Berikutnya (Min Belanja 200k)"));
		return promos;
	}

	public PromoNextTx findById(int id) {
		return findAll().stream()
				.filter(p -> p.getId() == id)
				.findFirst()
				.orElse(null);
	}

	public int getActivePromoCount() {
		return findAll().size();
	}
}
