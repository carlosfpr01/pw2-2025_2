package edu.ifrs.business;
/**
 * Serviço responsável por validar regras de peso.
 */
public interface WeightService {
    /**
     * Verifica se o total de peso está dentro do limite permitido.
     */
    boolean isWeightAllowed(int totalWeight, int maxWeightLimit);
}
