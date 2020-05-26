package me.crupette.surrealbiomes.world.feature;

import com.mojang.datafixers.Dynamic;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorConfig;
import net.minecraft.world.gen.feature.Feature;

import java.util.Random;
import java.util.function.Function;

public class SpikesFeature extends Feature<SpikesFeatureConfig> {
    public SpikesFeature(Function<Dynamic<?>, ? extends SpikesFeatureConfig> configFactory) {
        super(configFactory);
    }

    @Override
    public boolean generate(IWorld world, ChunkGenerator<? extends ChunkGeneratorConfig> generator, Random random, BlockPos pos, SpikesFeatureConfig config) {
        int radius = random.nextInt(config.maxRadius - config.minRadius) + config.minRadius;
        BlockState color = config.compositionBlocks.get(random.nextInt(config.compositionBlocks.size()));
        for(int x = -radius; x <= radius; x++)
            for(int z = -radius; z <= radius; z++){
                if(Math.abs(x) + Math.abs(z) > radius) continue;
                int height = random.nextInt(Math.abs((int) (config.maxHeight - (Math.abs(x + z) * (1.f / config.falloff)))));

                BlockPos cpos = pos.add(x, 0, z);
                do {
                    cpos = cpos.down();
                }while (!config.surfaceBlocks.contains(world.getBlockState(cpos).getBlock().getDefaultState()) && cpos.getY() > 1);

                for(int y = 0; y < height; y++){
                    world.setBlockState(cpos, color, 2);
                    cpos = cpos.up();
                }
            }
        return true;
    }
}
