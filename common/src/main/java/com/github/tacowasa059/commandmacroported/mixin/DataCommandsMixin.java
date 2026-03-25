package com.github.tacowasa059.commandmacroported.mixin;

import com.github.tacowasa059.commandmacroported.accessor.DataCommandsAccessor;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.commands.arguments.NbtPathArgument;
import net.minecraft.nbt.Tag;
import net.minecraft.server.commands.data.DataAccessor;
import net.minecraft.server.commands.data.DataCommands;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Collection;
import java.util.Iterator;


@Mixin(DataCommands.class)
public class DataCommandsMixin implements DataCommandsAccessor{

    @Final
    @Shadow
    private static SimpleCommandExceptionType ERROR_MULTIPLE_TAGS;

    public Tag commandmacroported$getSingleTag(NbtPathArgument.NbtPath p_139399_, DataAccessor p_139400_)
            throws CommandSyntaxException {
        Collection<Tag> $$2 = p_139399_.get(p_139400_.getData());
        Iterator<Tag> $$3 = $$2.iterator();
        Tag $$4 = $$3.next();
        if ($$3.hasNext()) {
            throw ERROR_MULTIPLE_TAGS.create();
        } else {
            return $$4;
        }
    }
}
