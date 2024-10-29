package Entity;

import EcosystemProcess.Environment;

public interface LivingBeing {
    void grow();          // Растет ли существо
    void consume();       // Потребляет ли что-то (еда, вода и т.д.)
    boolean isAlive();
    String getName();
    Environment getEnvironment();
}
