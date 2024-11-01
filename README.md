# Ecosystem Simulation Project

## Краткое описание проекта

Этот проект представляет симуляцию экосистемы,в которой существуют представители животных и растений, а также зависимости данных популяций от ресурсов экосистемы:
 (температура, влажность, вода, радиация и ветер).
 
## Управление данными экосистем


Проект поддерживает следующие основные возможности:
1) Загрузка уже существующих экосистем: 
   Эта функция доступна только в случае существования ранее созданных экосистем. Экосистемы хранятся в директории storage_ecosystem, которая будет создана при условии ее отсутствия.
   В данной директории и будет хранится информация о ранее добавленных экосистемах в виде директории с названиями экосистем. В данных именованных директориях будут находится три текстовых файла:
   -ecosysem.txt (актуальные ресурсы экосистемы)
   -plants.txt (актуальные растения, их количество и потребности каждой популяции)
   -animals.txt (актуальные животные, их количество, потребность и тип питания)
   -log.txt (информация о событиях экосистемы)

3) Создание новой экосистемы: 
   Экосистема признается созданной только в том случае, если пользователь, когда поработал с ней, согласился на ее дальнейшее сохранение и определил ей имя.
     
4) Удаление экосистемы: 
   Из списка предоставленных экосистем необходимо ввести номер экосистемы, которую необходимо удалить. После этого директория соответствующей экосистемы будет удалена.
    

## Управление выбранной экосистемы

В программном средстве реализованы следующие функции управления экосистемы:

1) Запуск симуляции(1 день жизни симуляции):
   Данная функция предоставляет информацию о всем случившемся за один день в экосистеме. Будет сообщено о причинах гибели и о количестве погибших особей в этот день, а также о количестве особей, которые
   появились путем размножения. Шанс размножения животных и растений увеличивает с количеством соблюденных условий ресурсов и выживания (см. раздел животные/растения). Все события экосистемы записываются
   в log.

2) Добавление новых животных:
   Данная функция доступна лишь в том случае, если в текущей экосистеме присутствует хотя бы один экземпляр растений, т.к без этого пищевая цепь не сможет замкнуться(хотя бы одно животное должно быть
   травоядным). Создание животного полностью идентично созданию растения (см. растения), за исключением того, что у животного появляется пункт "пищи", где пользователю необходимо ввести имя существа,
   которым будет питаться данное животное (т.е другое животное или растение). Добавление будет записано в log.

3) Добавление новых растений:
   Данная функция реализует добавление популяции растений в существующую экосистему. Можно добавлять только растения с уникальными именами. При добавлении растения пользователю необходимо ввести 
   "потребности" данного растения к ресурсам экосистемы (см. краткое описание проекта), а также количество экземпляров. В данном случае пользователю необходимо ввести минимальный и максимальный параметр 
   каждой потребности,который в последующем и будет влиять на выживание данной популяции в конкретной экосистеме. Добавление будет записано в log.

4) Показать состояние экосистемы:
   Показывает название популяции и количество экземпляров, живущих в ней на данный момент.

5) Показать доступные ресурсы:
   Показывает количественную характеристику текущих ресурсов экосистемы (см. краткое описание проекта).

6) Показать существующие популяции:
   Выводит в консоль подробную информацию про каждую популяцию: зависимости от каждого ресурса (минимальный / максимальный), название популяции, количество экземпляров.
   У популяций животных также будет показано, чем они питаются.

7) Изменить ресурсы экосистемы:
   Позволяет изменить определенный ресурс экосистемы, путем выбора номера ресурса и присваивания ему нового значения (пронумерованный список ресурсов будет выведен в консоль).
   Новое значение вводится с консоли.Изменения будут записаны в log.

8) Изменить размер популяции:
   Выводит в консоль пронумерованные названия популяций и присваивает новое значения размера популяции у выбранного экземпляра.
   Новое значение вводится с консоли.Изменения будут записаны в log.

9) Предсказание жизни экосистемы:
   Создает копию всех популяций и анализирует, способна ли она прожить хотя бы 100 дней. Процесс жизни этих популяций будет выведен в консоль. В случае, если хотя бы одна популяция умирает
   (количество ее представителей = 0), то функция выведет количество дней, которое смогла прожить популяция. Оригинальные данные при этом не пострадают. Иначе сообщит об успехе проживания.
   Записывается в log.

10) Выйти:
    Прекращение работы с экосистемой. В этом случае будет предложено сохранить данную экосистему. Если это новая экосистема, то будет предложено ввести ее название, что и будет названием
    директории, куда будут сохранены данные (см. управление данными экосистем). В случае, если директория с этой экосистемой уже создана (пользователь работает с ней не в первый раз), то
    будет предложено просто сохранить изменения уже в существующей директории. Возвращает к меню управления данными (см. управление данными экосистем).

## Структура проекта

- **EcosystemProcess** - содержит логику, управляющую ресурсами и требованиями среды.
- **Entity** - включает классы для представления животных, растений и популяций.
- **FileStorage** - отвечает за сохранение и загрузку экосистем, животных и растений в текстовые файлы.
- **Controllers** - управляет симуляцией, взаимодействует с пользователем для изменения экосистемы.
- **Factories** - отвечает за создание новых экземпляров растений и животных.
  
### Основные классы

- **Ecosystem**: управляет всеми популяциями и ресурсами, контролирует пищевые цепочки, обновления состояния и ведет логирование.
- **EcosystemController**: интерфейс для пользователя, содержит меню для взаимодействия с экосистемой, добавления популяций и изменения ресурсов.
- **Environment**: описывает условия окружающей среды для каждого вида.
- **Population**: представляет количество особей определенного вида (растения или животного), отвечает за цепи питания и факторы выживания/размножения.
- **UserInput**: отвечает за все консольные взаимодействия с пользователем.
- **BeingFactory**: отвечает за создание новых экземпляров растений и животных.
- **Animal**: содержит описание определенного животного.
- **Plant**: содержит описание определенного растения.

## Как использовать

1. Запустите симуляцию, выбрав создание новой или загрузку существующей экосистемы.
2. Добавьте существующие или создайте новые популяции животных и растений.
3. Изменяйте ресурсы экосистемы, чтобы адаптировать её к нужным условиям.
4. Запустите прогнозирование выживаемости для оценки, как долго продержится экосистема.

## Пример использования

```bash
Вы хотите:
1. Загрузить существующую экосистему
2. Создать новую экосистему
3. Удалить экосистему
4. Выйти из программы.

Введите выбор: 1
Выберите экосистему для загрузки: forestEco
1 - Запустить симуляцию
2 - Добавить популяцию животных
3 - Добавить популяцию растений
...
