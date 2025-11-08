package cars.student;

import cars.engine.Vector2;
import cars.engine.World;

import java.awt.*;

import static cars.engine.Vector2.subtract;
import static cars.engine.Vector2.vec2;
import static cars.engine.Vector2.*;


public class StudentCarFlee extends StudentCarBase {


    // raio de fuga
    private static final double PANIC_RADIUS = 300.0;

    public StudentCarFlee(Color cor, double x, double y) {
        super(settings ->
                settings
                        .color(cor)
                        .randomOrientation()
                        .position(x,y)
                        .maxForce(800)
                        .maxSpeed(400)
        );
    }


    @Override
    public Vector2 calculateBehaviorForce(final World world) {

        final Vector2 targetPos = world.getMousePos();

        if (targetPos == null) {
            return vec2();
        }

        // Calcular o vetor do carro ao mouse e a distância
        Vector2 displacement = subtract(targetPos, getPosition());
        double distance = displacement.size();


        // Se a distância for MAIOR que o raio, o carro não sente a ameaça e a força de fuga é zero.
        if (distance > PANIC_RADIUS) {
            return vec2();
        }

        // Lógica de FUGA (Flee) Sò funciona dentro do PANIC_RADIUS
        Vector2 desiredVelocity = subtract(getPosition(), targetPos);

        // Define a Velocidade Desejada (Direção * Velocidade Máxima)
        desiredVelocity.resize(getMaxSpeed());

        // Flexa amarela (desiredVelocity) - Seta azul (Velocidade atual)
        return subtract(desiredVelocity, getVelocity());
    }
}