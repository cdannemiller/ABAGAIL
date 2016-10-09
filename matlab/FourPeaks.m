clear all;
clc;

javaaddpath ..\ABAGAIL.jar

import dist.DiscreteDependencyTree
import dist.DiscreteUniformDistribution
import dist.Distribution
import opt.DiscreteChangeOneNeighbor
import opt.EvaluationFunction
import opt.GenericHillClimbingProblem
import opt.HillClimbingProblem
import opt.NeighborFunction
import opt.RandomizedHillClimbing
import opt.SimulatedAnnealing
import opt.example.FourPeaksEvaluationFunction
import opt.ga.CrossoverFunction
import opt.ga.SingleCrossOver
import opt.ga.DiscreteChangeOneMutation
import opt.ga.GenericGeneticAlgorithmProblem
import opt.ga.GeneticAlgorithmProblem
import opt.ga.MutationFunction
import opt.ga.StandardGeneticAlgorithm
import opt.ga.UniformCrossOver
import opt.prob.GenericProbabilisticOptimizationProblem
import opt.prob.MIMIC
import opt.prob.ProbabilisticOptimizationProblem
import shared.FixedIterationTrainer

N=200;
T=N/5;
ranges = ones(N,1)*2;

ef = FourPeaksEvaluationFunction(T);
odd = DiscreteUniformDistribution(ranges);
nf = DiscreteChangeOneNeighbor(ranges);
mf = DiscreteChangeOneMutation(ranges);
cf = SingleCrossOver();
df = DiscreteDependencyTree(.1, ranges);
hcp = GenericHillClimbingProblem(ef, odd, nf);
gap = GenericGeneticAlgorithmProblem(ef, odd, mf, cf);
pop = GenericProbabilisticOptimizationProblem(ef, odd, df);

rhc = RandomizedHillClimbing(hcp);
fit = FixedIterationTrainer(rhc, 200000);
fit.train();
fprintf('RHC: %f\n', ef.value(rhc.getOptimal()));

sa = SimulatedAnnealing(1E11, .95, hcp);
fit = FixedIterationTrainer(sa, 200000);
fit.train();
fprintf('SA:  %f\n', ef.value(sa.getOptimal()));

ga = StandardGeneticAlgorithm(200, 100, 10, gap);
fit = FixedIterationTrainer(ga, 1000);
fit.train();
fprintf('GA:  %f\n', ef.value(sa.getOptimal()));

mimic = MIMIC(200, 20, pop);
fit = FixedIterationTrainer(mimic, 1000);
fit.train();
fprintf('MIMIC:  %f\n', ef.value(sa.getOptimal()));
