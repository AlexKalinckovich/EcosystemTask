package Entity;

import EcosystemProcess.Environment;

import java.util.Objects;


public class Plant implements LivingBeing {
    private final String name;
    private final Environment environment;  // Связь с окружающей средой

    public Plant(String name,Environment environment) {
        this.name = name;
        this.environment = environment;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Environment getEnvironment() {
        return environment;
    }

    @Override
    public void grow() {
        System.out.println(name + " растет.");
    }

    @Override
    public void consume() {
        System.out.println(name + " потребляет воду и солнечный свет.");
    }

    @Override
    public boolean equals(Object o) {
        boolean result;
        if (this == o) {
            result = true;
        } else if (!(o instanceof Plant plant)) {
            result = false;
        } else {
            result = Objects.equals(name, plant.name) && Objects.equals(environment, plant.environment);
        }
        return result;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, environment);
    }

    @Override
    public boolean isAlive() {
        return false;
    }

    @Override
    public String toString() {
        return "Название:" + name + "\n" + environment;
    }
}
