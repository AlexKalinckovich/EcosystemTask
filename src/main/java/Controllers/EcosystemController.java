package Controllers;

import EcosystemProcess.Ecosystem;
import EcosystemProcess.EcosystemResources;
import EcosystemProcess.Environment;
import EcosystemProcess.Requirement;
import Entity.Animal;
import Entity.LivingBeing;
import Entity.Plant;
import Entity.Population;
import Factories.BeingFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EcosystemController {
    private final Ecosystem currentEcosystem;

    public EcosystemController(Ecosystem currentEcosystem) {
        this.currentEcosystem = currentEcosystem;
    }

    public Ecosystem getCurrentEcosystem() {
        return currentEcosystem;
    }

    @Override
    public String toString() {
        return "EcosystemController{" +
                "currentEcosystem=" + currentEcosystem +
                '}';
    }

    private void showMenu() {
        System.out.println("1 - Запустить симуляцию");
        System.out.println("2 - Добавить популяцию животных");
        System.out.println("3 - Добавить популяцию растений");
        System.out.println("4 - Показать состояние экосистемы");
        System.out.println("5 - Показать доступные ресурсы");
        System.out.println("6 - Показать существующие популяции");
        System.out.println("7 - Изменить ресурсы экосистемы");
        System.out.println("8 - Изменить размер популяции");
        System.out.println("9 - Предсказание жизни экосистемы");
        System.out.println("10 - Выйти");
    }

    private void modifyResources(EcosystemResources resources,List<String> logs) {
        System.out.println("Выберите ресурс для изменения:");
        System.out.println("1. Температура: " + resources.getTemperature());
        System.out.println("2. Влажность: " + resources.getHumidity());
        System.out.println("3. Ветер: " + resources.getWind());
        System.out.println("4. Вода: " + resources.getWater());
        System.out.println("5. Радиация: " + resources.getRadiation());

        int choice = UserInput.takeUserInputFromRange(1, 5);
        System.out.println("Введите новое значение для выбранного ресурса:");

        int newValue = UserInput.takeUserIntegerInput();
        switch (choice) {
            case 1 -> resources.setTemperature(newValue);
            case 2 -> resources.setHumidity(newValue);
            case 3 -> resources.setWind(newValue);
            case 4 -> resources.setWater(newValue);
            case 5 -> resources.setRadiation(newValue);
            default -> throw new IllegalArgumentException("Некорректный выбор ресурса");
        }

        String logMessage = "Ресурс " + getResourceName(choice) + " изменён на " + newValue;
        logs.add(logMessage);
        System.out.println(logMessage);
    }

    private void modifyEcosystemResources(Ecosystem ecosystem) {
        EcosystemResources resources = ecosystem.getResources();
        modifyResources(resources,ecosystem.getLogs());
    }

    private static String getResourceName(int resourceIndex) {
        return switch (resourceIndex) {
            case 1 -> "Температура";
            case 2 -> "Влажность";
            case 3 -> "Ветер";
            case 4 -> "Вода";
            case 5 -> "Радиация";
            default -> throw new IllegalArgumentException("Неверный индекс ресурса");
        };
    }

    public void startCurrentSimulation() {
        boolean isWorking = true;
        final int MAX_INPUT_CHOICE = 10;
        final int MIN_INPUT_CHOICE = 1;
        while (isWorking) {
            showMenu();
            int choice = UserInput.takeUserInputFromRange(MIN_INPUT_CHOICE, MAX_INPUT_CHOICE);
            switch (choice) {
                case 1 -> currentEcosystem.updateEcosystem();
                case 2 -> addAnimalPopulation();
                case 3 -> addPlantPopulation();
                case 4 -> showEcosystemState();
                case 5 -> showAvailableResources();
                case 6 -> showAvailablePopulations();
                case 7 -> modifyEcosystemResources(currentEcosystem);
                case 8 -> modifyPopulationSize();
                case 9 -> predictEcosystemLifespan();
                case 10 -> isWorking = false;
            }
        }
    }

    public void predictEcosystemLifespan() {
        final int MAX_SURVIVING_DAYS = 100;
        boolean survived = true;


        List<Population<? extends LivingBeing>> prevState = deepCopyPopulations(currentEcosystem.getPopulations());

        int i = 0;
        while (survived && i < MAX_SURVIVING_DAYS) {
            currentEcosystem.updateEcosystem();

            for (Population<?> population : currentEcosystem.getPopulations()) {
                if (population.getCurrentPopulation() == 0) {
                    System.out.println("Экосистема начнет погибать через " + i + " дней");
                    survived = false;
                    break;
                }
            }
            i++;
        }

        if (survived) {
            System.out.println("Экосистема продержится как минимум " + MAX_SURVIVING_DAYS + " дней");
        }

        currentEcosystem.setPopulations(prevState);
    }

    private List<Population<? extends LivingBeing>> deepCopyPopulations(List<Population<? extends LivingBeing>> populations) {
        List<Population<? extends LivingBeing>> copiedPopulations = new ArrayList<>();

        for (Population<? extends LivingBeing> population : populations) {
            LivingBeing beingCopy = deepCopyLivingBeing(population.getBeingType());
            Population<? extends LivingBeing> populationCopy = new Population<>(beingCopy, population.getCurrentPopulation());
            copiedPopulations.add(populationCopy);
        }

        return copiedPopulations;
    }

    private LivingBeing deepCopyLivingBeing(LivingBeing being) {
        LivingBeing result;
        if (being instanceof Animal original) {
            result = new Animal(original.getName(),
                    deepCopyEnvironment(original.getEnvironment()),
                    deepCopyLivingBeing(original.getFoodType()), // Копируем пищу
                    original.getAge());// Копируем пищу
        } else if (being instanceof Plant original) {
            result = new Plant(original.getName(),
                    deepCopyEnvironment(original.getEnvironment()));
        } else {
            throw new IllegalArgumentException("Unknown type of LivingBeing");
        }
        return result;
    }

    private Environment deepCopyEnvironment(Environment original) {
        return new Environment(
                new Requirement(original.getRequirement("temperature").requirement(),
                                original.getRequirement("temperature").minRequirementValue(),
                                original.getRequirement("temperature").maxRequirementValue()),

                new Requirement(original.getRequirement("humidity").requirement(),
                                original.getRequirement("humidity").minRequirementValue(),
                                original.getRequirement("humidity").maxRequirementValue()),

                new Requirement(original.getRequirement("wind").requirement(),
                                original.getRequirement("wind").minRequirementValue(),
                                original.getRequirement("wind").maxRequirementValue()),

                new Requirement(original.getRequirement("water").requirement(),
                                original.getRequirement("water").minRequirementValue(),
                                original.getRequirement("water").maxRequirementValue()),

                new Requirement(original.getRequirement("radiation").requirement(),
                                original.getRequirement("radiation").minRequirementValue(),
                                original.getRequirement("radiation").maxRequirementValue())
        );
    }

    private void modifyPopulationSize() {
        System.out.println("Выберите популяцию, размер который вы бы хотели изменить.");
        int size = currentEcosystem.getPopulations().size();
        List<Population<? extends LivingBeing>> populations = currentEcosystem.getPopulations();
        for(int i = 0;i < size; i++){
            System.out.println(i + 1 + " " + populations.get(i).getBeingType().getName());
        }
        int choice = UserInput.takeUserInputFromRange(1,size);
        System.out.println("Введите новый размер популяции:");
        int value = UserInput.takeUserInputFromRange(1,Integer.MAX_VALUE);
        Population<? extends LivingBeing> modifyPopulation = populations.get(choice - 1);
        modifyPopulation.setCurrentPopulation(value);
        String log = modifyPopulation.getBeingType().getName() + " нынешнее количество экземпляра " + value;
        System.out.println(log);
        currentEcosystem.getLogs().add(log);
    }

    private void showAvailablePopulations(){
        List<Population<? extends LivingBeing>> list = currentEcosystem.getPopulations();
        for(Population<? extends LivingBeing> item: list){
            System.out.println(item);
        }
    }

    @Override
    public boolean equals(Object o) {
        boolean result;
        if (this == o) {
            result = true;
        } else if (!(o instanceof EcosystemController that)) {
            result = false;
        } else {
            result = Objects.equals(currentEcosystem, that.currentEcosystem);
        }
        return result;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(currentEcosystem);
    }

    private void addAnimalPopulation() {
        if(!currentEcosystem.getPopulations().isEmpty()) {
            Animal newAnimal = BeingFactory.createAnimalFromInput();
            System.out.println("Введите начальное количество животных:");
            int animalCount = UserInput.takeUserInputFromRange(1, Integer.MAX_VALUE);
            Population<Animal> animalPopulation = new Population<>(newAnimal, animalCount);
            currentEcosystem.addPopulation(animalPopulation);
            String logStr = "В экосистему добавлена популяция:" + animalPopulation.getBeingType().getName();
            currentEcosystem.getLogs().add(logStr);
            System.out.println("Популяция животных добавлена.");
        }else{
            System.out.println("В текущей экосистеме не представлено ни одного растения. \n Животным будет нечего есть.");
        }
    }

    private void addPlantPopulation() {
        Plant newPlant = BeingFactory.createPlantFromInput();
        System.out.println("Введите начальное количество растений:");
        int plantCount = UserInput.takeUserInputFromRange(1, Integer.MAX_VALUE);
        Population<Plant> plantPopulation = new Population<>(newPlant, plantCount);
        currentEcosystem.addPopulation(plantPopulation);
        String logStr = "В экосистему добавлена популяция:" + plantPopulation.getBeingType().getName();
        currentEcosystem.getLogs().add(logStr);
        System.out.println("Популяция растений добавлена.");
    }

    private void showAvailableResources() {
        currentEcosystem.showResources();
    }

    private void showEcosystemState() {
        currentEcosystem.showPopulations();
    }
}
