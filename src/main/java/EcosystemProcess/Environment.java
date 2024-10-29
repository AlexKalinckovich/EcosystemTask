package EcosystemProcess;

import java.util.HashMap;

public class Environment {
    private final HashMap<String, Requirement> requirements = new HashMap<>();

    public Environment(Requirement temperature, Requirement humidity,
                       Requirement wind, Requirement water,
                       Requirement radiation) {
        requirements.put("temperature", temperature);
        requirements.put("humidity", humidity);
        requirements.put("wind", wind);
        requirements.put("water", water);
        requirements.put("radiation", radiation);
    }


    @Override
    public String toString() {
        return "Environment{" +
                "requirements=" + requirements +
                '}';
    }

    public HashMap<String, Requirement> getRequirements() {
        return requirements;
    }

    // Метод для получения требования среды
    public Requirement getRequirement(String name) {
        return requirements.get(name);
    }

    public int checkIfConditionsAreMet(EcosystemResources resources) {
        int result = 0;
        for (HashMap.Entry<String, Requirement> entry : requirements.entrySet()) {
            String key = entry.getKey();
            Requirement requirement = entry.getValue();

            int currentValue = switch (key) {
                case "temperature" -> resources.getTemperature();
                case "humidity" -> resources.getHumidity();
                case "wind" -> resources.getWind();
                case "water" -> resources.getWater();
                case "radiation" -> resources.getRadiation();
                default -> throw new IllegalArgumentException("Неизвестный ресурс: " + key);
            };

            if (requirement.isWithinRange(currentValue)) {
                result++;
            }
        }
        return result;
    }

    public void showEnvironment() {
        for (Requirement requirement : requirements.values()) {
            System.out.println(requirement.toString());
        }
    }
}
