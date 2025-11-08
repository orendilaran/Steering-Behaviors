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

// Classe base para o comportamento dos carros
public abstract class StudentCarBase extends Car {

    // Construtor da classe Car
    public StudentCarBase(Consumer<Settings> settings) {
        super(settings);
    }


    public abstract Vector2 calculateBehaviorForce(final World world);


    @Override
    public final Vector2 calculateSteering(final World world) {

        // 1. Calcular a Força de Comportamento (O que o carro QUER fazer)
        Vector2 behaviorForce = calculateBehaviorForce(world);
        if (behaviorForce == null) {
            behaviorForce = Vector2.vec2(); // Força zero se o comportamento não retornar nada
        }

        // 2. Calcular a Força de Desvio (O que o carro DEVE fazer para NÃO BATER)
        // Passamos 'this' (a referência ao carro atual) para o método utilitário.
        Vector2 avoidanceForce = calculateAvoidance(this, world);

        // 3. Somar as Forças: Combina o que o carro quer com o Desvio
        // Damos PRIORIDADE ao Desvio multiplicando-o por 2.0. Isso garante que o carro vire para evitar a colisão.
        Vector2 prioritizedAvoidance = multiply(avoidanceForce, 2.0);
        Vector2 finalForce = add(behaviorForce, prioritizedAvoidance);

        // 4. Truncar (limitar) o resultado final da soma pela força máxima do carro.
        return truncate(finalForce, getMaxForce());
    }
}