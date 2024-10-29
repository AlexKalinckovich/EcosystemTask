package FileStorage;

import EcosystemProcess.Environment;
import EcosystemProcess.Requirement;
import Entity.Animal;
import Entity.LivingBeing;
import Entity.Plant;
import Entity.Population;
import Factories.BeingFactory;

import java.io.*;
import java.util.*;

public class LivingBeingStorageManager {

    private final String livingStorageDir = "living_storage/";
    private final String animalsFile = "animals.txt";
    private final String plantsFile = "plants.txt";
    private final Set<String> animals = new HashSet<>();
    private final Set<String> plants = new HashSet<>();
    private final String storageDirectory = "ecosystem_storage/";

    public String getAnimalsFile() {
        return livingStorageDir + animalsFile;
    }

    public String getPlantsFile() {
        return livingStorageDir + plantsFile;
    }


    public boolean createEntityFile() {
        boolean result = false;
        boolean finished = false;
        File dir = new File(livingStorageDir);
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                System.err.println("Ошибка при создании директории living_storage.");
                finished = true;
            }
        }
        if (!finished) {
            File animalFile = new File(livingStorageDir + this.animalsFile);
            File plantFile = new File(livingStorageDir + this.plantsFile);
            try {
                if (animalFile.createNewFile()) {
                    System.out.println("Файл entity.txt успешно создан.");
                    result = true;
                } else {
                    System.out.println("Файл entity.txt уже существует.");
                }

                if(plantFile.createNewFile()) {
                    System.out.println("Файл plants.txt успешно создан");
                    result = true;
                }else{
                    System.out.println("Файл plants.txt уже существует");
                }
            } catch (IOException e) {
                System.err.println("Ошибка при создании файла entity.txt: " + e.getMessage());
            }
        }

        return result;
    }

    private boolean isFileExists(String filename) {
        File file = new File(filename);
        return file.exists();
    }

    public LivingBeingStorageManager() {
        if(isFileExists(livingStorageDir + this.animalsFile) && isFileExists(livingStorageDir + this.plantsFile)) {
            loadEntities();
        }
    }

    // Метод для загрузки всех сущностей в HashSet для проверки уникальности
    private void loadEntities() {
        File animalFile = new File(livingStorageDir + this.animalsFile);
        File plantFile = new File(livingStorageDir + this.plantsFile);

        try (BufferedReader reader = new BufferedReader(new FileReader(animalFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                animals.add(line);
            }
        } catch (IOException e) {
            System.err.println("Ошибка при загрузке сущностей: " + e.getMessage());
        }

        try(BufferedReader reader = new BufferedReader(new FileReader(plantFile))){
            String line;
            while ((line = reader.readLine()) != null) {
                plants.add(line);
            }
        }catch (IOException e) {
            System.err.println("Ошибка при загрузке сущностей: " + e.getMessage());
        }
    }

    // Метод для сохранения нового животного или растения в entity.txt
    private void saveEntity(String entityName,boolean isAnimal) {
        if(isAnimal){
            saveUnique(entityName, animals,livingStorageDir + animalsFile);
        }else{
            saveUnique(entityName, plants,livingStorageDir + plantsFile);
        }
    }

    private void saveUnique(String entityName, Set<String> plants,String fileName) {
        if (!plants.contains(entityName)) {
            plants.add(entityName);
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
                writer.write(entityName + "\n");
            } catch (IOException e) {
                System.err.println("Ошибка при сохранении сущности: " + e.getMessage());
            }
        }
    }

    public void saveAnimal(Population<Animal> populationAnimal,String dir) {
        saveLivingBeing(populationAnimal,dir,animalsFile);
        saveEntity(populationAnimal.getBeingType().getName(),true);
    }

    public void savePlant(Population<Plant> populationPlant,String dir) {
        saveLivingBeing(populationPlant, dir,plantsFile);
        saveEntity(populationPlant.getBeingType().getName(),false);
    }


    public boolean isAnimalFileExist(){
        File file = new File(livingStorageDir + animalsFile);
        return file.exists();
    }

    public boolean isPlantFileExist(){
        File file = new File(livingStorageDir + plantsFile);
        return file.exists();
    }

    public void clearEntityFile(String dirPath){
        File animalFile = new File(dirPath + animalsFile);
        File plantFile = new File(dirPath + plantsFile);
        File animalsFile = new File(livingStorageDir + this.animalsFile);
        File plantsFile = new File(livingStorageDir + this.plantsFile);


        try {
            if (!animalFile.exists() || !plantFile.exists() || animalsFile.exists() || !plantsFile.exists()) {
                if (!animalFile.createNewFile() || !plantFile.createNewFile()) {
                    throw new IOException();
                }
            } else {
                // Очищаем файлы, если они существуют
                new BufferedWriter(new FileWriter(animalFile)).close();
                new BufferedWriter(new FileWriter(plantFile)).close();
                new BufferedWriter(new FileWriter(animalsFile)).close();
                new BufferedWriter(new FileWriter(plantsFile)).close();
            }
            System.out.println("Файлы для хранения животных и растений очищены.");
        } catch (IOException e) {
            System.out.println("Файлы для хранения животных и растений не требуют очистки.");
        }
    }

    // Общий метод для сохранения животных и растений
    private void saveLivingBeing(Population<? extends LivingBeing> population, String dir,String filePath) {

        LivingBeing being = population.getBeingType();

        String fileName = storageDirectory + "/" + dir + "/" + filePath;


        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName,true))) {
            if (being instanceof Animal animal) {
                writer.write("Animal,"
                                + animal.getName() + ","
                                + animal.getFoodType().getName() + ","
                                + animal.getAge() + ","
                                + population.getCurrentPopulation() + ","
                                + saveRequirements(animal.getEnvironment()) + "\n");
            } else if (being instanceof Plant plant) {
                writer.write("Plant,"
                        + plant.getName() + ","
                        + population.getCurrentPopulation() + ","
                        + saveRequirements(plant.getEnvironment()) + "\n");
            }
            System.out.println(being.getName() + " успешно сохранено в файл: " + fileName);
        } catch (IOException e) {
            System.err.println("Ошибка при сохранении: " + e.getMessage());
        }
    }

    private String saveRequirements(Environment environment) {
        Requirement temperature = environment.getRequirement("temperature");
        Requirement humidity = environment.getRequirement("humidity");
        Requirement wind = environment.getRequirement("wind");
        Requirement water = environment.getRequirement("water");
        Requirement radiation = environment.getRequirement("radiation");

        return temperature.minRequirementValue() + "," + temperature.maxRequirementValue() + "," +
                humidity.minRequirementValue() + "," + humidity.maxRequirementValue() + "," +
                wind.minRequirementValue() + "," + wind.maxRequirementValue() + "," +
                water.minRequirementValue() + "," + water.maxRequirementValue() + "," +
                radiation.minRequirementValue() + "," + radiation.maxRequirementValue();
    }

    // Метод для загрузки всех животных из файла
    public Collection<? extends Population<? extends LivingBeing>> loadAnimals(String dir) {
        return loadLivingBeings(storageDirectory + dir + animalsFile, true);
    }

    // Метод для загрузки всех растений из файла
    public Collection<? extends Population<? extends LivingBeing>> loadPlants(String dir) {
        return loadLivingBeings(storageDirectory + dir + plantsFile, false);
    }

    // Общий метод для загрузки животных и растений
    private <T extends LivingBeing> List<Population<T>> loadLivingBeings(String filePath, boolean isAnimal) {
        List<Population<T>> populations = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                LivingBeing being;
                if (isAnimal) {
                    Population<Animal> population = parseAnimal(parts);
                    being = population.getBeingType();
                    populations.add((Population<T>) population);
                    BeingFactory.getBeings().put(being.getName(), being);
                } else {
                    Population<Plant> population = parsePlant(parts);
                    populations.add((Population<T>) population);
                    being = population.getBeingType();
                    BeingFactory.getBeings().put(being.getName(), being);
                }
            }
        } catch (IOException e) {
            System.err.println("Ошибка при загрузке: " + e.getMessage());
        }

        return populations;
    }

    // Метод для парсинга животного
    private Population<Animal> parseAnimal(String[] parts) {
        String name = parts[1];

        String foodTypeName = parts[2];  // Храним только имя, объект найдем позже
        LivingBeing food = BeingFactory.createLivingBeing(foodTypeName);

        int age = Integer.parseInt(parts[3]);
        Environment environment = parseEnvironment(parts, 4);

        int initialPopulation = Integer.parseInt(parts[4]);


        // Создаем Animal без корректного foodType, оно будет установлено позже
        Animal animal = new Animal(name, environment,food, age);  // null для foodType
        return new Population<>(animal, initialPopulation);  // Популяция добавляется как обычно
    }

    // Метод для парсинга растения
    private Population<Plant> parsePlant(String[] parts) {
        String name = parts[1];
        Environment environment = parseEnvironment(parts, 2);
        int initialPopulation = Integer.parseInt(parts[2]);
        Plant plant = new Plant(name,environment);
        return new Population<>(plant,initialPopulation);
    }

    // Метод для парсинга ресурсов из строки
    private Environment parseEnvironment(String[] parts, int offset) {
        Requirement temperature = new Requirement("temperature", Integer.parseInt(parts[offset]), Integer.parseInt(parts[offset + 1]));
        Requirement humidity = new Requirement("humidity", Integer.parseInt(parts[offset + 2]), Integer.parseInt(parts[offset + 3]));
        Requirement wind = new Requirement("wind", Integer.parseInt(parts[offset + 4]), Integer.parseInt(parts[offset + 5]));
        Requirement water = new Requirement("water", Integer.parseInt(parts[offset + 6]), Integer.parseInt(parts[offset + 7]));
        Requirement radiation = new Requirement("radiation", Integer.parseInt(parts[offset + 8]), Integer.parseInt(parts[offset + 9]));

        return new Environment(temperature, humidity, wind, water, radiation);
    }
}
