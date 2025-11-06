package cars.student;

import cars.engine.Vector2;
import cars.engine.World;

import java.awt.*;

import static cars.engine.Vector2.subtract;
import static cars.engine.Vector2.vec2;
import static cars.engine.Vector2.*;

// Agora estende StudentCarBase
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

        // 3. Lógica do Flee (Fuga) - SÓ EXECUTADA SE ESTIVER DENTRO DO RAIO

        // Vetor de Desejo: aponta do mouse PARA o carro (getPosition - targetPos)
        // Usamos o displacement invertido (o vetor de subtração já foi calculado acima, então podemos reutilizar, mas é mais limpo recalcular a direção)
        Vector2 desiredVelocity = subtract(getPosition(), targetPos);

        // Define a Velocidade Desejada (Direção * Velocidade Máxima)
        desiredVelocity.resize(getMaxSpeed());

        // Retorna a Força de Fuga bruta.
        // O StudentCarBase adiciona o Avoidance e aplica o limite final de força.
        return subtract(desiredVelocity, getVelocity());
    }
}