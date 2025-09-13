package edu.ifrs;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import edu.ifrs.business.Load;
import edu.ifrs.business.Vehicle;

public class CarregarTest {
    
    @Test
    public void testeAdicionaCargaNegativo(){
        Vehicle veiculo = new Vehicle(100);
        veiculo.addWeight(new Load(-1));
        assertTrue(veiculo.checkWeightLimit());
    }

    @Test
    public void testeAdicionaCarga0(){
        Vehicle veiculo = new Vehicle(0);
        veiculo.addWeight(new Load(0));
        assertTrue(veiculo.checkWeightLimit());
    }

    @Test
    public void testeAdicionaCarga1(){
        Vehicle veiculo = new Vehicle(1);
        veiculo.addWeight(new Load(1));
        assertTrue(veiculo.checkWeightLimit());
    }

    @Test
    public void testeAdicionaCargaMax(){
        Vehicle veiculo = new Vehicle(100);
        veiculo.addWeight(new Load(100));
        assertTrue(veiculo.checkWeightLimit());
    }

    @Test
    public void testeAdicionaCargaMaiorMax(){
        Vehicle veiculo = new Vehicle(100);
        veiculo.addWeight(new Load(150));
        assertFalse(veiculo.checkWeightLimit());
    }

    

}
