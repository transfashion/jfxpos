# Panduan Pengembangan AI - JFX POS

Dokumen ini berisi pedoman, struktur, dan aturan arsitektur untuk pengembangan aplikasi **Point of Sale (POS)** berbasis desktop menggunakan JavaFX. Semua agen AI yang berkontribusi pada repositori ini harus mematuhi aturan berikut.

---

## 1. Arsitektur & Teknologi

*   **Platform & Bahasa**: Java Desktop Application dengan **JavaFX** menggunakan **Java 21**.
*   **Build Tool / Framework**: **Gradle** (Multi-module project).
*   **Struktur Modul**:
    1.  `launcher`: Titik awal (*start point*) aplikasi. Modul ini bertanggung jawab untuk proses pemeriksaan dan pembaruan otomatis (*autoupdate*), khususnya mengunduh/mengganti file `jfxpos.jar` utama sebelum menjalankan aplikasi POS.
    2.  `jfxpos`: Modul aplikasi utama berisi UI POS, kontroler, logika bisnis, dan integrasi database.

---

## 2. Pengelolaan Database & Koneksi

*   **Database Engine**: **FirebirdSQL 5.0.4**.
*   **Connection Pooling**:
    *   Wajib menggunakan *Connection Pool* (misalnya HikariCP atau library connection pooling sejenis) untuk mengelola koneksi ke server FirebirdSQL agar efisien dan mencegah kebocoran koneksi (*connection leaks*).
    *   Jumlah maksimum koneksi dalam pool (*pool size*) **harus dapat dikonfigurasi** secara dinamis melalui Form Konfigurasi Aplikasi (`ConfigController` / `ConfigDialog`).
    *   Pengaturan pool size disimpan dan dimuat melalui kelas konfigurasi aplikasi (`AppConfig` & `AppConfigStore`).

---

## 3. Aturan Akses Data & Pola Desain (Design Patterns)

Untuk menjaga kualitas dan pemeliharaan kode (*maintainability*), ikuti aturan ketat berikut:

### A. Pola Repositori (Repository Pattern)
*   **Dilarang Keras** melakukan koneksi database, query SQL, atau operasi I/O database langsung di dalam kelas View, FXML Controller, atau Form.
*   Semua interaksi database harus dibungkus dalam kelas **Repository** (misal: `UserRepository`, `TransactionRepository`).
*   Controller hanya boleh berinteraksi dengan Repository untuk mengambil atau memanipulasi data.

### B. Representasi Data dengan Model
*   Sebisa mungkin representasikan baris/data dari tabel database menggunakan objek kelas **Model** (misal: kelas turunan dari `Model` seperti `User`).
*   Model bertindak sebagai penampung data (*data holder*) yang bersih dan terstruktur untuk dikirimkan antar layer (Repository $\leftrightarrow$ Controller $\leftrightarrow$ View).

### C. Kontrak Data (Data Contract)
*   Gunakan mekanisme **Data Contract** untuk mendefinisikan nama tabel dan kolom/field database.
*   **Wajib digabungkan di dalam kelas Model terkait** sebagai kelas bersarang statis (`public static final class Contract`). Hal ini dilakukan agar struktur schema DB dan model data berada di satu tempat yang kohesif, mengurangi jumlah file, dan memudahkan pemeliharaan kode.
*   Contoh implementasi kontrak data di dalam Model (misal kelas `User`):
    ```java
    public class User extends Model {
        public static final class Contract {
            public static final String TABLE_NAME = "USERS";
            
            public static final class Columns {
                public static final String ID = "USER_ID";
                public static final String USERNAME = "USERNAME";
                public static final String PASSWORD = "PASSWORD";
                public static final String ROLE = "ROLE";
            }
        }
        
        // fields, getters, setters...
    }
    ```

---

## 4. Panduan Implementasi Tambahan untuk AI

Saat menambahkan fitur baru atau melakukan perubahan:
1.  **Konfigurasi**: Jika memerlukan parameter koneksi database baru atau modifikasi pooling, perbarui record `AppConfig` di `jfxpos.config`, form FXML, serta `ConfigController` secara sinkron.
2.  **Modularitas**: Pastikan kelas penanganan *autoupdater* tetap berada di modul `launcher` dan tidak bergantung pada komponen UI atau pustaka internal dari `jfxpos`.
3.  **Kepatuhan Kode**: Selalu gunakan standard Java modern (Java 21), seperti records, pattern matching, dan penanganan resource otomatis (*try-with-resources*) untuk menutup koneksi database yang diperoleh dari pool.
