package com.github.tacowasa059.commandmacroported.mixin;

import com.github.tacowasa059.commandmacroported.accessor.RandomSequenceAccessor;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.RandomSequence;
import net.minecraft.world.RandomSequences;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;
import java.util.function.BiConsumer;

@Mixin(RandomSequences.class)
public class RandomSequencesMixin implements RandomSequenceAccessor {
    @Unique
    private long worldSeed;
    @Unique
    private int salt;
    @Unique
    private boolean includeWorldSeed = true;
    @Unique
    private boolean includeSequenceId = true;
    @Final
    @Shadow
    private static Logger LOGGER;
    @Final
    @Shadow
    private Map<ResourceLocation, RandomSequence> sequences;

    @Inject(method = "<init>", at=@At("RETURN"))
    private void Init(long p_287622_, CallbackInfo ci) {
        this.worldSeed = p_287622_;
    }

    @Inject(method = "save", at=@At("RETURN"), cancellable = true)
    public void save(CompoundTag p_287658_, CallbackInfoReturnable<CompoundTag> cir) {
        p_287658_.putInt("salt", this.salt);
        p_287658_.putBoolean("include_world_seed", this.includeWorldSeed);
        p_287658_.putBoolean("include_sequence_id", this.includeSequenceId);
        cir.setReturnValue(p_287658_);
    }

    @Unique
    private static boolean getBooleanWithDefault(CompoundTag p_297418_, String p_298953_, boolean p_297237_) {
        return p_297418_.contains(p_298953_, 1) ? p_297418_.getBoolean(p_298953_) : p_297237_;
    }

    @SuppressWarnings("removal")
    private static RandomSequences load(long p_287756_, CompoundTag p_287587_) {
        RandomSequences randomsequences = new RandomSequences(p_287756_);
        ((RandomSequenceAccessor)randomsequences).setSeedDefaults(p_287587_.getInt("salt"),
                getBooleanWithDefault(p_287587_, "include_world_seed", true),
                getBooleanWithDefault(p_287587_, "include_sequence_id", true));
        CompoundTag compoundtag = p_287587_.getCompound("sequences");

        for(String s : compoundtag.getAllKeys()) {
            try {
                RandomSequence randomsequence = RandomSequence.CODEC.decode(NbtOps.INSTANCE, compoundtag.get(s)).result().get().getFirst();
                ((RandomSequenceAccessor) randomsequences).putSequence(new ResourceLocation(s), randomsequence);
            } catch (Exception exception) {
                LOGGER.error("Failed to load random sequence {}", s, exception);
            }
        }

        return randomsequences;
    }

    public void forAllSequences(BiConsumer<ResourceLocation, RandomSequence> p_299883_) {
        this.sequences.forEach(p_299883_);
    }

    public int clear() {
        int i = this.sequences.size();
        this.sequences.clear();
        return i;
    }

    private RandomSequence createSequence(ResourceLocation p_299881_, int p_299267_, boolean p_300525_, boolean p_297272_) {
        long i = (p_300525_ ? this.worldSeed : 0L) ^ (long)p_299267_;
        return new RandomSequence(i, p_299881_);
    }

    private RandomSequence createSequence(ResourceLocation p_299723_) {
        return this.createSequence(p_299723_, this.salt, this.includeWorldSeed, this.includeSequenceId);
    }

    public void reset(ResourceLocation p_298741_) {
        this.sequences.put(p_298741_, this.createSequence(p_298741_));
    }

    public void reset(ResourceLocation p_301350_, int p_298554_, boolean p_298049_, boolean p_301283_) {
        this.sequences.put(p_301350_, this.createSequence(p_301350_, p_298554_, p_298049_, p_301283_));
    }

    public void setSeedDefaults(int p_299968_, boolean p_298395_, boolean p_298518_) {
        this.salt = p_299968_;
        this.includeWorldSeed = p_298395_;
        this.includeSequenceId = p_298518_;
    }

    @Unique
    public void putSequence(ResourceLocation resourceLocation, RandomSequence randomsequence){
        sequences.put(resourceLocation, randomsequence);
    }
}
