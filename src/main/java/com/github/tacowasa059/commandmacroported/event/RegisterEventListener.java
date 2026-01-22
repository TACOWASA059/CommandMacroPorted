package com.github.tacowasa059.commandmacroported.event;

import com.github.tacowasa059.commandmacroported.Commandmacroported;
import com.github.tacowasa059.commandmacroported.ported.RandomCommand;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Commandmacroported.MODID)
public class RegisterEventListener {
    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        RandomCommand.register(event.getDispatcher());
    }

}
