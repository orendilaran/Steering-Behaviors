package cars.student;


import cars.engine.Vector2;
import cars.engine.World;

import java.awt.*;


import static cars.engine.Vector2.subtract;
import static cars.engine.Vector2.normalize;
import static cars.engine.Vector2.vec2;


// 1. MUDANÇA DA HERANÇA: Agora estende StudentCarBase
public class StudentCarMouse extends StudentCarBase {
    // A variável 'state' foi removida, pois não é necessária para um comportamento fixo.

    public StudentCarMouse(Color cor, double x, double y) {
        super(settings ->
                settings
                        .color(cor)
                        .randomOrientation()
                        .position(x,y)
                        .maxSpeed(200) // Velocidade moderada
                        .maxForce(600) // Força aumentada para garantir que ele possa virar
        );
    }

    /**
     * 2. MUDANÇA DO MÉTODO: Implementa o método calculateBehaviorForce.
     * Contém APENAS a lógica de Busca (Seek) do Mouse.
     */
    @Override
    public Vector2 calculateBehaviorForce(final World world) {
        final Vector2 targetPos = world.getMousePos();

        if (targetPos == null) {
            // Retorna vetor zero, o carro mantém o que estava fazendo
            return vec2();
        }

        // Lógica de SEEK (Buscar)

        // 1. Calcular o vetor do carro até o alvo (Direção de Busca)
        Vector2 desiredVelocity = subtract(targetPos, getPosition());

        // 2. Definir a Velocidade Desejada (Direção * Velocidade Máxima)
        // Note: Seu código original não usava desiredSpeed no cálculo final, mas sim o maxSpeed.
        // O método resize faz a mesma coisa: define o tamanho para maxSpeed.
        desiredVelocity.resize(getMaxSpeed());

        // 3. Calcular a Força de Direção (Steering Force)
        // Força = Velocidade Desejada - Velocidade Atual

        // Retorna a força bruta de Seek. O StudentCarBase fará a soma com Avoidance e o truncate.
        return subtract(desiredVelocity, getVelocity());
    }
}
