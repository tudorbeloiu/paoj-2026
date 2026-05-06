# Sistem de Licitații (Auction System)

## 1.1 Lista acțiunilor / interogărilor posibile în sistem
Sistemul are 34 de interogări și operații (CRUD complet):

### User Management
1. **Înregistrează un cumpărător (Buyer)** - adaugă un utilizator nou cu un cont de balanță.
2. **Înregistrează un vânzător (Seller)** - adaugă un utilizator nou care poate vinde.
3. **Caută utilizator după email** - returnează datele unui utilizator specific.
4. **Șterge utilizator după ID** - elimină un utilizator din sistem și baza de date.
5. **Afișează toți utilizatorii** - listează toți utilizatorii înregistrați.
6. **Actualizează cumpărător** - modifică datele și balanța unui Buyer existent.
7. **Actualizează vânzător** - modifică datele unui Seller existent.

### Product & Category Management
8. **Adaugă o categorie nouă** - creează categorii pentru organizarea produselor.
9. **Afișează toate categoriile** - vizualizează categoriile disponibile.
10. **Adaugă un produs fizic** - asociază un produs fizic (cu an și greutate) unui Seller.
11. **Adaugă un produs digital** - asociază un produs digital (cu format și cheie) unui Seller.
12. **Afișează toate produsele** - vizualizează catalogul complet (JOIN pe tabelele de moștenire).
13. **Caută produs după ID** - returnează detaliile unui produs specific.
14. **Actualizează categorie** - modifică numele și descrierea.
15. **Șterge categorie** - elimină o categorie din sistem.
16. **Actualizează produs fizic** - modifică atributele de bază și cele specifice produselor fizice.
17. **Actualizează produs digital** - modifică atributele de bază și cele specifice produselor digitale.
18. **Șterge produs** - elimină un produs din sistem (cu verificare de tip).

### Auction Management
19. **Creează o licitație** - asociază un produs, un preț de pornire, tipul licitației și o durată.
20. **Afișează licitațiile active** - filtrează și vizualizează doar licitațiile în curs.
21. **Caută licitații după categorie** - filtrează licitațiile active în funcție de categorie.
22. **Caută licitații după vânzător** - afișează toate licitațiile deschise de un anumit Seller.
23. **Filtrează licitații după preț** - caută licitații aflate într-un anumit interval.
24. **Închide o licitație (Close)** - declanșează o TRANZACȚIE JDBC care updatează licitația, scade balanța și generează tranzacția finală.
25. **Anulează o licitație** - oprește o licitație activă și returnează banii ultimei oferte.
26. **Șterge o licitație** - elimină complet licitația din istoric.
27. **Raport licitații detaliate** - interogare SQL avansată (combinație între `auctions`, `products` și `categories`) pentru a afișa licitațiile cu detalii complete.

### Bid Management
28. **Plasează o ofertă** - cumpărătorii licitează o sumă (protejată tranzacțional).
29. **Afișează ofertele unei licitații** - vizualizează istoricul de "bidding".
30. **Raport activitate ofertanți** - interogare SQL avansată ce returnează cumpărătorii și numărul total de oferte plasate de fiecare, ordonat descrescător.

### Transactions
31. **Afișează toate tranzacțiile** - vizualizează istoricul global al tuturor achizițiilor.
32. **Afișează tranzacțiile unui utilizator** - vizualizează istoricul de achiziții/vânzări al unui cont specific.

### Notifications
33. **Afișează notificările necitite** - sistemul trimite notificări la supralicitare sau câștigare.
34. **Marchează notificarea ca citită** - updatează statusul notificării în baza de date.
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
8. `AuctionType` / `AuctionStatus` / `BidStatus` (Enum-uri)
