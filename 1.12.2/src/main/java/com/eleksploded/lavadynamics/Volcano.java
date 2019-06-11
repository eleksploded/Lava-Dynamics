package com.eleksploded.lavadynamics;

import java.util.Random;

import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.feature.WorldGenLakes;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class Volcano {
	
	
	public void OnChunkLoad(ChunkEvent.Load event) {
		//Get saved value for tested chunks
		VolcanoData data = VolcanoData.get(event.getWorld());
		//Get Chunk that was loaded
		Chunk chunk = event.getChunk();
		//Check if chunk is tested already
		if(!data.isChunkTested(chunk)){
			Random rand = new Random();
			//Random Chance to spawn a Volcano
			if(rand.nextInt(100)+1 <= Config.volcanoChance) {
				//Spawn a volcano
				genVolcano(chunk, event.getWorld());
			}
		}
	}

	public static void genVolcano(Chunk chunk, World world) {
		//----------Setup----------//
		
		boolean debug = Config.genVolcanoDebug;
		Random rand = new Random(world.getSeed());
		//Get the center of the chunk
		int x = (chunk.getPos().getXEnd() - chunk.getPos().getXStart())/2 + chunk.getPos().getXStart();
		int z = (chunk.getPos().getZEnd() - chunk.getPos().getZStart())/2 + chunk.getPos().getZStart();		
		//Get random y level based on config value
		int y = Config.volcanoYLevel + (rand.nextInt(5) - 2);
		//Get center of chunk into BlockPos
		BlockPos center = new BlockPos(x,y,z);
		
		if(debug) {
			LavaDynamics.Logger.info("Center Location is " + center);
		}
		
		//----------Lava Lake----------//
		
		//Generate new Lava lake at chunk center
		WorldGenLakes lakes = new WorldGenLakes(Blocks.LAVA);
		lakes.generate(world, rand, center);
		if(debug) {
			LavaDynamics.Logger.info("Lava lake generated");
		}
		
		//----------Lava Pillar---------//
		
		if(debug) {
			LavaDynamics.Logger.info("Generating Lava Pillar");
		}
		
		//Get hight and set pillar
		int height = chunk.getHeight(new BlockPos(x,y+5,z)) - center.getY();
		if(debug){
			LavaDynamics.Logger.info("Height for Generation is: " + height);
		}
		
		for(int i = 0; i <= height; i++) {
			//Add starting height in. Cast int cause it was treating them as Strings for some reason
			int j = (int)i + (int)center.getY();
			if(debug) {
				LavaDynamics.Logger.info("Placing Lava at y=" + j);
			}
			//Set Block, which causes block updates to smelt things
			world.setBlockState(new BlockPos(x,j,z), Blocks.LAVA.getDefaultState());
			//Pause for configured interval, I would use Timer() but I don't know how to set a fixed amount
			try {
				Thread.sleep(Config.timeToRise);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		if(debug) {
			LavaDynamics.Logger.info("Lava Pillar Done");
		}
		
		//----------Whatever comes next-----------//
	}
}
