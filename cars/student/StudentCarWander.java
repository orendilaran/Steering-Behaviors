package cars.student;


import cars.engine.Vector2;
import cars.engine.World;

import java.awt.*;
import java.util.random.RandomGenerator;

import static cars.engine.Vector2.*;
import static java.lang.Math.toRadians;



public class StudentCarWander extends StudentCarBase {
    private static final RandomGenerator RND = RandomGenerator.getDefault();

    // Configurações do círculo de Wander
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
                        .maxForce(2000)
                        .maxSpeed(150)
                        .mass(0.2)
        );
        this.wanderAngle = RND.nextDouble(0, 2 * Math.PI);
    }


    @Override
    public Vector2 calculateBehaviorForce(final World world) {

        // Se estiver parado, anda :P
        if (getSpeed() < 5) {
            Vector2 initialImpulse = byAngle(wanderAngle).multiply(getMaxSpeed() / 2.0);
            return initialImpulse;
        }

        // Forma o Vetor que aponta pro centro do circulo
        Vector2 circleCenter = multiply(getDirection(), WANDER_CIRCLE_DISTANCE);

        //RND é o random que gera um angulo aleatório
        double angleChange = (RND.nextDouble(-1.0, 1.0) * WANDER_ANGLE_CHANGE) * world.getSecs();
        wanderAngle += angleChange;

        //Pega um ponto do perimetro (aleatório)
        Vector2 displacement = byAngle(wanderAngle);
        displacement.multiply(WANDER_CIRCLE_RADIUS);

        // Calcula a posição final do alvo (para onde ele vai)
        Vector2 wanderTarget = add(getPosition(), circleCenter, displacement);

        // A força que vai carregar o carrinho até o alvo final calculado ali em cima /\
        Vector2 desiredVelocity = subtract(wanderTarget, getPosition());

        // resize ajusta o vetor para o tamanho maxSpeed;
        desiredVelocity.resize(getMaxSpeed());

        // enfim a seta amarela, o resultado de todas da força calculada alí em cima - Velocity da classe CAR
        Vector2 steeringForce = subtract(desiredVelocity, getVelocity());

        // O StudentCarBase se encarrega da soma com Avoidance e do truncate.
        return steeringForce;
    }
}