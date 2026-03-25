package com.github.tacowasa059.commandmacroported.accessor;

import com.github.tacowasa059.commandmacroported.ported.FunctionInstantiationException;
import net.minecraft.commands.CommandFunction;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.ServerFunctionManager;

import javax.annotation.Nullable;

public interface ServerFunctionManagerAccessor {
    int commandmacroported$execute(CommandFunction commandFunction, CommandSourceStack p_179962_,
                                   @Nullable ServerFunctionManager.TraceCallbacks p_179963_,
                                   @Nullable CompoundTag p_300204_) throws FunctionInstantiationException;
}
