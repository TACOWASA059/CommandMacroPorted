package com.github.tacowasa059.commandmacroported;

import com.github.tacowasa059.commandmacroported.ported.RandomCommand;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

public final class CommandMacroPortedFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        CommandMacroPortedCommon.init();
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> RandomCommand.register(dispatcher));
    }
}
