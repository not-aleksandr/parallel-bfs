# Запуск
## Тесты
``
./gradlew test
``
## Benchmark

``
./gradlew run
``

# Результаты benchmark

Измерения проводились на кубическом графе со стороной 490. 
Изначально проводилось несколько итераций для прогрева JVM, 
после по 5 итераций для каждой из реализаций bfs. 
Результаты замеров представлены в миллисекундах, ускорение представлено относительно последовательной реализации.

## Последовательная реализация

| №1    | №2    | №3    | №4    | №5    | Среднее | Ускорение |
|-------|-------|-------|-------|-------|---------|-----------|
| 26161 | 26362 | 26392 | 26262 | 26224 | 26280   | 1         |

## Параллельная реализация

| Размер блока | №1    | №2    | №3    | №4    | №5    | Среднее | Ускорение |
|--------------|-------|-------|-------|-------|-------|---------|-----------|
| 2000         | 11676 | 11720 | 11697 | 11670 | 11639 | 11680   | 2.25      |


