package cars.student;


import cars.engine.Vector2;
import cars.engine.World;
import cars.engine.Car;

import java.awt.*;
import java.util.List;

import static cars.engine.Vector2.*;


public class StudentCarSeek extends StudentCarBase {


    public StudentCarSeek(Color cor, double x, double y) {
        super(settings ->
                settings
                        .color(cor)
                        .randomOrientation()
                        .position(x,y)
                        .maxSpeed(350) // Velocidade padrão
                        .maxForce(400) // Força padrão
        );
    }

        // calculo do Seek
    @Override
    public Vector2 calculateBehaviorForce(final World world) {

        // Obter os vizinhos e verificar se a lista não está vazia
        List<Car> neighbors = world.getNeighbors();
        if (neighbors.isEmpty()) {
            return vec2();
        }

     // Pega o Vector2 do ultimo carro na lista e pega a posição
        final Vector2 targetPos = neighbors.getLast().getPosition();
        // Posição alvo (carro amarelo) - posição do atual = direção do vetor
        Vector2 desiredVelocity = subtract(targetPos, getPosition());

        // resize ajusta o vetor para o tamanho maxSpeed
        desiredVelocity.resize(getMaxSpeed());


        // Força de direção = flexa amarela (desiredVelocity) - (Velocidade atual) seta azul (que tá no car)
        return subtract(desiredVelocity, getVelocity());
    }


}