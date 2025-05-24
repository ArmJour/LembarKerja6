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

- **src/**  
  - **kasus1/**  
    - `SquareTask.java`  
    - `AppRunCase1.java`  
  - **kasus2/**  
    - `Inventory.java`  
    - `OrderProcessor.java`  
    - `ReturnProcessor.java`  
    - `AppRunCase2.java`
- `.gitignore`  
- `README.md`


---

## Cara Menjalankan

1. **Clone repositori**  
   ```bash
   git clone https://github.com/username/nama-repo.git
   cd nama-repo

