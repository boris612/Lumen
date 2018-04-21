package hr.fer.zpr.lumen.math;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hr.fer.zpr.lumen.LogUtil;

/**
 * Created by Alen on 18.12.2017..
 */

public class ProbabilityDistribution {
    private Map<Object, Double> distributionMap;

    public ProbabilityDistribution() {
        distributionMap = new HashMap<>();
    }

    public class DistributionInterval {
        private Object intervalChoice;
        private double intervalStart;
        private double intervalEnd;

        public DistributionInterval(Object intervalChoice, double intervalStart, double intervalEnd) {
            this.intervalChoice = intervalChoice;
            this.intervalStart = intervalStart;
            this.intervalEnd = intervalEnd;
        }

        public Object getIntervalChoice() {
            return intervalChoice;
        }

        public void setIntervalChoice(Object intervalChoice) {
            this.intervalChoice = intervalChoice;
        }

        public double getIntervalStart() {
            return intervalStart;
        }

        public void setIntervalStart(double intervalStart) {
            this.intervalStart = intervalStart;
        }

        public double getIntervalEnd() {
            return intervalEnd;
        }

        public void setIntervalEnd(double intervalEnd) {
            this.intervalEnd = intervalEnd;
        }
    }

    public void resetProbabilitiesToUniformDistribution() {
        double choiceProbability = 1. / distributionMap.size();
        for (Object choice : distributionMap.keySet()) {
            distributionMap.put(choice, choiceProbability);
        }
    }

    public void addChoice(Object choice) {
        distributionMap.put(choice, 0.);
        resetProbabilitiesToUniformDistribution();
    }

    public void increaseChoiceProbabilityByScaleFactor(Object choice, double scaleFactor) {
        if (!distributionMap.containsKey(choice)) {
            throw new IllegalArgumentException();
        }
        double currentProb = distributionMap.get(choice);
        double newProb;
        if (scaleFactor < 0) {
            throw new IllegalArgumentException();
        }
        newProb = currentProb * scaleFactor;
        if (newProb > 1) {
            newProb = 1;
        }

        double otherProbsScaleFactor = (1 - newProb) / (1 - currentProb);
        scaleAllProbabilitiesExcluding(otherProbsScaleFactor, choice);
        distributionMap.put(choice, newProb);
    }

    private void scaleAllProbabilitiesExcluding(double scaleFactor, Object toBeExcluded) {
        for (Map.Entry<Object, Double> entry : distributionMap.entrySet()) {
            if (entry.getKey().equals(toBeExcluded)) {
                continue;
            }
            distributionMap.put(entry.getKey(), entry.getValue() * scaleFactor);
        }
    }

    public void increaseChoiceProbability(Object choice, double probabilityDiff) {
        double currentProb = distributionMap.get(choice);
        increaseChoiceProbabilityByScaleFactor(choice, 1 + (currentProb / probabilityDiff));
    }

    public Collection<DistributionInterval> getDistributionAsIntervalCollection() {
        List<DistributionInterval> intervals = new ArrayList<>();
        double intervalBorder = 0;
        LogUtil.d("DISTR", distributionMap.toString());
        for (Map.Entry<Object, Double> entry : distributionMap.entrySet()) {
            double intervalStart = intervalBorder;
            double intervalEnd = intervalStart + entry.getValue();
            intervals.add(new DistributionInterval(entry.getKey(), intervalStart, intervalEnd));
            intervalBorder = intervalEnd;
            LogUtil.d("DISTR", "INTERVAL" + intervalStart + " " + intervalEnd + ", choice:" + entry.getKey());
        }
        return intervals;
    }
}
