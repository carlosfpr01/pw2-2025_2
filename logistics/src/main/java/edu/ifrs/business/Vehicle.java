/**
 * VVS by Rodrigo Prestes Machado
 *
 * VVS is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 * work. If not, see <http://creativecommons.org/licenses/by/4.0/>.
*/
package edu.ifrs.business;

import java.util.ArrayList;
import java.util.List;

public class Vehicle {

    private int maximumWeightLimit;
    private List<Load> loads = new ArrayList<>();
    private WeightService weightService;

    public Vehicle(int weightLimit, WeightService weightService) {
        this.maximumWeightLimit = weightLimit;
        this.weightService = weightService;
    }

    public void addWeight(Load load) {
        loads.add(load);
    }

    public boolean checkWeightLimit() {
        int total = loads.stream().mapToInt(Load::getWeight).sum();
        // delega a verificação ao serviço externo
        return weightService.isWeightAllowed(total, maximumWeightLimit);
    }

    public int getMaximumWeightLimit() {
        return maximumWeightLimit;
    }

    public List<Load> getLoads() {
        return loads;
    }
}