package com.github.tacowasa059.commandmacroported.mixin;

import com.github.tacowasa059.commandmacroported.accessor.ServerFunctionManagerAccessor;
import com.github.tacowasa059.commandmacroported.ported.FunctionInstantiationException;
import com.github.tacowasa059.commandmacroported.ported.Tracer;
import com.mojang.logging.LogUtils;
import net.minecraft.Util;
import net.minecraft.commands.CommandFunction;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.commands.DebugCommand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.slf4j.Logger;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UncheckedIOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;

@Mixin(DebugCommand.class)
public class DebugCommandsMixin {
    @Unique
    private static final Logger commandmacroported$LOGGER = LogUtils.getLogger();

    @Inject(method = "traceFunction", at=@At("HEAD"), cancellable = true)
    private static void traceFunction(CommandSourceStack p_180066_, Collection<CommandFunction> p_180067_,
                                      CallbackInfoReturnable<Integer> cir) {
        int i = 0;
        MinecraftServer minecraftserver = p_180066_.getServer();
        String s = "debug-trace-" + Util.getFilenameFormattedDateTime() + ".txt";

        try {
            Path path = minecraftserver.getFile("debug").toPath();
            Files.createDirectories(path);

            try (Writer writer = Files.newBufferedWriter(path.resolve(s), StandardCharsets.UTF_8)) {
                PrintWriter printwriter = new PrintWriter(writer);

                for(CommandFunction commandfunction : p_180067_) {
                    printwriter.println((Object)commandfunction.getId());
                    Tracer debugcommand$tracer = new Tracer(printwriter);

                    try {
                        i += ((ServerFunctionManagerAccessor) p_180066_.getServer().getFunctions()).commandmacroported$execute(commandfunction, p_180066_.withSource(debugcommand$tracer).withMaximumPermission(2), debugcommand$tracer, (CompoundTag)null);
                    } catch (FunctionInstantiationException functioninstantiationexception) {
                        p_180066_.sendFailure(functioninstantiationexception.messageComponent());
                    }
                }
            }
        } catch (IOException | UncheckedIOException uncheckedioexception) {
            commandmacroported$LOGGER.warn("Tracing failed", (Throwable)uncheckedioexception);
            p_180066_.sendFailure(Component.translatable("commands.debug.function.traceFailed"));
        }

        int j = i;
        if (p_180067_.size() == 1) {
            p_180066_.sendSuccess(() -> {
                return Component.translatable("commands.debug.function.success.single", j, p_180067_.iterator().next().getId(), s);
            }, true);
        } else {
            p_180066_.sendSuccess(() -> {
                return Component.translatable("commands.debug.function.success.multiple", j, p_180067_.size(), s);
            }, true);
        }

        cir.setReturnValue(i);
        cir.cancel();
    }


}
