package cars.student;

// Não precisamos mais importar Car, pois StudentCarBase já o faz
import cars.engine.Vector2;
import cars.engine.World;

import java.awt.*;
import java.util.random.RandomGenerator;

import static cars.engine.Vector2.*;
import static java.lang.Math.toRadians;


// 1. MUDANÇA DA HERANÇA: Agora estende StudentCarBase
public class StudentCarWander extends StudentCarBase {
    private static final RandomGenerator RND = RandomGenerator.getDefault();

    // Configurações do círculo de Wander (Valores ajustados para movimento visível)
    private static final double WANDER_CIRCLE_DISTANCE = 50.0;
    private static final double WANDER_CIRCLE_RADIUS = 500.0;
    private static final double WANDER_ANGLE_CHANGE = toRadians(180);

    private double wanderAngle;

    public StudentCarWander(Color cor, double x, double y) {
        super(settings ->
                settings
                        .color(cor)
                        .randomOrientation()
                        .position(x, y)
                        // Parâmetros ajustados:
                        .maxForce(2000)
                        .maxSpeed(150)
                        .mass(0.2)
        );
        this.wanderAngle = RND.nextDouble(0, 2 * Math.PI);
    }

    /**
     * 2. MUDANÇA DO MÉTODO: Implementa o método calculateBehaviorForce.
     * Deve retornar APENAS a força de Wander, sem o truncate final.
     */
    @Override
    public Vector2 calculateBehaviorForce(final World world) {

        // Lógica de Impulso Inicial: Força o movimento se estiver parado
        if (getSpeed() < 5) {
            Vector2 initialImpulse = byAngle(wanderAngle).multiply(getMaxSpeed() / 2.0);
            // Retorna o impulso como a força de comportamento
            return initialImpulse;
        }

        // 1. Vetor que aponta pro centro do circulo
        Vector2 circleCenter = multiply(getDirection(), WANDER_CIRCLE_DISTANCE);

        // 2. Mudar o ângulo do alvo aleatoriamente (mas suavemente)
        double angleChange = (RND.nextDouble(-1.0, 1.0) * WANDER_ANGLE_CHANGE) * world.getSecs();
        wanderAngle += angleChange;

        // 3. Vetor do centro até a borda do circulo
        Vector2 displacement = byAngle(wanderAngle);
        displacement.multiply(WANDER_CIRCLE_RADIUS);

        // 4. Calcula a posição final do alvo
        Vector2 wanderTarget = add(getPosition(), circleCenter, displacement);

        // 5. Calcula a Força de Seek (Busca) para o alvo de Wander
        Vector2 desiredVelocity = subtract(wanderTarget, getPosition());
        desiredVelocity.resize(getMaxSpeed());

        Vector2 steeringForce = subtract(desiredVelocity, getVelocity());

        // Retorna a força bruta de Wander. O StudentCarBase se encarrega da soma com Avoidance e do truncate.
        return steeringForce;
    }
}