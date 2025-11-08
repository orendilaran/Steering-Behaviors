package cars.student;

import cars.engine.Car;
import cars.engine.Vector2;
import cars.engine.World;
import static cars.student.SteeringUtils.calculateAvoidance;

import java.awt.*;

import static cars.engine.Vector2.vec2;
import java.util.random.RandomGenerator;
import static cars.engine.Vector2.normalize;
import static cars.engine.Vector2.subtract;
import static cars.engine.Vector2.truncate;
import static cars.engine.Vector2.multiply;
import static cars.engine.Vector2.add;
import static cars.engine.Vector2.byAngle;
import static java.lang.Math.toRadians;
//Aqui tinha o básico que a professora passou, mas aí modificamos para fazer o carro AMARELO

public class StudentCar extends Car {
    //ESTADOS do carro amarelo que muda ao apertar ESPAÇO
    private static final int STATE_SEEK = 0;
    private static final int STATE_FLEE = 1;
    private static final int STATE_ARRIVE = 2;
    private static final int STATE_WANDER = 3;

    int state= STATE_SEEK;


// Configurações do carro amarelo
    private static final double DECELERATION_RADIUS = 400.0;
    private static final RandomGenerator RND = RandomGenerator.getDefault();
    private static final double WANDER_CIRCLE_DISTANCE = 50.0;
    private static final double WANDER_CIRCLE_RADIUS = 500.0;
    private static final double WANDER_ANGLE_CHANGE = toRadians(270);
    private double wanderAngle; // Ângulo interno de Wander

    public StudentCar(Color cor, double x, double y) {
        super(settings ->
          settings
            .color(cor)
            .randomOrientation()
            .position(x,y)
                  .maxForce(2000)
                  .maxSpeed(150)
                  .mass(0.2)

        );
        this.wanderAngle = RND.nextDouble(0, 2 * Math.PI); // Inicia ângulo Wander
    }
    @Override
    public Vector2 calculateSteering(final World world) {
        //logica de troca de estado com o ESPAÇO
        if (world.isSpacePressed()) {
            state = (state + 1) % 4; // Ciclo: 0 -> 1 -> 2 -> 3 -> 0 ...
            System.out.println("Comportamento mudado para estado: " + state);
        }

        Vector2 targetPos = world.getMousePos();

        // Se não houver mouse e não estiver em WANDER, retorna nulo
        if (targetPos == null && state != STATE_WANDER) {
            return null;
        }

        return switch (state) {
            case STATE_SEEK -> calculateSeek(targetPos);
            case STATE_FLEE -> calculateFlee(targetPos);
            case STATE_ARRIVE -> calculateArrive(targetPos);
            case STATE_WANDER -> calculateWander(world);
            default -> null;
        };
    }
// --- MÉTODOS DE COMPORTAMENTO ---

    // SEEK
    private Vector2 calculateSeek(Vector2 targetPos) {
        Vector2 desiredVelocity = subtract(targetPos, getPosition());
        desiredVelocity.resize(getMaxSpeed());

        Vector2 steeringForce = subtract(desiredVelocity, getVelocity());
        return truncate(steeringForce, getMaxForce());
    }

    //FLEE
    private Vector2 calculateFlee(Vector2 targetPos) {
        Vector2 desiredVelocity = subtract(getPosition(), targetPos); // Inverte para fugir
        desiredVelocity.resize(getMaxSpeed());

        Vector2 steeringForce = subtract(desiredVelocity, getVelocity());
        return truncate(steeringForce, getMaxForce());
    }

    // 2. Comportamento ARRIVE (Chegar)
    private Vector2 calculateArrive(Vector2 targetPos) {
        Vector2 desiredVelocity = subtract(targetPos, getPosition());
        double distance = desiredVelocity.size();

        double desiredSpeed;
        if (distance <= DECELERATION_RADIUS) {
            desiredSpeed = getMaxSpeed() * (distance / DECELERATION_RADIUS);
        } else {
            desiredSpeed = getMaxSpeed();
        }

        desiredVelocity = normalize(desiredVelocity);
        desiredVelocity.multiply(desiredSpeed);

        Vector2 steeringForce = subtract(desiredVelocity, getVelocity());
        return truncate(steeringForce, getMaxForce());
    }

    // 3. Comportamento WANDER (Aleatório)
    private Vector2 calculateWander(World world) {
        // 1. Centro do círculo à frente
        Vector2 circleCenter = multiply(getDirection(), WANDER_CIRCLE_DISTANCE);

        // 2. Mudança suave de ângulo
        double angleChange = (RND.nextDouble(-1.0, 1.0) * WANDER_ANGLE_CHANGE) * world.getSecs();
        wanderAngle += angleChange;

        // 3. Posição do alvo na borda do círculo
        Vector2 displacement = byAngle(wanderAngle);
        displacement.multiply(WANDER_CIRCLE_RADIUS);

        // 4. Posição final do alvo no mundo (posição do carro + centro + deslocamento)
        Vector2 wanderTarget = add(getPosition(), circleCenter, displacement);

        // 5. Seek para o alvo de Wander
        Vector2 desiredVelocity = subtract(wanderTarget, getPosition());
        desiredVelocity.resize(getMaxSpeed());

        Vector2 steeringForce = subtract(desiredVelocity, getVelocity());

        return truncate(steeringForce, getMaxForce());
    }

    /**
     * Deve calcular o steering behavior para esse carro
     * O parametro world contem diversos metodos utilitários:
     * world.getClickPos(): Retorna um vector2D com a posição do último click,
     * ou nulo se nenhum click foi dado ainda
     * - world.getMousePos(): Retorna um vector2D com a posição do cursor do mouse
     * - world.getNeighbors(): Retorna os carros vizinhos. Não inclui o próprio carro.
     * Opcionalmente, você pode passar o raio da vizinhança. Se o raio não for
     * fornecido retornará os demais carros.
     * - world.getSecs(): Indica quantos segundos transcorreram desde o último quadro
     * Você ainda poderá chamar os seguintes metodos do carro para obter informações:
     * - getDirection(): Retorna um vetor unitário com a direção do veículo
     * - getPosition(): Retorna um vetor com a posição do carro
     * - getMass(): Retorna a massa do carro
     * - getMaxSpeed(): Retorna a velocidade de deslocamento maxima do carro em píxeis / s
     * - getMaxForce(): Retorna a forca maxima que pode ser aplicada sobre o carro
     */


}
