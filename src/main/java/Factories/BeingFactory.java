package Factories;

import Controllers.UserInput;
import EcosystemProcess.Environment;
import EcosystemProcess.Requirement;
import Entity.Animal;
import Entity.LivingBeing;
import Entity.Plant;

import java.util.HashMap;


public class BeingFactory {

    private static HashMap<String,LivingBeing> beings = new HashMap<>();

    public static HashMap<String,LivingBeing> getBeings() {
        return beings;
    }

    public static void setBeings(HashMap<String, LivingBeing> beings) {
        BeingFactory.beings = beings;
    }


    public static Animal createAnimalFromInput() {
        System.out.println("Введите название животного:");
        String name = createBeingName();
        Environment animalEnvironment = createEnvironmentFromInput();
        System.out.println("Введите название сущности, которая будет являться пищей для животного:");
        LivingBeing foodType = takeBeingFoodType(name);
        Animal animal = new Animal(name, animalEnvironment,foodType,0);
        beings.put(name, animal);
        return animal;
    }

    public static LivingBeing takeBeingFoodType(String animalName) {
        LivingBeing result = null;
        boolean isCorrect = false;
        String foodName;
        while(!isCorrect) {
            isCorrect = true;
            foodName = UserInput.takeUserStringInput();
            if(!beings.containsKey(foodName)) {
                System.out.println("Такого экземпляра нет.");
                isCorrect = false;
            }else if(foodName.equals(animalName)){
                System.out.println("Экземпляр не может питаться сам собой.");
                isCorrect = false;
            }else{
                result = beings.get(foodName);
            }
        }
        return result;
    }

    public static Plant createPlantFromInput() {
        System.out.println("Введите название растения:");
        String name = createBeingName();
        Environment plantEnvironment = createEnvironmentFromInput();
        Plant plant = new Plant(name, plantEnvironment);
        beings.put(name, plant);
        return plant;
    }

    private static String createBeingName(){
        String name = "";
        boolean isCorrect = false;
        while(!isCorrect){
            isCorrect = true;
            name = UserInput.takeUserStringInput().toLowerCase();
            if(beings.containsKey(name)){
                System.out.println("Экземпляр с таким названием уже существует.");
                isCorrect = false;
            }
        }
        return name;
    }

    private static Requirement createRequirementFromInput(String requirementName,int min,int max) {
        int low;
        int high;

        System.out.println("Введите " + requirementName);
        System.out.println("Минимальное значение:");
        low = UserInput.takeUserInputFromRange(min,max - 1);
        System.out.println("Максимальное значение:");
        high = UserInput.takeUserInputFromRange(low,max);
        return new Requirement(requirementName, low, high);
    }

    public static Environment createEnvironmentFromInput() {
        Requirement temperature = createRequirementFromInput("температуру (от -50 до 50):",-50,50);
        Requirement humidity = createRequirementFromInput("влажность (в процентах):",0,100);
        Requirement wind = createRequirementFromInput("силу ветра (0-50):",0,50);
        Requirement water = createRequirementFromInput("количество воды (0-100):",0,100);
        Requirement radiation = createRequirementFromInput("уровень радиации (0-100):",0,100);

        // Создаем объект среды с переданными параметрами
        return new Environment(temperature, humidity, wind, water, radiation);
    }

    public static LivingBeing createLivingBeing(String name){
        return new LivingBeing() {
            @Override
            public void grow() {

            }

            @Override
            public void consume() {

            }

            @Override
            public boolean isAlive() {
                return false;
            }

            @Override
            public String getName() {
                return name;
            }

            @Override
            public Environment getEnvironment() {
                return null;
            }
        };
    }
}
