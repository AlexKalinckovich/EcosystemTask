package EcosystemProcess;



public class EcosystemResources {
    private int temperature;
    private int humidity;
    private int wind;
    private int water;
    private int radiation;

    public EcosystemResources(int temperature, int humidity, int wind, int water, int radiation) {
        this.temperature = temperature;
        this.humidity = humidity;
        this.wind = wind;
        this.water = water;
        this.radiation = radiation;
    }

    // Геттеры
    public int getTemperature() {
        return temperature;
    }

    public int getHumidity() {
        return humidity;
    }

    public int getWind() {
        return wind;
    }

    public int getWater() {
        return water;
    }

    public int getRadiation() {
        return radiation;
    }

    // Сеттеры
    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public void setWind(int wind) {
        this.wind = wind;
    }

    public void setWater(int water) {
        this.water = water;
    }

    public void setRadiation(int radiation) {
        this.radiation = radiation;
    }

    @Override
    public String toString() {
        return "Температура: " + temperature + "\n" +
                "Влажность: " + humidity + "\n" +
                "Скорость ветра (м/c): " + wind + "\n" +
                "Вода: " + water + "\n" +
                "Радиация: " + radiation;
    }
}
