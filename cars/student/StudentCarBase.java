package cars.student;

import cars.engine.Car;
import cars.engine.Vector2;
import cars.engine.World;


import static cars.student.SteeringUtils.calculateAvoidance;
import static cars.engine.Vector2.multiply;
import static cars.engine.Vector2.add;
import static cars.engine.Vector2.truncate;
import java.util.function.Consumer;
import cars.engine.Settings;

import java.util.function.Consumer;

// Classe base para o comportamento dos carros - todos os carros possuem desvio de colisão e truncate.
public abstract class StudentCarBase extends Car {

    // Construtor da classe Car
    public StudentCarBase(Consumer<Settings> settings) {
        super(settings);
    }


    public abstract Vector2 calculateBehaviorForce(final World world);


    @Override
    public final Vector2 calculateSteering(final World world) {

        // Para evitar erros
        Vector2 behaviorForce = calculateBehaviorForce(world); //pega um comportamento da classe filha
        if (behaviorForce == null) {
            behaviorForce = Vector2.vec2(); // Força zero se o comportamento não retornar nada
        }

        //Pega o avoidanceForce que está na Classe SteeringUtils aqui confesso que não entendi o que acontece, é magica.
        Vector2 avoidanceForce = calculateAvoidance(this, world);

        // Damos PRIORIDADE ao Desvio multiplicando-o por 2.0. Isso garante que o carro vire para evitar a colisão.
        Vector2 prioritizedAvoidance = multiply(avoidanceForce, 2.0);
        Vector2 finalForce = add(behaviorForce, prioritizedAvoidance);

        // o truncate garante que a força total não exceda o máximo permitida (que a gente escolhe na classe)
        return truncate(finalForce, getMaxForce());
    }
}