package com.github.tacowasa059.commandmacroported.ported;

import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.server.ServerFunctionManager;

public class ModSuggestionProviders {

    public static final SuggestionProvider<CommandSourceStack> SUGGEST_FUNCTION = (ctx, builder) -> {
        ServerFunctionManager mgr = ctx.getSource().getServer().getFunctions();
        SharedSuggestionProvider.suggestResource(mgr.getTagNames(), builder, "#");
        return SharedSuggestionProvider.suggestResource(mgr.getFunctionNames(), builder);
    };
}
