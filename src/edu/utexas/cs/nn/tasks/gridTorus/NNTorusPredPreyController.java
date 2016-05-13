
package edu.utexas.cs.nn.tasks.gridTorus;

/**
 * Imports needed parts to initialize the Controller, as in Torus agent and world, the controller, network, and statistic utilities.
 */
import edu.utexas.cs.nn.gridTorus.TorusAgent;
import edu.utexas.cs.nn.gridTorus.TorusWorld;
import edu.utexas.cs.nn.gridTorus.controllers.TorusPredPreyController;
import edu.utexas.cs.nn.networks.Network;
import edu.utexas.cs.nn.parameters.Parameters;
import edu.utexas.cs.nn.util.stats.StatisticsUtilities;

/**
 *
 * @author Jacob Schrum, Gabby Gonzalez
 * The following class extends the normal TorusPredPreyController to allow for a neural network.
 */
public class NNTorusPredPreyController extends TorusPredPreyController {
	/**
	 * Initializes the network to be used.
	 */
	private final Network nn;
	//true if this agent is a predator
	private final boolean isPredator;
	/**
	 * Takes in network and connects it to the controller
	 * @param nn
	 * @param isPredator
	 */
	public NNTorusPredPreyController(Network nn, boolean isPredator){ 
		this.nn = nn;
		this.isPredator = isPredator;
	}

	/**
	 * Takes in all agents (me, world, preds, prey) to allow agent me to return best possible actions
	 * @param all agents (me, world, preds, prey)
	 * @return actions array
	 */
	@Override
	public int[] getAction(TorusAgent me, TorusWorld world, TorusAgent[] preds, TorusAgent[] prey) { 
		double[] inputs = inputs(me,world,preds,prey);
		double[] outputs = nn.process(inputs);
		// Assume one output for each direction
		return isPredator ? PREDATOR_ACTIONS[StatisticsUtilities.argmax(outputs)] : PREY_ACTIONS[StatisticsUtilities.argmax(outputs)];
	}

	/**
	 * gets the offsets from this agent to all prey agents 
	 * @param me this agent
	 * @param world
	 * @param prey
	 * @return the offsets to prey
	 */
	public double[] getPreyOffsets(TorusAgent me, TorusWorld world, TorusAgent[] prey){
		double[] preyInputs = new double[prey.length*2];
		for(int i = 0; i < prey.length; i++) {
			preyInputs[(2*i)] = me.shortestXOffset(prey[i]) / (1.0*world.width());
			preyInputs[(2*i)+1] = me.shortestYOffset(prey[i]) / (1.0*world.height()); 
		}
		return preyInputs;
	}

	/**
	 * gets the offsets from this agent to all predator agents 
	 * @param me this agent
	 * @param world
	 * @param preds
	 * @return the offsets to predators
	 */
	public double[] getPredatorOffsets(TorusAgent me, TorusWorld world, TorusAgent[] preds){
		double[] predInputs = new double[preds.length*2];
		for(int i = 0; i < preds.length; i++) {
			predInputs[(2*i)] = me.shortestXOffset(preds[i]) / (1.0*world.width());
			predInputs[(2*i)+1] = me.shortestYOffset(preds[i]) / (1.0*world.height()); 
		}
		return predInputs;
	}

	/**
	 * Calculates inputs for the neural network in order to figure what action to take in getAction.
	 * @param all agents (me, world, preds, prey)
	 * @return inputs for the network
	 */
	public double[] inputs(TorusAgent me, TorusWorld world, TorusAgent[] preds, TorusAgent[] prey) { 
		//if options to sense teammates is turned on, include the allies in the inputs (after enemies)
		if(Parameters.parameters.booleanParameter("torusSenseTeammates")){
			int numAgentsSensed = prey.length + preds.length;
			double[] inputs = new double[numAgentsSensed * 2];
			//if this is a predator
			if(isPredator){
				for(int i = 0; i < prey.length; i++) {
					inputs[(2*i)] = me.shortestXOffset(prey[i]) / (1.0*world.width());
					inputs[(2*i)+1] = me.shortestYOffset(prey[i]) / (1.0*world.height()); 
				}
				for(int i = 0; i < preds.length; i++) {
					inputs[(2*i) + prey.length] = me.shortestXOffset(preds[i]) / (1.0*world.width());
					inputs[(2*i)+1 + prey.length] = me.shortestYOffset(preds[i]) / (1.0*world.height()); 
				}
			}else{ //this is a prey
				for(int i = 0; i < preds.length; i++) {
					inputs[(2*i) + prey.length] = me.shortestXOffset(preds[i]) / (1.0*world.width());
					inputs[(2*i)+1 + prey.length] = me.shortestYOffset(preds[i]) / (1.0*world.height()); 
				}
				for(int i = 0; i < prey.length; i++) {
					inputs[(2*i)] = me.shortestXOffset(prey[i]) / (1.0*world.width());
					inputs[(2*i)+1] = me.shortestYOffset(prey[i]) / (1.0*world.height()); 
				}
			}
			return inputs;
		}else{ //the option to sense teammates is turned off, so just sense enemies
			int numAgentsSensed = isPredator ? prey.length : preds.length;
			double[] inputs = new double[numAgentsSensed * 2];
			for(int i = 0; i < numAgentsSensed; i++) {
				inputs[(2*i)] = me.shortestXOffset(isPredator ? prey[i] : preds[i]) / (1.0*world.width());
				inputs[(2*i)+1] = me.shortestYOffset(isPredator ? prey[i] : preds[i]) / (1.0*world.height()); 
			}

			return inputs;
		}
	}

	/**
	 * Sets up the sensor labels for sensors to be used in network visualization.
	 * @param numAgents number of agents to sense
	 * @param type is the type of genotype (predator or a prey) that will be sensed
	 * @return sensor labels for this genotype
	 */
	public static String[] sensorLabels(int numAgents, String type) {
		String[] result = new String[numAgents * 2];
		for(int i = 0; i < numAgents; i++) {
			result[(2*i)] = "X Offset to " + type + " " + i;
			result[(2*i)+1] = "Y Offset to " + type + " " + i; 
		}
		return result;
	}

}
