package edu.ifrs;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import edu.ifrs.business.Load;
import edu.ifrs.business.Vehicle;
import edu.ifrs.business.WeightService;
import io.quarkus.test.Mock;

@ExtendWith(MockitoExtension.class)
public class CarregarTest {

    @Mock
    private WeightService weightService;

    @Test
    public void testeAdicionaCargaNegativo(){
        when(weightService.isWeightAllowed(20, 100)).thenReturn(true);
        Vehicle vehicle = new Vehicle(100, weightService);
        vehicle.addWeight(new Load(20));
        assertFalse(vehicle.checkWeightLimit());
    }

}
