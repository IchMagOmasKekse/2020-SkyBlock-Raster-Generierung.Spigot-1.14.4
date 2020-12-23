package me.skytale.de.generators;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;

public class SkyWorldGenerator extends ChunkGenerator {
		
	public SkyWorldGenerator() {
		
	}
	
	/*
	 * TODO: Kann als Post-ChunkGenerator verstanden werden
	 *
	 * - BlockPopulator werden dann eingesetzt, wenn die Chunks schon generiert sind,
	 *   und nun noch eine Chunkübergreifende Struktur geplaced werden muss(zum Beispiel Ein Dorf)
	 *
	 */
	public List<BlockPopulator> getDefaultPopulators(World world) {
		ArrayList<BlockPopulator> populators = new ArrayList<BlockPopulator>();
		return populators;
		
	}
	
	public ChunkData generateChunkData(World world, Random random, int chunkX, int chunkZ, BiomeGrid grid) {
		ChunkData data = createChunkData(world);
		
		for(int x = 0; x < 16; x++) {
			for(int z = 0; z < 16; z++) {
				data.setBlock(x, 0, z, Material.AIR);
			}
		}
		
		return data;
	}
	
	/*
	 * TODO: ?
	 */
	public ChunkGenerator getDefaultWorldGenerator(String worldName, String uid) {
		return new SkyWorldGenerator();
	}
}
