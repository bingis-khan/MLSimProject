# MLSimProject
FUGGGG :DDD

Projekt przedstawia symulację agentową, której celem jest pokazanie poczynań przedstawicieli danego gatunku. Osobniki znajdują się na planszy z jedzeniem, ich czyny są sterowane przez algorytm genetyczny.Pożądane osobniki to takie które przemieszczają się, zdobywając przy tym jedzenie w sposób wymagający jak najmniejszej utraty energii. Dążymy do uzyskania rezulatatu w postaci otrzymywania coraz to lepszych zasad (obiekt typu Rule), lepiej przystosowanych do środowiska jakim jest w naszym wypadku plansza z pożywieniem.

Na podstawie rezulatów uzyskanych w pierwszym etapie symulacji zostaje policzona sprawność- indywidualnie każdemu z obiektów typu GeneticAlgorithm (który w istocie jest listą zasad) -to ona ma wpływ na dalsze losy obiektu. Na przekazanie swoich genów nowemu pokoleniu szansę ma jednak każdy GA, gdyż za metodę selekcji nasz projekt przyjmuje ruletkę- tzn. prawdopodobieństwo wylosowania do operacji krzyżowania jest proporcjonalne do wartości sprawności (tj. wartości fitness). 
Nowa populaja powstaje poprzez wywołanaie metody crossover tyle razy, ile wynosi połowa wszystkich obiektów GA. Za każdym wykonaniem metody do nowej populacji mogą przejść 2 obiekty będące mieszanką cech obiektów rodzicielskich. Dla wylosowanego punktu krzyżowania obiekty potomne to odpowiednio geny przed tym punktem rodzica pierwszego i geny znajdujące się po tym punkcie rodzica drugiego. 
Wszystko dodatkowo wzbogacone jest o element niepewności związany z prawdopodobieństwem wykonania tej operacji. Zajdzie ona tylko wówczas, gdy losowa liczba nie przekroczy odpowiednio ustalonego progu. Tak więc po zapełnieniu nowej populacji przez osobniki potomne, przychodzi czas na dopełnienie jej do rozmiarów poprzedniczki. Dzieję się to poprzez wybranie (ponownie metodą ruletki) obiektów GA, które zostaną w całości skopionawe dalej. 
Ponad to każdy ma szansę na mutacje, z odpowiednim niewielkim prawdopodobieństwem. Mutacja polega na zamianie w losowo wybranej zasadzie, jednej wartości prawda fałsz na przeciwną. Następnie ciąg wydarzeń powtarza się, dzięki czemu dążymy aby nowe populacje zawierały co raz to lepsze algorytmy genetyczne.

## Podstawowa pętla sterowania
- echo CZY TO DZIAŁA?!
- initialize population 1 4 100  // Tworzy losową populację o wielkości 100 z wielkością zasad <1, 4>.
- selector bob 50 49 1  // Selektor typu 'bob' z parametrami selection=50, crossover=49, mutation=1. (Są to procenty całej populacji.)
- set parameters 100 100 3.5  // Parametry symulacji. 100 - długość, 100 - szerokość, 3.5 - jedzenie generowane na jednego agenta.
- run simulation             // Puszcza symulację i zapisuje rezultaty ('fitness').
- update                    // Bierze ostatnie rezultaty i tworzy za pomocą nich i wybranego selektora nową populację, która zastępuje starą.

## Trening
- batch 100 10  // Na 100 rund, bierze średnią z 10 symulacji, i na tej podstawie wybiera nową populację.

## Sterowanie symulacją krok po kroku
- step // Tworzy NOWĄ symulację i wchodzi w tryb sterowania symulacją.
- step // W tym trybie step będzie oznaczało jeden krok.
- full // Wyświetla całą planszę na konsoli.
- finish // Kończy symulację i zapisuje wyniki.
- end  // Kończy symulację bez zapisu.

## GUI
- gui  // Gdy jest aktywna symulacja (tryb sterowania symulacją), otwiera okno z graficzną reprezentacją symulacji.
       // Naciśnij spację, by spauzować. Zamknij okno, by wrócić do programu.
       
## Makra
- define super_reatard echo zupa # echo z kakaem # echo stoopid // Tworzenie nowego makra o nazwie 'super_reatard'. Każda komenda jest podzielona znakiem '#'.
- m retard // m wywołuje makro o nazwie 'super_reatard'. Wywołuje to zdefiniowane wcześniej makro.

## Zapisywanie
- save epicween // Zapisuje populację w pliku o nazwie 'epicween'.
- load epicween // Ładuje populację z pliku epicween.

## POMOCY!!!! (czyli więcej jest tych komend)
- help // Wyświetla wszystkie komendy.
- help help // Wyświetla dokładny opis komendy o tej nazwie (tych nazwach), w tym przypadku 'help', samej siebie.
