package com.github.tacowasa059.commandmacroported.ported;


import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ImmutableList;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.nbt.*;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.ServerFunctionManager;

import javax.annotation.Nullable;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.*;

@SuppressWarnings("removal")
public class PortedCommandFunction extends net.minecraft.commands.CommandFunction {

    public PortedCommandFunction(ResourceLocation p_77979_, PortedCommandFunction.Entry[] p_77980_) {
        super(p_77979_, p_77980_);
    }
    public PortedCommandFunction instantiate(@Nullable CompoundTag p_300636_,
                                             CommandDispatcher<CommandSourceStack> p_300698_,
                                             CommandSourceStack p_298553_) throws FunctionInstantiationException {
        return this;
    }

    @VisibleForTesting
    public static PortedCommandFunction.MacroEntry decomposeMacro(String p_301200_, int p_300045_) {
        ImmutableList.Builder<String> builder = ImmutableList.builder();
        ImmutableList.Builder<String> builder1 = ImmutableList.builder();
        int i = p_301200_.length();
        int j = 0;
        int k = p_301200_.indexOf(36);

        while(k != -1) {
            if (k != i - 1 && p_301200_.charAt(k + 1) == '(') {
                builder.add(p_301200_.substring(j, k));
                int l = p_301200_.indexOf(41, k + 1);
                if (l == -1) {
                    throw new IllegalArgumentException("Unterminated macro variable in macro '" + p_301200_ + "' on line " + p_300045_);
                }

                String s = p_301200_.substring(k + 2, l);
                if (!isValidVariableName(s)) {
                    throw new IllegalArgumentException("Invalid macro variable name '" + s + "' on line " + p_300045_);
                }

                builder1.add(s);
                j = l + 1;
                k = p_301200_.indexOf(36, j);
            } else {
                k = p_301200_.indexOf(36, k + 1);
            }
        }

        if (j == 0) {
            throw new IllegalArgumentException("Macro without variables on line " + p_300045_);
        } else {
            if (j != i) {
                builder.add(p_301200_.substring(j));
            }

            return new PortedCommandFunction.MacroEntry(builder.build(), builder1.build());
        }
    }

    private static boolean isValidVariableName(String p_299170_) {
        for(int i = 0; i < p_299170_.length(); ++i) {
            char c0 = p_299170_.charAt(i);
            if (!Character.isLetterOrDigit(c0) && c0 != '_') {
                return false;
            }
        }

        return true;
    }

    public static class CommandMacro extends PortedCommandFunction {
        private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#");
        private final List<String> parameters;
        private final Object2ObjectLinkedOpenHashMap<List<String>, PortedCommandFunction> cache =
                new Object2ObjectLinkedOpenHashMap<>(8, 0.25F);

        public CommandMacro(ResourceLocation p_297380_, PortedCommandFunction.Entry[] p_300955_, List<String> p_300316_) {
            super(p_297380_, p_300955_);
            this.parameters = p_300316_;
        }

        public PortedCommandFunction instantiate(@Nullable CompoundTag p_300836_,
                                                 CommandDispatcher<CommandSourceStack> p_297591_,
                                                 CommandSourceStack p_298828_) throws FunctionInstantiationException {
            if (p_300836_ == null) {
                throw new FunctionInstantiationException(Component.translatable("commands.function.error.missing_arguments", this.getId()));
            } else {
                List<String> list = new ArrayList<>(this.parameters.size());

                for(String s : this.parameters) {
                    if (!p_300836_.contains(s)) {
                        throw new FunctionInstantiationException(Component.translatable("commands.function.error.missing_argument", this.getId(), s));
                    }

                    list.add(stringify(p_300836_.get(s)));
                }

                PortedCommandFunction commandfunction = this.cache.getAndMoveToLast(list);
                if (commandfunction != null) {
                    return commandfunction;
                } else {
                    if (this.cache.size() >= 8) {
                        this.cache.removeFirst();
                    }

                    PortedCommandFunction commandfunction1 = this.substituteAndParse(list, p_297591_, p_298828_);
                    if (commandfunction1 != null) {
                        this.cache.put(list, commandfunction1);
                    }

                    return commandfunction1;
                }
            }
        }

