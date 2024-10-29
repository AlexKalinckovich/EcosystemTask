package Entity;

import EcosystemProcess.Environment;

import java.util.Objects;

public class Animal implements LivingBeing {
    private final String name;
    private LivingBeing foodType;  // Чем питается животное
    private final Environment environment;  // Связь с окружающей средой
    private int age;  // Возраст животного
    private final int lifespan;  // Максимальная продолжительность жизни

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public boolean equals(Object o) {
        boolean result;
        if (this == o) {
            result = true;
        } else if (!(o instanceof Animal animal)) {
            result = false;
        } else {
            result = age == animal.age &&
                    lifespan == animal.lifespan &&
                    Objects.equals(name, animal.name) &&
                    Objects.equals(foodType, animal.foodType) &&
                    Objects.equals(environment, animal.environment);
        }
        return result;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, foodType, environment, age, lifespan);
    }

    @Override
    public String toString() {
        return "Название:" + name + "\n" + environment;
    }

    public int getLifespan() {
        return lifespan;
    }

    public Animal(String name, Environment environment, LivingBeing foodType, int age) {
        this.name = name;
        this.foodType = foodType;
        this.environment = environment;
        this.age = age;
        this.lifespan = 10 + (int)(Math.random() * 10);  // Животные живут от 10 до 20 дней
    }

    public void setFoodType(LivingBeing foodType) {
        this.foodType = foodType;
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
        age++;
        System.out.println(name + " растет, возраст: " + age);
    }

    @Override
    public void consume() {
        System.out.println(name + " потребляет " + foodType.getName() + " и воду.");
    }

    @Override
    public boolean isAlive() {
        return age < lifespan;
    }

    public LivingBeing getFoodType() {
        return foodType;
    }

    public int getAge() {
        return age;
    }
}
