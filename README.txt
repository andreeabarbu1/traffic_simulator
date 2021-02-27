 BARBU RODICA ANDREEA 332CC 
 TEMA 2 ALGORITMI PARALELI SI DISTRIBUITI
 TRAFFIC SIMULATOR

 	Pentru implementarea temei am folosit scheletul pus la dispozitie.
 Pentru fiecare tip de intersectie am creat o clasa specifica, 
 IntersectionFactory intorcandu-mi o instanta a fiecarui tip de intersectie.
 Fiecare car-thread va afisa mesajul propriu.
 	Cerinta 1: simple_semaphore
 Fiecare masina ajunge la intersectie, apoi asteapta perioada specificata in 
 fisierul de intrare (sleep(time)), urmand apoi sa iasa din intersectie.
	Cerinta 2: simple_n_roundabout
 In ReaderHandlerFactory se va citi de pe ultima linie a fisierului de intrare
 numarul maxim de masini ce pot intra in intersectie, respectiv timpul de 
 parcurgere.
 Intersectia va fi o instanta a clasei SimpleRoundabout. Pentru sincronizare
 se va folosi un semafor initializat cu numarul maxim de masini citit anterior.
 Fiecare masina va incerca sa faca acquire(), apoi asteapta deltaTime secunde
 si va face release(). Astfel, in intersectie vor intra maximum n masini la 
 un anumit moment.
 	Cerinta 3: simple_strict_1_car_roundabout
 	Intersectia va fi o instanta a clasei StrictRoundabout. Variabilele de
 instanta: numarul benzii, durata de traversare, un ArrayList de semafoare
 (fiecare banda va avea propriul semafor, initializat cu 1). Fiecare masina
 va ajunge la intersectie, apoi va incerca sa faca aquire() pe semaforul benzii
 ei, dupa ce a intrat in intersectie asteapta deltaTime secunde si apoi iese 
 din intersectie, apeland release(). In intersectie se va afla maxim o masina 
 din fiecare banda la un anumit moment de timp.
 	Cerinta 4: simple_strict_x_car_roundabout
 	Este folosita tot clasa StrictRoundabout, modalitatea de rezolvarea fiind
 asemanatoare cu cea de la exercitiul anterior. Fiecare banda va avea propriul
 semafor initializat cu numarul maxim de masini ce pot intra in intersectie.
 Se va folosi si o bariera pentru sincronizarea masinilor, folosita la fiecare
 pas: reached, selected, entered si exited. Masinile vor ajunge la intersectie,
 apoi vor incerca sa faca acquire(). Vor fi selectate si vor intra doar maxim
 n masini de pe fiecare banda, apoi vor astepta timpul specificat, iar apoi 
 vor face release(), astfel un nou interval de masini poate intra in intersectie.
 	Cerinta 5: simple_max_x_car_roundabout
 	Se va rezolva asemanator cu ultimele doua cerinte. Se va folosi clasa
 StrictRoundabout, iar ca metoda de sincronizare: semaforul. Fiecare banda
 are propriul semafor, initializat cu numarul maxim de masini ce pot traversa
 intersectia la un moment de timp. Fiecare masina va ajunge la intersectie,
 va incerca sa faca acquire(), dupa ce a intrat asteapta timpul necesar si va
 iesi - release(). In intersectie, la orice moment de timp, vor fi permise 
 maxim n masini ce vin dintr-o directie.
 	Cerinta 6: priority_intersection
 	Va fi folosita o instanta a clasei PriorityIntersection. Se vor utiliza
 doua semafoare, unul folosit pentru a permite unei singure masini de prioritate
 mica sa intre in intersectie (initializat cu 1), si unul pentru a verifica ca
 in intersectie nu se afla masini cu prioritate mare (initializat cu numarul de
 masini cu propritate mare + 1). Masinile cu prioritate mare vor intra in 
 intersectie, vor face acquire() pe cel de-al doilea semafor, apoi vor astepta
 deltaTime secunde pentru a parasi intersectia: release(). Ca o masina cu
 prioritate mica (1) sa intre in intersectie va incerca sa faca acquire() pe
 primul semafor, asigurandu-se ca nu se afla alta masina de prioritate 1 in
 intersectie si apoi va face acquire() pe cel de-al doilea semafor, incercand 
 sa blocheze toate permisele semaforului, i se va permite sa intre in
 intersectie doar daca nu se afla masini cu prioritate mare in ea.
 	Cerinta 7: crosswalk
 	Pentru rezolvarea cerintei se folosesc clasele CrossWalkIntersection si
 Pedestrian. Variabila de clasa pedestrians a clasei Main va fi instantiata
 cu numarul maxim de pietoni si timpul de executie. Pentru a retine masinile
 ce merg in cerc pana ce semaforul se va face verde se va folosi o coada
 (BlockingQueue). Pentru retinerea istoricului mesajelor afisate se va folosi 
 un hashmap (key: id_car, value: ultimul mesaj afisat). Pana ce se va termina
 timpul de executie, masinile vor trece daca semaforul este verde, la fiecare
 iteratie se verifica daca trec pietonii, in acest caz, semaforul va fi rosu
 pentru masini si acestea vor astepta sa treaca pietonii. Fiecare masina va fi
 adaugata in coada. Cand semaforul va fi din nou verde, masinile vor fi scoase,
 pe rand, din coada si vor iesi din intersectie.
 	Cerinta 8: simple_maintenance
 	Este utilizata o instanta a clasei SimpleMaintenance. Din fiecare sens vor
 circula maxim X masini, alternativ. Elemente de sincronizare folosite: o 
 bariera initializata cu X, doua semaforare (unul pentru prima banda - 
 initializat cu X - si unul pentru cea de-a doua banda - 0) si synchronized
 pentru regiunile critice. Masinile de pe  prima banda vor trece primele, acestea 
 vor face acquire() pe semaforul lor  (se va permite doar trecerea a X masini) si 
 release(X) pe semaforul 2. Se va realiza release(X) doar atunci cand au trecut X 
 masini pe prima banda. Cand masinile de pe a doua banda pot face acquire() la 
 propriul semafor vor urma acelasi procedeu. Astfel se va asigura trecerea a X
 masini din fiecare sens, alternativ.
 	Cerinta 9: complex_maintenance
 	Cerinta 10: railroad
 	Se va utiliza o instanta a clasei RailRoad. Pentru sincronizarea masinilor
 va fi folosita o bariera initializata cu numarul de masini. Iar pentru a retine
 ordinea acestora va fi utilizat un hashmap <key: lane, value: masina>.
 Cand o masina va ajunge la intersectie, aceasta va fi adaugata in coada
 propriului lane, astfel se va retine ordinea masinilor pe fiecare sens de mers.
 Toate masinile trebuie sa ajunga la intersectie, astfel va fi introdusa o 
 bariera. Dupa ce a trecut trenul, se va scoate fiecare masina din coada, 
 respectandu-se ordinea in care au ajuns pe fiecare sens. Ca metode de 
 sincronizare s-au folosit wait() si notifyAll(). Masinile care nu se afla in
 varful structurii de date asteapta, iar masina care va fi extrasa va anunta si
 restul thread-urilor. Acest proces repetandu-se pana ce vor iesi toate masinile.
