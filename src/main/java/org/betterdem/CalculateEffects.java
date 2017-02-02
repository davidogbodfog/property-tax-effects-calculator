package org.betterdem;

import org.moeaframework.Executor;
import org.moeaframework.core.NondominatedPopulation;
import org.moeaframework.core.Solution;
import org.moeaframework.core.variable.EncodingUtils;

public class CalculateEffects
{
    public static void main( String[] args )
    {
        System.out.println( "Starting optimization...");

        NondominatedPopulation result = new Executor()
                .withProblemClass(CurrentStateInferenceProblem.class)
                .withAlgorithm("BDNCustom")
                .withMaxEvaluations(100000)
                .distributeOnAllCores()
                .run();

        System.out.println("number of non-dominated results:" + result.size());



        // best overall
        double minScore = 10E20;
        Solution bestOverall = null;
        for (Solution solution : result) {
            double overallScore = 0;
            for (int i = 0; i < CurrentStateModel.NUM_OBJECTIVES; i++) {
                overallScore += solution.getObjective(i);
            }
            if (overallScore < minScore){
                bestOverall = solution;
                minScore = overallScore;
            }
        }

        System.out.println("best overall result (unweighted sum):");
        System.out.println(minScore);

        double[] individual = EncodingUtils.getReal(bestOverall);
        CurrentStateModel model = CurrentStateModel.decodeFromIndividual(individual);
        System.out.println("some households in this result ....");
        for (int i = 0; i < 10; i++) {
            System.out.println(model.households.get(i).getListRepr());
        }

//
//        // display the results
//        System.out.println("non-dominated results:");
//        for (Solution solution : result) {
//            double[] individual = EncodingUtils.getReal(solution);
//            CurrentStateModel model = CurrentStateModel.decodeFromIndividual(individual);
//            System.out.println("some households in this result ....");
//            for (int i = 0; i < 10; i++) {
//                System.out.println(model.households.get(i).getListRepr());
//            }
//        }


    }
}
