package cars.student;

import cars.engine.Car;
import cars.engine.Vector2;
import cars.engine.World;

import java.awt.*;
import java.util.ArrayList;
import java.util.random.RandomGenerator;

import static cars.engine.Vector2.*;
import static java.lang.Math.toRadians;
//Aqui tinha o básico que a professora passou, mas aí modificamos para fazer o carro AMARELO

public class StudentCarFollowPath extends Car {
    //Caminho que o carro roxo segue
    private static final Vector2[] Path = {
            new Vector2 (-480,330),
            new Vector2(490,330),
            new Vector2(490,10),
            new Vector2(-490,8),
            new Vector2(-490,-280),
            new Vector2(25,-285),
            new Vector2(30,-68),
            new Vector2(80,-68),
            new Vector2(80,-270),
            new Vector2(490,-282),
            new Vector2(490,8),
            new Vector2(-490,8)
    };
    private static int i = 0;

    public StudentCarFollowPath(Color cor, double x, double y) {
        super(settings ->
          settings
            .color(cor)
            .randomOrientation()
            .position(500,300)
                  .maxForce(2000)
                  .maxSpeed(150)
                  .mass(0.2)

        );
    }
    @Override
    public Vector2 calculateSteering(final World world) {
        // Este if verifica se o carro chegou no alvo
        if ((this.getPosition().x >= Path[i].x-2 &&  this.getPosition().x <= Path[i].x+2)&& (this.getPosition().y >= Path[i].y -2 && this.getPosition().y <= Path[i].y+2)){
            i++; // progressao na lista
        if (i >= Path.length) {i = 0;}} // verifica se chegou no final ta lista e reseta para o começo.

        final Vector2 targetPos = Path[i];
        Vector2 desiredVelocity = subtract(targetPos, getPosition());
        desiredVelocity.resize(getMaxSpeed());
        return subtract(desiredVelocity, getVelocity());
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
