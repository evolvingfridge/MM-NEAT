cd ..
cd ..
java -jar target/MM-NEAT-0.0.1-SNAPSHOT.jar runNumber:%1 randomSeed:%1 base:tetris trials:3 maxGens:500 mu:50 io:true netio:true mating:true task:edu.southwestern.tasks.rlglue.tetris.TetrisTask rlGlueEnvironment:org.rlcommunity.environments.tetris.Tetris rlGlueExtractor:edu.southwestern.tasks.rlglue.featureextractors.tetris.RawTetrisStateExtractor tetrisTimeSteps:true tetrisBlocksOnScreen:false rlGlueAgent:edu.southwestern.tasks.rlglue.tetris.TetrisAfterStateAgent splitRawTetrisInputs:true senseHolesDifferently:true log:Tetris-HNSeedRawCNN saveTo:HNSeedRawCNN hyperNEATSeedTask:edu.southwestern.tasks.rlglue.tetris.HyperNEATTetrisTask substrateMapping:edu.southwestern.networks.hyperneat.BottomSubstrateMapping HNProcessDepth:1 netLinkRate:0.0 netSpliceRate:0.0 linkExpressionThreshold:-1 steps:500000 extraHNLinks:true convolution:true