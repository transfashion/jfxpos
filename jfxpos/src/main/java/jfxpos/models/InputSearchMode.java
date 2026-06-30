package jfxpos.models;

public enum InputSearchMode {

	BARCODE("Input Barcode", "imgbarcode"),
	ART("Search Article", "imgarticle");

	// Variabel untuk menyimpan properti
	private final String prompt;
	private final String image;

	// Constructor harus menerima kedua parameter tersebut
	InputSearchMode(String prompt, String image) {
		this.prompt = prompt;
		this.image = image;
	}

	// Getter untuk mengambil teks prompt
	public String getPrompt() {
		return prompt;
	}

	// Getter untuk mengambil nama/path image
	public String getImage() {
		return image;
	}
}
