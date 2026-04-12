# Sistem de Licitații (Auction System)

## 1.1 Lista acțiunilor / interogărilor posibile în sistem
Sistemul permite utilizatorilor să execute următoarele 24 de interogări și operații structurate logic:

### User Management
1. **Înregistrează un cumpărător (Buyer)** - adaugă un utilizator nou cu un cont de balanță.
2. **Înregistrează un vânzător (Seller)** - adaugă un utilizator nou care poate vinde produse.
3. **Caută utilizator după email** - returnează datele unui utilizator specific.
4. **Șterge utilizator după ID** - elimină un utilizator din sistem.
5. **Afișează toți utilizatorii** - listează toți utilizatorii înregistrați.

### Product & Category Management
6. **Adaugă o categorie nouă** - creează categorii pentru organizarea produselor.
7. **Afișează toate categoriile** - vizualizează categoriile disponibile.
8. **Adaugă un produs fizic** - asociază un produs fizic (cu an și greutate) unui Seller.
9. **Adaugă un produs digital** - asociază un produs digital (cu format și cheie de licență) unui Seller.
10. **Afișează toate produsele** - vizualizează catalogul complet de produse.
11. **Caută produs după ID** - returnează detaliile unui produs specific.

### Auction Management
12. **Creează o licitație (Create Auction)** - asociază un produs, un preț de pornire, tipul licitației și o durată.
13. **Afișează licitațiile active** - filtrează și vizualizează doar licitațiile în curs.
14. **Caută licitații după categorie** - filtrează licitațiile active în funcție de tipul produsului.
15. **Caută licitații după vânzător** - afișează toate licitațiile deschise de un anumit Seller.
16. **Filtrează licitații după preț** - caută licitații aflate într-un anumit interval de preț.
17. **Închide o licitație (Close Auction)** - finalizează o licitație și generează automat o tranzacție între câștigător și vânzător.
18. **Anulează o licitație** - oprește o licitație activă și returnează banii ultimei oferte plasate.

### Bid Management
19. **Plasează o ofertă (Place Bid)** - cumpărătorii licitează o sumă, care este blocată/scăzută din balanță.
20. **Afișează ofertele unei licitații** - vizualizează istoricul de "bidding" pentru o licitație.

### Transactions
21. **Afișează toate tranzacțiile** - vizualizează istoricul global al tuturor achizițiilor finalizate.
22. **Afișează tranzacțiile unui utilizator** - vizualizează istoricul specific de achiziții/vânzări al unui cont.

### Notifications
23. **Afișează notificările necitite** - sistemul trimite notificări la supralicitare (outbid) sau câștigare, afișate aici.
24. **Marchează notificarea ca citită** - schimbă statusul unei notificări din sistem.

---

## 1.2 Lista tipurilor de obiecte din domeniu
Sistemul modelează următoarele entități principale (inclusiv clase derivate care implementează concepte OOP precum moștenirea și polimorfismul):

1. `User` (Clasă abstractă, derivată în `Buyer` și `Seller`)
2. `Product` (Clasă abstractă, derivată în `PhysicalProduct` și `DigitalProduct`)
3. `Category`
4. `Auction`
5. `Bid`
6. `Transaction` (Clasă imutabilă - read-only)
7. `Notification`
8. `AuctionType` / `AuctionStatus` (Enum-uri)