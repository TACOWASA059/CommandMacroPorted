package com.github.tacowasa059.commandmacroported.accessor;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.RandomSequence;

import java.util.function.BiConsumer;

public interface RandomSequenceAccessor {
    void forAllSequences(BiConsumer<ResourceLocation, RandomSequence> p_299883_);
    int clear();
    void reset(ResourceLocation p_298741_);
    void reset(ResourceLocation p_301350_, int p_298554_, boolean p_298049_, boolean p_301283_);
    void setSeedDefaults(int p_299968_, boolean p_298395_, boolean p_298518_);
    void putSequence(ResourceLocation resourceLocation, RandomSequence randomsequence);
}
