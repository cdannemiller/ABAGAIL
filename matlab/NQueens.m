clear all;
javaaddpath ..\ABAGAIL.jar;

import dist.*;
import opt.*;
import opt.ga.*;
import opt.prob.*;
import shared.*;
import util.*;

N = 10;
%ranges = randi([-2^31,2^31],N,1);

ef = NQueensFitnessFunction();
odd = DiscretePermutationDistribution(N);
nf = SwapNeighbor();
mf = SwapMutation();
cf = SingleCrossOver();
df = DiscreteDependencyTree(.1); 
hcp = GenericHillClimbingProblem(ef, odd, nf);
gap = GenericGeneticAlgorithmProblem(ef, odd, mf, cf);
pop = GenericProbabilisticOptimizationProblem(ef, odd, df);
        
starttime = tic;
rhc = RandomizedHillClimbing(hcp);      
fit = FixedIterationTrainer(rhc, 100);
fit.train();
fprintf('RHC: %f\n', ef.value(rhc.getOptimal()));
%fprintf('RHC: Board Position: \n');
%fprintf('%s', ef.boardPositions());
fprintf('Time : %f\n', toc(starttime));
fprintf('============================\n');
        
starttime = tic;
sa = SimulatedAnnealing(1E1, .1, hcp);
fit = FixedIterationTrainer(sa, 100);
fit.train();
fprintf('SA:  %f\n', ef.value(rhc.getOptimal()));
%fprintf('SA:  Board Position: \n');
%fprintf('%s', ef.boardPositions());
fprintf('Time : %f\n', toc(starttime));
fprintf('============================\n');

starttime = tic;
ga = StandardGeneticAlgorithm(200, 0, 10, gap);
fit = FixedIterationTrainer(ga, 100);
fit.train();
fprintf('GA: %f\n', ef.value(rhc.getOptimal()));
%fprintf('GA: Board Position: \n');
%fprintf('%s', ef.boardPositions());
fprintf('Time : %f\n', toc(starttime));
fprintf('============================\n');

starttime = tic;
mimic = MIMIC(200, 10, pop);
fit = FixedIterationTrainer(mimic, 5);
fit.train();
fprintf('MIMIC: %f\n', ef.value(rhc.getOptimal()));
%fprintf('MIMIC: Board Position: \n');
%fprintf('%s', ef.boardPositions());
fprintf('Time : %f\n', toc(starttime));
fprintf('============================\n');
