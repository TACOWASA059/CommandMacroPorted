package com.github.tacowasa059.commandmacroported.mixin;

import com.github.tacowasa059.commandmacroported.accessor.ServerFunctionManagerAccessor;
import com.github.tacowasa059.commandmacroported.ported.FunctionInstantiationException;
import com.github.tacowasa059.commandmacroported.ported.PortedCommandFunction;
import net.minecraft.commands.CommandFunction;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.ServerFunctionManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import javax.annotation.Nullable;

@Mixin(ServerFunctionManager.class)
public abstract class ServerFunctionManagerMixin implements ServerFunctionManagerAccessor {

    @Shadow
    public abstract int execute(CommandFunction p_179961_, CommandSourceStack p_179962_, @Nullable ServerFunctionManager.TraceCallbacks p_179963_);

    public int commandmacroported$execute(CommandFunction precommandFunction, CommandSourceStack p_179962_,
                                          @Nullable ServerFunctionManager.TraceCallbacks p_179963_, @Nullable CompoundTag p_300204_)
            throws FunctionInstantiationException {
        ServerFunctionManager manager = (ServerFunctionManager) (Object) this;
        CommandFunction commandfunction = ((PortedCommandFunction)precommandFunction).instantiate(p_300204_, manager.getDispatcher(), p_179962_);
        return execute(commandfunction, p_179962_, p_179963_);
    }
}
