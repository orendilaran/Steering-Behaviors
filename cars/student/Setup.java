package cars.student;

import cars.engine.Car;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Setup {
    /**
     * Retorne uma lista com todos os carros que serão desenhados no exercício.
     */
    public List<Car> createCars() {
        List<Car> carros = new ArrayList<>();
        carros.add(new StudentCarMouse(Color.BLUE, -100,-100));
        carros.add(new StudentCarSeek(Color.RED, 200, 300));
        carros.add(new StudentCarFlee(Color.GREEN, 100, 100));
        return carros;
    }
}
