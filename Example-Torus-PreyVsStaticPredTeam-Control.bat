java -jar dist/MM-NEATv2.jar runNumber:%1 randomSeed:%1 base:torus trials:10 maxGens:150 mu:100 io:true netio:true mating:false fs:true task:edu.utexas.cs.nn.tasks.gridTorus.TorusEvolvedPreyVsStaticPredatorsTask log:PreyVsStaticPredTeam-Control saveTo:Control allowDoNothingActionForPredators:true torusPreys:2 torusPredators:10 torusSenseTeammates:true