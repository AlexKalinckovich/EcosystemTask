import Controllers.EcosystemController;
import Controllers.UserInput;
import EcosystemProcess.Ecosystem;
import EcosystemProcess.EcosystemResources;
import Entity.Animal;
import Entity.LivingBeing;
import Entity.Plant;
import Entity.Population;
import Factories.BeingFactory;
import FileStorage.EcosystemStorageManager;
import FileStorage.LivingBeingStorageManager;

import java.util.ArrayList;
import java.util.List;

public class Main {

    private static final EcosystemStorageManager storageManager = new EcosystemStorageManager();
    private static final LivingBeingStorageManager livingBeingStorageManager = new LivingBeingStorageManager();

    private static void addExistingAnimal(List<Population<? extends LivingBeing>> populations, List<Population<Animal>> animals, Ecosystem ecosystem) {
        for (Population<Animal> animal : animals) {
            String name = animal.getBeingType().getName();
            System.out.println("Добавить " + name + "? (1. Да / 2. Нет)");
            if (UserInput.takeUserInputFromRange(1, 2) == 1) {
                addFoodChain(animal, populations, ecosystem);  // Добавляем животное и его пищевую цепь
            }
        }
    }

    private static void addFoodChain(Population<? extends LivingBeing> population, List<Population<? extends LivingBeing>> populations, Ecosystem ecosystem) {
        if (!populations.contains(population)) {
            populations.add(population);

            if (population.getBeingType() instanceof Animal animal) {
                // Получаем тип пищи
                String foodName = animal.getFoodType().getName();

                // Поиск пищи среди уже существующих растений и животных в экосистеме
                Population<? extends LivingBeing> foodPopulation = ecosystem.findPopulationByName(foodName);

                // Если пища есть, добавляем её
                if (foodPopulation != null) {
                    System.out.println("Добавляем " + foodName + " как пищу для " + animal.getName());
                    addFoodChain(foodPopulation, populations, ecosystem);  // Рекурсивно добавляем пищевую цепь
                }
            }
        }
    }


    private static Ecosystem createNewEcosystem() {
        EcosystemResources resources = new EcosystemResources(21, 43, 12, 45, 3);
        Ecosystem ecosystem = new Ecosystem(resources);

        List<Population<? extends LivingBeing>> populations = new ArrayList<>();

        // Предложение добавить растения из всех предыдущих экосистем (Не реализовано)
        /* if (livingBeingStorageManager.isPlantFileExist()) {
            List<? extends Population<? extends LivingBeing>> plants = new ArrayList<>(livingBeingStorageManager.loadPlants(livingBeingStorageManager.getPlantsFile()));
            System.out.println("Добавить существующие растения?");
            addExistingBeing(populations, plants);  // Добавляем существующие растения
        }

        // Создание новых животных с учётом их пищевых зависимостей
        System.out.println("Добавить новых животных?");
        if (UserInput.takeUserInputFromRange(1, 2) == 1) {
            addNewAnimalsWithDependencies(populations, ecosystem);
        }

        // Добавляем популяции в экосистему
        for (Population<? extends LivingBeing> population : populations) {
            ecosystem.addPopulation(population);
        } */

        return ecosystem;
    }


    private static void addNewAnimalsWithDependencies(List<Population<? extends LivingBeing>> populations, Ecosystem ecosystem) {
        boolean addMoreAnimals = true;
        while (addMoreAnimals) {
            Animal newAnimal = BeingFactory.createAnimalFromInput();
            System.out.println("Введите популяцию для " + newAnimal.getName() + ":");
            int populationSize = UserInput.takeUserIntegerInput();
            Population<Animal> animalPopulation = new Population<>(newAnimal, populationSize);

            // Добавляем животное и его пищевую цепь в экосистему
            addFoodChain(animalPopulation, populations, ecosystem);

            System.out.println("Добавить ещё животное? (1. Да / 2. Нет)");
            addMoreAnimals = UserInput.takeUserInputFromRange(1, 2) == 1;
        }
    }


    private static void addExistingBeing(List<Population<? extends LivingBeing>> populations, List<? extends Population<? extends LivingBeing>> plants) {
        if (UserInput.takeUserInputFromRange(1, 2) == 1) {
            for (Population<? extends LivingBeing> plant : plants) {
                System.out.println("Добавить " + plant.getBeingType().getName() + "? (1. Да / 2. Нет)");
                if (UserInput.takeUserInputFromRange(1, 2) == 1) {
                    System.out.println("Введите популяцию для " + plant.getBeingType().getName() + ":");
                    int populationSize = UserInput.takeUserIntegerInput();
                    populations.add(new Population<>(plant.getBeingType(), populationSize));
                }
            }
        }
    }


    private static void deleteEcosystem() {
        List<String> availableEcosystems = storageManager.listAvailableEcosystems();
        if (availableEcosystems.isEmpty()) {
            System.out.println("Нет сохранённых экосистем.");
        } else {
            System.out.println("Выберите экосистему для удаления:");
            String fileName = takeAvailableEcosystem(availableEcosystems);
            if (storageManager.deleteEcosystemDirectory(fileName)) {
                System.out.println("Экосистема " + fileName + " успешно удалена.");
            } else {
                System.out.println("Не удалось удалить экосистему.");
            }
        }
    }

