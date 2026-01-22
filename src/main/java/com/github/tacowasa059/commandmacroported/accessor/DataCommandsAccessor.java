package com.github.tacowasa059.commandmacroported.accessor;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.arguments.NbtPathArgument;
import net.minecraft.nbt.Tag;
import net.minecraft.server.commands.data.DataAccessor;

public interface DataCommandsAccessor {
    Tag commandmacroported$getSingleTag(NbtPathArgument.NbtPath p_139399_,
                                        DataAccessor p_139400_) throws CommandSyntaxException;
}
