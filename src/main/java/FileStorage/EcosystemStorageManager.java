package FileStorage;

import EcosystemProcess.Ecosystem;
import EcosystemProcess.EcosystemResources;
import Entity.LivingBeing;
import Entity.Population;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class EcosystemStorageManager {

    private static final String storageDirectory = "ecosystem_storage/";

    public boolean createEcosystemDirectory(String ecosystemName) {
        boolean result = false;
        File ecosystemDir = new File(storageDirectory + ecosystemName);
        if (!ecosystemDir.exists()) {
            if (ecosystemDir.mkdirs()) {
                System.out.println("Директория для экосистемы " + ecosystemName + " успешно создана.");
                result = true;
            } else {
                System.err.println("Ошибка при создании директории для экосистемы " + ecosystemName);
            }
        } else {
            System.out.println("Директория для экосистемы " + ecosystemName + " уже существует.");
        }
        return !result;
    }

    // Метод для создания файлов экосистемы
    public boolean createEcosystemFiles(String ecosystemName) {
        boolean result;
        String ecosystemDirPath = storageDirectory + ecosystemName + "/";

        File ecosystemFile = new File(ecosystemDirPath + "ecosystem.txt");
        File animalsFile = new File(ecosystemDirPath + "animals.txt");
        File plantsFile = new File(ecosystemDirPath + "plants.txt");
        File logFile = new File(ecosystemDirPath + "log.txt");

        try {
            if (ecosystemFile.createNewFile()) {
                System.out.println("Файл ecosystem.txt успешно создан.");
            }
            if (animalsFile.createNewFile()) {
                System.out.println("Файл animals.txt успешно создан.");
            }
            if (plantsFile.createNewFile()) {
                System.out.println("Файл plants.txt успешно создан.");
            }
            if (logFile.createNewFile()) {
                System.out.println("Файл log.txt успешно создан.");
            }
            result = true;
        } catch (IOException e) {
            System.err.println("Ошибка при создании файлов экосистемы: " + e.getMessage());
            result = false;
        }
        return result;
    }
    
    public static String getStorageDirectory() {
        return storageDirectory;
    }

    // Метод для сохранения экосистемы и её составляющих в директорию
    public void saveEcosystem(Ecosystem ecosystem, String ecosystemName) {
        boolean finished = false;
        ecosystem.setEcosystemName(ecosystemName);
        String dirPath = "ecosystem_storage/" + ecosystemName + "/ecosystem.txt";
        File dir = new File(dirPath);
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                System.out.println("Ошибка создания директории экосистемы.");
                finished = true;
            }
        }
        if (!finished) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(dirPath))) {
                EcosystemResources resources = ecosystem.getResources();
                writer.write("EcosystemName" + "\n");
                writer.write(ecosystemName + "\n");
                writer.write("Temperature:" + resources.getTemperature() + "\n");
                writer.write("Humidity:" + resources.getHumidity() + "\n");
                writer.write("Wind:" + resources.getWind() + "\n");
                writer.write("Water:" + resources.getWater() + "\n");
                writer.write("Radiation:" + resources.getRadiation() + "\n");
                System.out.println("Экосистема успешно сохранена.");
            } catch (IOException e) {
                System.err.println("Ошибка при сохранении экосистемы: " + e.getMessage());
            }
        }
        ecosystem.logEcosystemUpdate(ecosystem.getEcosystemName(),ecosystem.getLogs());
    }

    public boolean deleteEcosystemDirectory(String ecosystemName) {
        boolean result = false;
        File dir = new File("ecosystem_storage/" + ecosystemName);

        if (dir.exists()) {
            try {
                // Удаляем все файлы и подкаталоги, начиная с самого глубинного уровня
                Files.walk(Paths.get(dir.getPath()))
                        .sorted(Comparator.reverseOrder())
                        .map(java.nio.file.Path::toFile)
                        .forEach(File::delete);

                System.out.println("Экосистема " + ecosystemName + " успешно удалена.");
                result = true;
            } catch (IOException e) {
                System.err.println("Ошибка при удалении экосистемы: " + e.getMessage());
            }
        } else {
            System.out.println("Экосистема с таким именем не найдена.");
        }

        return result;
    }


    public List<String> listAvailableEcosystems() {
        File dir = new File("ecosystem_storage/");
        File[] files = dir.listFiles(File::isDirectory);
        List<String> ecosystemDirs = new ArrayList<>();

        if (files != null) {
            for (File file : files) {
                ecosystemDirs.add(file.getName());
            }
        }

        return ecosystemDirs;
    }

    // Метод для загрузки экосистемы
    public Ecosystem loadEcosystem(String ecosystemName, List<Population<? extends LivingBeing>> populations) {
        Ecosystem result;
        String dirPath = "ecosystem_storage/" + ecosystemName + "/";
        File file = new File(dirPath + "ecosystem.txt");
        EcosystemResources resources = null;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;

            while ((line = reader.readLine()) != null) {
                if(line.equals("EcosystemName")){
                    ecosystemName = reader.readLine();
                }else{
                    resources = parseResources(line, resources);
                }
            }

            System.out.println("Экосистема успешно загружена: " + ecosystemName);
            result = new Ecosystem(resources, populations,ecosystemName);
        } catch (IOException e) {
            System.err.println("Ошибка при загрузке экосистемы: " + e.getMessage());
            result = null;
        }
        return result;
    }

    // Метод для парсинга ресурсов
    private EcosystemResources parseResources(String line, EcosystemResources resources) {
        String[] parts = line.split(":");
        String resourceName = parts[0];
        int value = Integer.parseInt(parts[1]);

        if (resources == null) {
            resources = new EcosystemResources(0, 0, 0, 0, 0);
        }

        return switch (resourceName) {
            case "Temperature" -> new EcosystemResources(value, resources.getHumidity(), resources.getWind(), resources.getWater(), resources.getRadiation());
            case "Humidity" -> new EcosystemResources(resources.getTemperature(), value, resources.getWind(), resources.getWater(), resources.getRadiation());
            case "Wind" -> new EcosystemResources(resources.getTemperature(), resources.getHumidity(), value, resources.getWater(), resources.getRadiation());
            case "Water" -> new EcosystemResources(resources.getTemperature(), resources.getHumidity(), resources.getWind(), value, resources.getRadiation());
            case "Radiation" -> new EcosystemResources(resources.getTemperature(), resources.getHumidity(), resources.getWind(), resources.getWater(), value);
            default -> throw new IllegalArgumentException("Неизвестный ресурс: " + resourceName);
        };
    }

}
