package cars.student;

import cars.engine.Vector2;
import cars.engine.World;

import java.awt.Color;

import static cars.engine.Vector2.normalize;
import static cars.engine.Vector2.subtract;
import static cars.engine.Vector2.vec2;

// AGORA HERDA DE StudentCarBase
public class StudentCarArrive extends StudentCarBase {

    private static final double DECELERATION_RADIUS = 500.0;

    public StudentCarArrive(Color cor, double x, double y) {
        super(settings ->
                settings
                        .color(cor)
                        .randomOrientation()
                        .position(x, y)
                        .maxSpeed(500)
        );
    }


    @Override
    public Vector2 calculateBehaviorForce(final World world) {
        final Vector2 targetPos = world.getMousePos();

        if (targetPos == null) {
            return vec2();
        }

        Vector2 desiredVelocity = subtract(targetPos, getPosition());
        double distance = desiredVelocity.size();

        // Lógica de desaceleração (Arrive)
        double desiredSpeed;
        if (distance <= DECELERATION_RADIUS) {
            desiredSpeed = getMaxSpeed() * (distance /(3* DECELERATION_RADIUS));
        } else {
            desiredSpeed = getMaxSpeed();
        }

        desiredVelocity = normalize(desiredVelocity);
        desiredVelocity.multiply(desiredSpeed);

        // Retorna a força bruta de direção. O limite será aplicado no StudentCarBase.
        return subtract(desiredVelocity, getVelocity());
    }
}