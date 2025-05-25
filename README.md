# Lembar Kerja 06 – Implementasi Thread di Java

**Mata Kuliah**       : Pemrograman Lanjut (COM60024)  
**Kelas**             : C  
**Durasi Pengerjaan** : 3 Hari  
**Tanggal Pengumpulan**: 25 Mei 2025, 23:59 WIB  

---

## Deskripsi Singkat

Repositori ini berisi implementasi konsep _multithreading_ di Java berdasarkan dua studi kasus:

1. **Studi Kasus 1** – *Task Parallelism*  
   - Menjalankan 5 tugas independen (menghitung kuadrat angka) secara paralel  
   - Tiap tugas dijalankan dalam thread terpisah  
   - Tidak ada kebutuhan sinkronisasi karena tidak ada _shared resource_  

2. **Studi Kasus 2** – *Resource Synchronization*  
   - Sistem inventory toko online yang mengelola penambahan dan pengurangan stok  
   - Menangani kondisi race condition dan menjaga konsistensi data  
   - Menggunakan mekanisme sinkronisasi (`synchronized`, `Locks`, atau sejenisnya)

---

## Struktur Direktori

Tentu, mari kita perbaiki deskripsi untuk Kasus2 dalam struktur GitHub Anda, dengan menambahkan tag <br /> di setiap akhir baris komentar untuk memastikan pemformatan yang benar di README GitHub (jika itu yang Anda tuju dengan <br />).

Saya akan menyesuaikan deskripsi berdasarkan file-file Kasus2 yang telah Anda unggah dan diskusi kita sebelumnya mengenai CombinedGUIManager.java (yang akan saya sebut sebagai GUIManager.java dalam deskripsi ini untuk konsistensi dengan nama file yang Anda berikan di struktur awal, meskipun kode yang kita kembangkan mungkin bernama CombinedGUIManager).

Berikut adalah perbaikan deskripsi struktur GitHub secara keseluruhan:

├── src/<br />
│ ├── Kasus1/<br />
│ │ ├── AkarKuadrat.java    # Kelas tugas menghitung akar kuadrat<br />
│ │ ├── Discount.java       # Kelas tugas menghitung diskon<br />
│ │ ├── HitungKubik.java    # Kelas tugas menghitung pangkat tiga (kubik)<br />
│ │ ├── HitungPersegi.java  # Kelas tugas menghitung pangkat dua (kuadrat)<br />
│ │ ├── Modulus.java        # Kelas tugas menghitung sisa bagi (modulus)<br />
│ │ └── App.java            # Entry point menjalankan berbagai tugas perhitungan secara paralel<br />
│ │<br />
│ └── Kasus2/<br />
│ ├── App.java            # Entry point aplikasi manajemen inventaris dengan GUI<br />
│ ├── GUIManager.java     # Mengelola antarmuka pengguna (GUI) dengan Java Swing untuk interaksi manual dan simulasi stok<br />
│ ├── Inventory.java      # Model data untuk menyimpan dan mengelola stok produk<br />
│ ├── Order.java          # Representasi dan pemrosesan data pemesanan (mengurangi stok)<br />
│ ├── Product.java        # Kelas dasar untuk merepresentasikan detail produk<br />
│ ├── Return.java         # Representasi dan pemrosesan data pengembalian (menambah stok)<br />
│ ├── StockManager.java   # Mengelola logika bisnis terkait operasi stok dan inventaris<br />
│ └── StockUpdater.java   # Menjalankan operasi pemesanan dan pengembalian stok secara asinkron menggunakan thread<br />
│<br />
├── .gitignore<br />
└── README.md<br />

---

You can find the source code in the [src](https://github.com/ArmJour/LembarKerja6/tree/main/LembarKerja6/src)





