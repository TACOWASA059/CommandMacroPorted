package com.github.tacowasa059.commandmacroported.mixin;

import net.minecraft.commands.arguments.NbtPathArgument;
import net.minecraft.nbt.Tag;
import net.minecraft.server.commands.data.DataAccessor;
import net.minecraft.server.commands.data.DataCommands;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;


@Mixin(DataCommands.class)
public interface DataCommandsMixin{


    @Invoker("getSingleTag")
    static  Tag commandmacroported$getSingleTag(NbtPathArgument.NbtPath p_139399_, DataAccessor p_139400_) {
        throw new AssertionError();
    }
}
