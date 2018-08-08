package edu.southwestern.tasks.mario;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import ch.idsia.mario.engine.level.Level;
import edu.southwestern.MMNEAT.MMNEAT;
import edu.southwestern.evolution.genotypes.Genotype;
import edu.southwestern.tasks.mario.gan.MarioGANUtil;
import edu.southwestern.util.datastructures.ArrayUtil;

/**
 * 
 * Evolve Mario levels with latent vectors 
 * for the Mario GAN using an agent,
 * like the Mario A* Agent, as a means of evaluating
 * 
 * @author Jacob Schrum
 *
 * @param <T> real vector
 */
public class MarioGANLevelTask extends MarioLevelTask<ArrayList<Double>> {
	public MarioGANLevelTask() {
		super();
	}

	/**
	 * Extract real-valued latent vector from genotype and then send to GAN to get a Mario level
	 */
	@Override
	public Level getMarioLevelFromGenotype(Genotype<ArrayList<Double>> individual) {
		ArrayList<Double> latentVector = individual.getPhenotype();
		double[] doubleArray = ArrayUtil.doubleArrayFromList(latentVector);
		Level level = MarioGANUtil.generateLevelFromGAN(doubleArray);
		return level;
	}

	/**
	 * For quick testing
	 * @param args
	 * @throws FileNotFoundException
	 * @throws NoSuchMethodException
	 */
	public static void main(String[] args) throws FileNotFoundException, NoSuchMethodException {
		MMNEAT.main("runNumber:0 randomSeed:0 trials:1 mu:10 maxGens:500 io:false netio:false genotype:edu.southwestern.evolution.genotypes.BoundedRealValuedGenotype mating:true fs:false task:edu.southwestern.tasks.mario.MarioGANLevelTask saveAllChampions:false cleanOldNetworks:true logTWEANNData:false logMutationAndLineage:false marioLevelLength:120 marioStuckTimeout:20 watch:true".split(" "));
	}

}
