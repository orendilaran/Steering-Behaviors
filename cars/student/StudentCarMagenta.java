package cars.student;


import cars.engine.Car;
import cars.engine.Vector2;
import cars.engine.World;

import java.awt.*;
import java.util.List;
import java.util.Random;

import static cars.engine.Vector2.subtract;
import static cars.engine.Vector2.vec2;
//Carro com duas funcionalidades: Seek e Follow Path
public class StudentCarMagenta extends StudentCarBase {

    int currentTargetCar = 0;
    private static final Vector2[] Path = {
            vec2(-350, -200),
            vec2(350, -200),
            vec2(350, 220),
            vec2(-350, 210)
    };
    private static final int STATE_FOLLOW_PATH = 0;
    private static final int STATE_SEEK = 1;
    int state= STATE_FOLLOW_PATH;
    private static int i = 0;
    public StudentCarMagenta(Color cor, double x, double y) {
        super(settings ->
                settings
                        .color(cor)
                        .randomOrientation()
                        .position(x,y)
                        .maxSpeed(200) // Velocidade padrão
                        .maxForce(300) // Força padrão
        );
    }

        // calculo do Seek
    @Override
    public Vector2 calculateBehaviorForce(final World world) {
        Random number = new Random();
        // Obter os vizinhos e verificar se a lista não está vazia
        List<Car> neighbors = world.getNeighbors();
        if (neighbors.isEmpty()) {
            return vec2();
        }

        // Pega o Vector2 do ultimo carro na lista e pega a posição
        switch (state) {

            case STATE_FOLLOW_PATH -> {
                if ((this.getPosition().x >= Path[i].x - 7 && this.getPosition().x <= Path[i].x + 7) && (this.getPosition().y >= Path[i].y - 7 && this.getPosition().y <= Path[i].y + 7)) {
                    i++; // progressao na lista
                    if (i >= Path.length) {
                        i = 0;
                        state = STATE_SEEK;
                    }
                }
                final Vector2 targetPos = Path[i];
                Vector2 desiredVelocity = subtract(targetPos, getPosition());
                desiredVelocity.resize(getMaxSpeed());
                return subtract(desiredVelocity, getVelocity());

            }
            case STATE_SEEK -> {
                final Vector2 targetPos = neighbors.get(currentTargetCar).getPosition();
                Vector2 desiredVelocity = subtract(targetPos, getPosition());
                desiredVelocity.resize(getMaxSpeed());
                if (subtract(targetPos, getPosition()).size() < 75) {
                    currentTargetCar = number.nextInt(neighbors.size());
                    state = STATE_FOLLOW_PATH;
                }
                return subtract(desiredVelocity, getVelocity());
            }
        }

        // resize ajusta o vetor para o tamanho maxSpeed
        return null;
    }


}