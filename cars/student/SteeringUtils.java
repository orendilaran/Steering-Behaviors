package cars.student;

import cars.engine.Car;
import cars.engine.Vector2;
import cars.engine.World;

// lista de vizinhos
import java.util.List;


import static cars.engine.Vector2.vec2;
import static cars.engine.Vector2.subtract;
import static cars.engine.Vector2.normalize;
import static cars.engine.Vector2.multiply;
import static cars.engine.Vector2.truncate;


public final class SteeringUtils {
    // --- Esse código evita que os carros colidam entre si ---

    // Raio de detecção: Distância máxima para começar a desviar
    private static final int AVOIDANCE_RADIUS = 75;
    // Fator de Repulsão: Intensidade da força de afastamento.
    private static final double REPULSION_FACTOR = 5000.0;

    /**
     * Calcula a força de direção (steering force) necessária para desviar de carros vizinhos.
     * Esta força é inversamente proporcional ao quadrado da distância do vizinho.
     * * @param currentCar O carro que está calculando a força.
     * @param world O objeto World com informações da simulação.
     * @return O Vector2 da força de desvio, limitado pela força máxima do carro.
     */
    public static Vector2 calculateAvoidance(Car currentCar, final World world) {

        // 1. Obter os vizinhos dentro do raio de detecção (usa java.util.List)
        List<Car> neighbors = world.getNeighbors(AVOIDANCE_RADIUS);

        if (neighbors.isEmpty()) {
            return vec2(); // Retorna vetor zero se não houver vizinhos
        }

        Vector2 avoidanceForce = vec2();

        for (Car neighbor : neighbors) {
            Vector2 displacement = subtract(currentCar.getPosition(), neighbor.getPosition());
            double distance = displacement.size();

            Vector2 direction = normalize(displacement);



            // Ponderação (Strength):
            // O Desvio deve ser máximo (1.0) quando a distância é pequena, e zero no limite do raio.
            // (AVOIDANCE_RADIUS - distance) / AVOIDANCE_RADIUS

            // Calcula o quanto estamos "dentro" do raio (0 a 1)
            double innerDistance = AVOIDANCE_RADIUS - distance;

            // Força Máxima (ex: 2000) * Ponderação Linear (o quanto está dentro do raio)
            double strength = REPULSION_FACTOR * (innerDistance / AVOIDANCE_RADIUS);

            // Garantimos que a força não seja negativa (caso o carro esteja fora por pouco)
            if (strength < 0) strength = 0;

            // Cria o vetor de repulsão
            Vector2 repulsion = multiply(direction, strength);
            avoidanceForce.add(repulsion);
        }

        // 2. Limita a força total gerada (a Avoidance Force não pode ser maior que o maxForce do carro)
        return truncate(avoidanceForce, currentCar.getMaxForce());
    }
}