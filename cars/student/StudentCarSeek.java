package cars.student;

// Não precisamos mais importar Car, pois StudentCarBase já o faz
import cars.engine.Vector2;
import cars.engine.World;
import cars.engine.Car;

import java.awt.*;
import java.util.List; // Necessário para getNeighbors()

// Importamos todos os métodos estáticos do Vector2
import static cars.engine.Vector2.*;


// 1. MUDANÇA DA HERANÇA: Agora estende StudentCarBase
public class StudentCarSeek extends StudentCarBase {
    // A variável 'state' não é necessária se o carro só faz um comportamento

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

    /**
     * 2. MUDANÇA DO MÉTODO: Implementa o método calculateBehaviorForce.
     * Este método contém APENAS a lógica de Busca (Seek) do alvo.
     */
    @Override
    public Vector2 calculateBehaviorForce(final World world) {

        // Obter os vizinhos e verificar se a lista não está vazia
        List<Car> neighbors = world.getNeighbors();
        if (neighbors.isEmpty()) {
            return vec2();
        }

        // Definir o alvo como a posição do último vizinho
        // NOTA: Para um Seek efetivo, você deve buscar o mouse ou um ponto fixo,
        // buscar um vizinho fará ele "perseguir" o último na lista.
        final Vector2 targetPos = neighbors.getLast().getPosition();

        // Lógica de SEEK (Buscar)

        // 1. Calcular o vetor do carro até o alvo (Direção de Busca)
        Vector2 desiredVelocity = subtract(targetPos, getPosition());

        // 2. Definir a Velocidade Desejada (Direção * Velocidade Máxima)
        desiredVelocity.resize(getMaxSpeed());

        // 3. Calcular a Força de Direção (Steering Force)
        // Força = Velocidade Desejada - Velocidade Atual

        // Retorna a força bruta de Seek. O StudentCarBase se encarrega da soma com Avoidance e do truncate.
        return subtract(desiredVelocity, getVelocity());
    }

    // O bloco de Javadoc que estava no final foi removido, pois era desnecessário e causava erro.
}