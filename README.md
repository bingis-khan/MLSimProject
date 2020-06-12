# MLSimProject
FUGGGG :DDD

Projekt przedstawia symulację agentową, której celem jest pokazanie poczynań przedstawicieli danego gatunku. Osobniki znajdują się na planszy z jedzeniem, ich czyny są sterowane przez algorytm genetyczny.Pożądane osobniki to takie które przemieszczają się, zdobywając przy tym jedzenie w sposób wymagający jak najmniejszej utraty energii. Dążymy do uzyskania rezulatatu w postaci otrzymywania coraz to lepszych zasad (obiekt typu Rule), lepiej przystosowanych do środowiska jakim jest w naszym wypadku plansza z pożywieniem.

Na podstawie rezulatów uzyskanych w pierwszym etapie symulacji zostaje policzona sprawność- indywidualnie każdemu z obiektów typu GeneticAlgorithm (który w istocie jest listą zasad) -to ona ma wpływ na dalsze losy obiektu. Na przekazanie swoich genów nowemu pokoleniu szansę ma jednak każdy GA, gdyż za metodę selekcji nasz projekt przyjmuje ruletkę- tzn. prawdopodobieństwo wylosowania do operacji krzyżowania jest proporcjonalne do wartości sprawności (tj. wartości fitness). 
Nowa populaja powstaje poprzez wywołanaie metody crossover tyle razy, ile wynosi połowa wszystkich obiektów GA. Za każdym wykonaniem metody do nowej populacji mogą przejść 2 obiekty będące mieszanką cech obiektów rodzicielskich. Dla wylosowanego punktu krzyżowania obiekty potomne to odpowiednio geny przed tym punktem rodzica pierwszego i geny znajdujące się po tym punkcie rodzica drugiego. 
Wszystko dodatkowo wzbogacone jest o element niepewności związany z prawdopodobieństwem wykonania tej operacji. Zajdzie ona tylko wówczas, gdy losowa liczba nie przekroczy odpowiednio ustalonego progu. Tak więc po zapełnieniu nowej populacji przez osobniki potomne, przychodzi czas na dopełnienie jej do rozmiarów poprzedniczki. Dzieję się to poprzez wybranie (ponownie metodą ruletki) obiektów GA, które zostaną w całości skopionawe dalej. 
Ponad to każdy ma szansę na mutacje, z odpowiednim niewielkim prawdopodobieństwem. Mutacja polega na zamianie w losowo wybranej zasadzie, jednej wartości prawda fałsz na przeciwną. Następnie ciąg wydarzeń powtarza się, dzięki czemu dążymy aby nowe populacje zawierały co raz to lepsze algorytmy genetyczne.

Projekt pozwala użytkownikowi na następujące działania:
- 
-

