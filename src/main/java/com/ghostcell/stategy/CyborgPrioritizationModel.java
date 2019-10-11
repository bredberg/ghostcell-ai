package com.ghostcell.stategy;

import com.ghostcell.GameState;
import com.ghostcell.container.Factory;
import com.ghostcell.priomodel.FactoryPrio;
import com.ghostcell.priomodel.PrioList;
import com.ghostcell.priomodel.Weight;

public class CyborgPrioritizationModel {

    private GameState gameState;

    public CyborgPrioritizationModel(GameState gameState) {
        this.gameState = gameState;
    }

    public PrioList getPrioList(Factory originFactory) {

        PrioList prioList = new PrioList();

        for(Factory target : gameState.getFactories()) {

            FactoryPrio factoryPrio = new FactoryPrio(originFactory, target);

            if(target.getId() == originFactory.getId())
                continue;


            double distanceImportance = 0.9;
            double productionImportance = 0.2;

            factoryPrio.addWeight(new Weight()
                    .setLabel("dist")
                    .setMaxValue(20)
                    .setValue(originFactory.distanceTo(target))
                    .setReverse(true)
                    .setImportance(distanceImportance));

            factoryPrio.addWeight(new Weight() // target production
                    .setLabel("t_prod")
                    .setMaxValue(3)
                    .setValue(target.getProduction())
                    .setReverse(false)
                    .setImportance(productionImportance));


            double prio = factoryPrio.calculatePreliminaryPrio();

            if(prio == 0) {
                prio = 0.001;
            }

            factoryPrio.setFactoryPrio(prio);
            prioList.add(factoryPrio);
        }

        return prioList.sort();
    }

    private double normalize(double valueMax, double value, boolean reverse) {
        double norm = Math.max(value, 0.00001) / valueMax;
        return reverse ? 1-norm : norm;
    }
}
