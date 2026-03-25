package com.github.tacowasa059.commandmacroported;

import com.github.tacowasa059.commandmacroported.ported.RandomCommand;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.fml.common.Mod;

@Mod(CommandMacroPortedCommon.MOD_ID)
public final class CommandMacroPortedForge {
    public CommandMacroPortedForge() {
        CommandMacroPortedCommon.init();
        MinecraftForge.EVENT_BUS.addListener(CommandMacroPortedForge::onRegisterCommands);
    }

    private static void onRegisterCommands(RegisterCommandsEvent event) {
        RandomCommand.register(event.getDispatcher());
    }
}
