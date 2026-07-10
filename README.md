# JFX POS

JFX POS adalah aplikasi desktop **Point of Sale (POS)** berbasis **JavaFX** yang dibangun menggunakan **Java 25** dan database **FirebirdSQL 5.0.4**. Project ini dirancang dengan struktur multi-modul menggunakan build tool **Gradle** dan menerapkan arsitektur bersih untuk efisiensi serta kemudahan pemeliharaan (*maintainability*).

Server/backend API untuk POS ini dikembangkan sebagai project terpisah di repositori [jfxpos-api](https://github.com/transfashion/jfxpos-api).

---

## 🛠️ Arsitektur & Struktur Modul

Project ini dibagi menjadi 4 modul utama:

1. **`launcher`**  
   Titik awal (*start point*) jalannya aplikasi. Modul ini bertanggung jawab atas proses pemeriksaan dan pembaruan otomatis (*autoupdate*), khususnya mengunduh/mengganti file `jfxpos.jar` utama sebelum menjalankan aplikasi POS.
2. **`jfxpos`**  
   Modul aplikasi utama berisi UI POS, FXML Controller, logika bisnis, dan integrasi database.
3. **`jfxpossyn`**  
   Modul sinkronisasi data independen yang murni berjalan untuk keperluan data tanpa bergantung pada JavaFX / UI library, sehingga dapat digunakan kembali oleh modul lain.
4. **`jfxposecr`**  
   Modul penanganan ECR (Electronic Cash Register) / integrasi EDC independen yang murni berjalan untuk keperluan integrasi perangkat luar tanpa bergantung pada JavaFX / UI library, sehingga dapat digunakan kembali oleh modul lain.

Untuk detail pedoman pengembangan, pola desain (Repository, Model, Data Contract), dan aturan arsitektur lainnya, silakan merujuk ke [GEMINI.md].

---

## 💻 Panduan Perintah Gradle & Pengembangan

Berikut adalah ringkasan perintah Gradle yang sering digunakan untuk proses development, build, hingga pembuatan installer.

### 1. Membersihkan Project (Clean)
Menghapus direktori `build` yang dihasilkan sebelumnya:
```bash
./gradlew clean
```

### 2. Membangun Ulang Project (Rebuild)
Membersihkan dan mengompilasi ulang seluruh modul:
```bash
./gradlew clean build
```

### 3. Menjalankan Aplikasi (Run)
Menjalankan aplikasi secara langsung dari source code:
```bash
# Menjalankan langsung
./gradlew run

# Build terlebih dahulu lalu jalankan
./gradlew build run

# Bersihkan, build, lalu jalankan
./gradlew clean build run
```

### 4. Melakukan Debugging
Untuk menjalankan aplikasi dalam mode debug:
```bash
./gradlew :launcher:run --debug-jvm
```
*Setelah menjalankan perintah di atas, silakan hubungkan dengan debugger pada VS Code.*

Atau Anda juga dapat menggunakan opsi parameter berikut:
```bash
./gradlew run -Pdebug
```

### 5. Build Executable Image (EXE / Distributable)
Membuat aplikasi mandiri dalam bentuk *executable image*:
```bash
./gradlew :launcher:jpackageImage
```

### 6. Build Installer
Membuat paket *installer* aplikasi (sesuai OS target):
```bash
./gradlew :launcher:jpackage
```

---

## 🖥️ Desktop Entry (Linux)

Bagi pengguna Linux, Anda dapat membuat file desktop entry agar aplikasi dapat diluncurkan melalui launcher aplikasi desktop. Buat file `jfxpos.desktop` (misalnya di `~/.local/share/applications/`) dengan konfigurasi berikut:

```ini
[Desktop Entry]
Type=Application
Name=JFXPOS
Comment=JavaFX Point of Sale Application
Exec=/home/agung/Development/transfashion/jfxpos/build/jfxpos/bin/jfxpos
Icon=/home/agung/Development/transfashion/jfxpos/build/jfxpos/lib/jfxpos.png
Terminal=false
Categories=Office;Finance;
StartupNotify=true
```

> [!NOTE]  
> Sesuaikan path pada `Exec` dan `Icon` dengan lokasi instalasi atau build path riil pada mesin Anda.
