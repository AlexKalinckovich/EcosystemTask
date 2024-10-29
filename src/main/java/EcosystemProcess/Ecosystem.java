package EcosystemProcess;

import Entity.Animal;
import Entity.LivingBeing;
import Entity.Population;
import FileStorage.EcosystemStorageManager;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class Ecosystem {
    private final EcosystemResources resources;
    private List<Population<? extends LivingBeing>> populations = new ArrayList<>();
    private String ecosystemName = null;

    private final List<String> logs = new ArrayList<>();

    public void setPopulations(List<Population<? extends LivingBeing>> populations) {
        this.populations = populations;
    }

    public String getEcosystemName() {
        return ecosystemName;
    }

    public List<String> getLogs() {
        return logs;
    }

    public void setEcosystemName(String ecosystemName) {
        this.ecosystemName = ecosystemName;
    }

    public Ecosystem(EcosystemResources resources) {
        this.resources = resources;
    }

    public Ecosystem(EcosystemResources resources,List<Population<? extends LivingBeing>> populations,String ecosystemName) {
        this.resources = resources;
        this.populations.addAll(populations);
        this.ecosystemName = ecosystemName;
    }

    public void addPopulation(Population<? extends LivingBeing> population) {
        populations.add(population);
    }

    // Обновление состояния экосистемы
    public void updateEcosystem() {
        System.out.println("=== Обновление экосистемы ===");
        for(Population<? extends LivingBeing> population : populations) {
            if (population.getBeingType() instanceof Animal){
                Population<? extends LivingBeing> other = findPopulationByName(((Animal) population.getBeingType()).getFoodType().getName());
                population.consumeProcess(other,logs);
            }
            population.resourceCheck(resources,logs);
        }
    }

    // Показать состояние всех популяций
    public void showPopulations() {
        System.out.println("=== Текущие популяции в экосистеме ===");
        for (Population<? extends LivingBeing> population : populations) {
            String logStr = "Вид: " + population.getBeingType().getName() +
                    ", Численность: " + population.getCurrentPopulation();
            System.out.println(logStr);
            logs.add(logStr);
        }
    }

    // Метод для записи лога
    public void logEcosystemUpdate(String ecosystemName, List<String> logMessage) {
        String logFilePath = EcosystemStorageManager.getStorageDirectory() + ecosystemName + "/log.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(logFilePath, true))) {
            for(String log : logMessage) {
                writer.write(log + "\n");
            }
        } catch (IOException e) {
            System.err.println("Ошибка записи в лог-файл: " + e.getMessage());
        }
    }

    // Показать текущие доступные ресурсы экосистемы
    public void showResources() {
        System.out.println("=== Текущие ресурсы экосистемы ===");
        System.out.println(resources);
        logs.add(resources.toString());
    }

    // Найти популяцию по типу пищи (для проверки пищевой цепи)
    public Population<? extends LivingBeing> findPopulationByName(String name) {
        for (Population<? extends LivingBeing> population : populations) {
            if (population.getBeingType().getName().equals(name)) {
                return population;
            }
        }
        return null;
    }

    @Override
    public boolean equals(Object o) {
        boolean result;
        if (this == o) {
            result = true;
        } else if (!(o instanceof Ecosystem ecosystem)) {
            result = false;
        } else {
            result = Objects.equals(resources, ecosystem.resources) &&
                    Objects.equals(populations, ecosystem.populations) &&
                    Objects.equals(ecosystemName, ecosystem.ecosystemName) &&
                    Objects.equals(logs, ecosystem.logs);
        }
        return result;
    }

    @Override
    public int hashCode() {
        return Objects.hash(resources, populations, ecosystemName, logs);
    }

    public List<Population<? extends LivingBeing>> getPopulations() {
        return populations;
    }

    public EcosystemResources getResources() {
        return resources;
    }
}