    private static String takeAvailableEcosystem(List<String> availableEcosystems) {
        for (int i = 0; i < availableEcosystems.size(); i++) {
            System.out.println((i + 1) + ". " + availableEcosystems.get(i));
        }
        int ecosystemIndex = UserInput.takeUserInputFromRange(1, availableEcosystems.size());
        return availableEcosystems.get(ecosystemIndex - 1);
    }

    private static Ecosystem loadExistingEcosystem() {
        Ecosystem result = null;
        List<String> availableEcosystems = storageManager.listAvailableEcosystems();
        if (availableEcosystems.isEmpty()) {
            System.out.println("Нет сохранённых экосистем.");
        } else {
            System.out.println("Выберите экосистему для загрузки:");
            String fileName = takeAvailableEcosystem(availableEcosystems) + "/";
            List<Population<? extends LivingBeing>> populations = new ArrayList<>();
            populations.addAll(livingBeingStorageManager.loadPlants(fileName));
            populations.addAll(livingBeingStorageManager.loadAnimals(fileName));
            result = storageManager.loadEcosystem(fileName, populations);
        }

        return result;
    }

    // Метод для сохранения экосистемы
    private static void ecosystemSaving(Ecosystem ecosystem, String currentDirectoryName) {
        boolean finished = false;
        System.out.println("\nХотите сохранить экосистему?");
        System.out.println("1. Да");
        System.out.println("2. Нет");

        if (UserInput.takeUserInputFromRange(1, 2) == 1) {
            String directoryName;
            if (currentDirectoryName != null) {
                System.out.println("Сохранить изменения в текущую директорию (" + currentDirectoryName + ")?");
                System.out.println("1. Да");
                System.out.println("2. Создать новую директорию");

                if (UserInput.takeUserInputFromRange(1, 2) == 1) {
                    directoryName = currentDirectoryName; // Используем существующую директорию
                } else {
                    System.out.println("Введите имя новой директории:");
                    directoryName = UserInput.takeUserStringInput();
                    ecosystem.setEcosystemName(directoryName);
                    if (storageManager.createEcosystemDirectory(directoryName)) {
                        System.out.println("Ошибка: директория уже существует. Выберите другое имя.");
                        finished = true;// Остановить сохранение, если пользователь не хочет перезаписывать
                    }
                }
            } else {
                // Создаем новую директорию, если нет текущей директории
                System.out.println("Введите имя директории для сохранения экосистемы:");
                directoryName = UserInput.takeUserStringInput();
                if (storageManager.createEcosystemDirectory(directoryName)) {
                    System.out.println("Ошибка: директория уже существует.");
                    finished = true;
                }
            }
            if (!finished) {// Сохраняем экосистему и связанные данные
                if(!directoryName.equals(currentDirectoryName)) {
                    storageManager.createEcosystemFiles(directoryName);
                }
                livingBeingStorageManager.createEntityFile();
                livingBeingStorageManager.clearEntityFile(EcosystemStorageManager.getStorageDirectory() + directoryName + "/");
                storageManager.saveEcosystem(ecosystem, directoryName);
                for (Population<?> population : ecosystem.getPopulations()) {
                    if (population.getBeingType() instanceof Plant) {
                        livingBeingStorageManager.savePlant((Population<Plant>) population, directoryName);
                    } else if (population.getBeingType() instanceof Animal) {
                        livingBeingStorageManager.saveAnimal((Population<Animal>) population, directoryName);
                    }
                }
            }
        }
    }


    // Основной процесс работы с экосистемой
    private static void ecosystemWorkProcess(Ecosystem ecosystem) {
        EcosystemController controller = new EcosystemController(ecosystem);
        controller.startCurrentSimulation();
        ecosystemSaving(ecosystem,ecosystem.getEcosystemName());
    }

    public static void main(String[] args) {
        Ecosystem ecosystem;
        boolean isWorking = true;

        while (isWorking) {
            System.out.println("Вы хотите:");
            System.out.println("1. Загрузить существующую экосистему");
            System.out.println("2. Создать новую экосистему");
            System.out.println("3. Удалить экосистему");
            System.out.println("4. Выйти из программы.");
            int choice = UserInput.takeUserInputFromRange(1, 4);

            switch (choice) {
                case 1 -> {
                    ecosystem = loadExistingEcosystem();
                    if (ecosystem == null) {
                        System.out.println("Не удалось загрузить данную экосистему");
                    } else {
                        ecosystemWorkProcess(ecosystem);
                    }
                }
                case 2 -> {
                    ecosystem = createNewEcosystem();
                    ecosystemWorkProcess(ecosystem);
                }
                case 3 -> deleteEcosystem();
                case 4 -> isWorking = false;
            }
        }
    }
}
