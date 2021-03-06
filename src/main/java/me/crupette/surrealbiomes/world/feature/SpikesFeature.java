package me.crupette.surrealbiomes.world.feature;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;

import java.util.Random;

public class SpikesFeature extends Feature<SpikesFeatureConfig> {
    public SpikesFeature(Codec<SpikesFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public boolean generate(ServerWorldAccess serverWorldAccess, StructureAccessor accessor, ChunkGenerator generator, Random random, BlockPos pos, SpikesFeatureConfig config) {
        int radius = random.nextInt(config.maxRadius - config.minRadius) + config.minRadius;
        BlockState color = config.compositionBlocks.get(random.nextInt(config.compositionBlocks.size()));
        for(int x = -radius; x <= radius; x++)
            for(int z = -radius; z <= radius; z++){
                int dist = Math.abs(x) + Math.abs(z);
                if(dist > radius) continue;
                double heightBounds = (config.maxHeight * 1.5) -((config.maxHeight >> 1) *
                        (Math.pow(2.71828, Math.pow(z, 2) / Math.pow(2 * config.falloff, 2)) *
                                Math.pow(2.71828, Math.pow(x, 2) / Math.pow(2 * config.falloff, 2))));

                if((int)heightBounds <= 0) continue;
                int height = random.nextInt((int) heightBounds);

                BlockPos cpos = pos.add(x, 0, z);
                do {
                    cpos = cpos.down();
                }while (!config.surfaceBlocks.contains(serverWorldAccess.getBlockState(cpos).getBlock().getDefaultState()) && cpos.getY() > 1);

                for(int y = 0; y < height; y++){
                    serverWorldAccess.setBlockState(cpos, color, 2);
                    cpos = cpos.up();
                }
            }
        return true;
    }
}