        private static String stringify(Tag p_298736_) {
            if (p_298736_ instanceof FloatTag floattag) {
                return DECIMAL_FORMAT.format((double)floattag.getAsFloat());
            } else if (p_298736_ instanceof DoubleTag doubletag) {
                return DECIMAL_FORMAT.format(doubletag.getAsDouble());
            } else if (p_298736_ instanceof ByteTag bytetag) {
                return String.valueOf((int)bytetag.getAsByte());
            } else if (p_298736_ instanceof ShortTag shorttag) {
                return String.valueOf((int)shorttag.getAsShort());
            } else if (p_298736_ instanceof LongTag longtag) {
                return String.valueOf(longtag.getAsLong());
            } else {
                return p_298736_.getAsString();
            }
        }

        private PortedCommandFunction substituteAndParse(List<String> p_300530_, CommandDispatcher<CommandSourceStack> p_299533_,
                                                         CommandSourceStack p_301159_) throws FunctionInstantiationException {
            PortedCommandFunction.Entry[] acommandfunction$entry = this.getEntries();
            PortedCommandFunction.Entry[] acommandfunction$entry1 = new PortedCommandFunction.Entry[acommandfunction$entry.length];

            for(int i = 0; i < acommandfunction$entry.length; ++i) {
                PortedCommandFunction.Entry commandfunction$entry = acommandfunction$entry[i];
                if (!(commandfunction$entry instanceof PortedCommandFunction.MacroEntry commandfunction$macroentry)) {
                    acommandfunction$entry1[i] = commandfunction$entry;
                } else {
                    List<String> list = commandfunction$macroentry.parameters();
                    List<String> list1 = new ArrayList<>(list.size());

                    for(String s : list) {
                        list1.add(p_300530_.get(this.parameters.indexOf(s)));
                    }

                    String s1 = commandfunction$macroentry.substitute(list1);

                    try {
                        ParseResults<CommandSourceStack> parseresults = p_299533_.parse(s1, p_301159_);
                        if (parseresults.getReader().canRead()) {
                            throw Commands.getParseException(parseresults);
                        }

                        acommandfunction$entry1[i] = new PortedCommandFunction.CommandEntry(parseresults);
                    } catch (CommandSyntaxException commandsyntaxexception) {
                        throw new FunctionInstantiationException(Component.translatable("commands.function.error.parse",
                                this.getId(), s1, commandsyntaxexception.getMessage()));
                    }
                }
            }

            ResourceLocation resourcelocation = this.getId();
            return new PortedCommandFunction(new ResourceLocation(resourcelocation.getNamespace(),
                    resourcelocation.getPath() + "/" + p_300530_.hashCode()), acommandfunction$entry1);
        }

        static {
            DECIMAL_FORMAT.setMaximumFractionDigits(15);
            DECIMAL_FORMAT.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.US));
        }
    }

    public static class MacroEntry implements PortedCommandFunction.Entry {
        private final List<String> segments;
        private final List<String> parameters;

        public MacroEntry(List<String> p_299524_, List<String> p_299522_) {
            this.segments = p_299524_;
            this.parameters = p_299522_;
        }

        public List<String> parameters() {
            return this.parameters;
        }

        public String substitute(List<String> p_300217_) {
            StringBuilder stringbuilder = new StringBuilder();

            for(int i = 0; i < this.parameters.size(); ++i) {
                stringbuilder.append(this.segments.get(i)).append(p_300217_.get(i));
            }

            if (this.segments.size() > this.parameters.size()) {
                stringbuilder.append(this.segments.get(this.segments.size() - 1));
            }

            return stringbuilder.toString();
        }

        public void execute(ServerFunctionManager p_299564_, CommandSourceStack p_298428_,
                            Deque<ServerFunctionManager.QueuedCommand> p_299632_, int p_298690_,
                            int p_299006_, @Nullable ServerFunctionManager.TraceCallbacks p_297793_) throws CommandSyntaxException {
            throw new IllegalStateException("Tried to execute an uninstantiated macro");
        }
    }
}
