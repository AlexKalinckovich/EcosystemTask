package Entity;

import EcosystemProcess.EcosystemResources;

import java.util.List;
import java.util.Objects;
import java.util.Random;

public class Population<T extends LivingBeing> {
    private final T beingType;  // Тип существа
    private int currentPopulation; // Текущее количество существ

    public Population(T beingType, int initialPopulation) {
        this.beingType = beingType;
        this.currentPopulation = initialPopulation;

    }

    public void resourceCheck(EcosystemResources resources, List<String> logs) {
        int missingResourceCount = beingType.getEnvironment().checkIfConditionsAreMet(resources);
        String log;
        // Проверка на возраст (естественная смерть)
        if (!beingType.isAlive()) {
            currentPopulation--;
            log = beingType.getName() + " умирает от старости.";
            logs.add(log);
            System.out.println(log);
        }

        // Если условия не удовлетворены, уменьшаем популяцию
        if (missingResourceCount > 0) {
            log = beingType.getName() + " испытывает нехватку ресурсов: " + missingResourceCount;
            System.out.println(log);
            logs.add(log);
            applyDeathEffect(missingResourceCount,"нехватки ресурсов",logs);
        } else {
            log = beingType.getName() + " получает достаточно ресурсов для роста.";
            System.out.println(log);
            reproduce(resources);  // Если ресурсов достаточно, размножаемся быстрее
        }
    }

    // Метод для применения конкуренции между особями одного вида
    private void applyIntraSpeciesCompetition(List<String> logs) {
        Random rand = new Random();
        String log;
        int deaths = 0;

        // Чем больше популяция, тем выше вероятность конкуренции и смертей
        for (int i = 0; i < currentPopulation; i++) {
            double chanceOfDeath = Math.min(0.1 * ((double) currentPopulation / 50), 1.0);  // Шанс смерти до 100%
            if (rand.nextDouble() < chanceOfDeath) {
                deaths++;
            }
        }

        currentPopulation -= deaths;
        if (currentPopulation < 0) {
            currentPopulation = 0;
        }

        if (deaths > 0) {
            log = beingType.getName() + ": " + deaths + " особей умерли в результате конкуренции.";
            System.out.println(log);
            logs.add(log);
        }
    }


    public void consumeProcess(Population<?> foodPopulation, List<String> logs) {
        int foodAvailable = foodPopulation.getCurrentPopulation(); // Сколько пищи доступно (популяция еды)
        int foodNeeded = currentPopulation;  // Сколько пищи нужно (равно текущей популяции животных)
        String log;
        // Проверяем, достаточно ли еды
        if (foodAvailable >= foodNeeded) {
            log = beingType.getName() + " съели всю необходимую пищу.";
            System.out.println(log);
            // Пища съедена, ничего не происходит с популяцией
            foodPopulation.reducePopulation(foodNeeded);  // Уменьшаем популяцию пищи на необходимое количество
        } else {
            // Недостаток пищи: вычисляем нехватку и её влияние
            int foodDeficit = foodNeeded - foodAvailable;
            log = beingType.getName() + " испытывают недостаток пищи: не хватает " + foodDeficit + " единиц.";
            System.out.println(log);
            foodPopulation.reducePopulation(foodAvailable);

            // Влияние нехватки пищи на популяцию
            applyDeathEffect(foodDeficit," нехватки пищи.",logs);
        }
        // Конкуренция за ресурсы между особями одного вида
        applyIntraSpeciesCompetition(logs);
    }

    // Уменьшаем популяцию пищи
    public void reducePopulation(int amount) {
        currentPopulation -= amount;
        if (currentPopulation < 0) {
            currentPopulation = 0;  // Популяция не может быть отрицательной
        }
    }

    // Метод, применяющий влияние голода
    private void applyDeathEffect(int foodDeficit,String reason,List<String> logs) {
        Random rand = new Random();
        String log;
        int deaths = 0;
        for (int i = 0; i < currentPopulation; i++) {
            // Вероятность смерти возрастает с увеличением дефицита пищи
            double chanceOfDeath = Math.min(1.0, 0.1 * foodDeficit);  // Максимальная вероятность смерти — 100%
            if (rand.nextDouble() < chanceOfDeath) {
                deaths++;
            }
        }

        // Уменьшаем популяцию на количество смертей
        currentPopulation -= deaths;

        if (currentPopulation < 0) {
            currentPopulation = 0;  // Ограничение: популяция не может стать отрицательной
        }
        log = beingType.getName() + ": " + deaths + " особей умерли от" + reason;
        System.out.println(log);
        logs.add(log);
    }

    private void reproduce(EcosystemResources resources) {
        Random rand = new Random();

        // коэффициент размножения на основе удовлетворенности ресурсов
        double reproductionFactor = calculateReproductionFactor(resources);

        // Проверяем, достаточно ли популяции для размножения
        if (currentPopulation > 1) {
            // Чем выше reproductionFactor, тем быстрее размножение
            int maxNewPlants = Math.max(1, (int)(currentPopulation * reproductionFactor) >> 1);
            int newPlants = rand.nextInt(1, maxNewPlants + 1);  // Размножение
            currentPopulation += newPlants;
            System.out.println(beingType.getName() + " размножилось на " + newPlants + " новых особей.");
        } else {
            System.out.println(beingType.getName() + " не может размножаться, так как популяция слишком мала.");
        }
    }

    // Метод для расчета коэффициента размножения на основе ресурсов
    private double calculateReproductionFactor(EcosystemResources resources) {
        int optimalConditions = 0;
        int totalConditions = 5;  // 5 ресурсов (температура, влажность, ветер, вода, радиация)

        // Проверяем каждый ресурс на соответствие условиям
        if (beingType.getEnvironment().getRequirement("temperature").isWithinRange(resources.getTemperature())) {
            optimalConditions++;
        }
        if (beingType.getEnvironment().getRequirement("humidity").isWithinRange(resources.getHumidity())) {
            optimalConditions++;
        }
        if (beingType.getEnvironment().getRequirement("wind").isWithinRange(resources.getWind())) {
            optimalConditions++;
        }
        if (beingType.getEnvironment().getRequirement("water").isWithinRange(resources.getWater())) {
            optimalConditions++;
        }
        if (beingType.getEnvironment().getRequirement("radiation").isWithinRange(resources.getRadiation())) {
            optimalConditions++;
        }

        // Рассчитываем коэффициент на основе количества оптимальных условий
        // Максимальный коэффициент = 2.0
        return 1.0 + (optimalConditions / (double) totalConditions);
    }

    public T getBeingType() {
        return beingType;
    }

    public int getCurrentPopulation() {
        return currentPopulation;
    }

    @Override
    public boolean equals(Object o) {
        boolean result;
        if (this == o) {
            result = true;
        } else if (!(o instanceof Population<?> that)) {
            result = false;
        } else {
            result = currentPopulation == that.currentPopulation &&
                    Objects.equals(beingType, that.beingType);
        }
        return result;
    }

    @Override
    public int hashCode() {
        return Objects.hash(beingType, currentPopulation);
    }

    public boolean isExtinct() {
        return currentPopulation <= 0;
    }

    @Override
    public String toString() {
        return "Population{" +
                "beingType=" + beingType +
                ", currentPopulation=" + currentPopulation +
                '}';
    }

    public void setCurrentPopulation(int value) {
        this.currentPopulation = value;
    }
}
