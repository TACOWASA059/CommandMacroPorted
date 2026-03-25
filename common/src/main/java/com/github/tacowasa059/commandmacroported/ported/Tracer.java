package com.github.tacowasa059.commandmacroported.ported;

import net.minecraft.commands.CommandSource;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.ServerFunctionManager;

import java.io.PrintWriter;

public class Tracer implements ServerFunctionManager.TraceCallbacks, CommandSource {
    private final PrintWriter output;
    private int lastIndent;
    private boolean waitingForResult;

    public Tracer(PrintWriter p_180079_) {
        this.output = p_180079_;
    }

    private void indentAndSave(int p_180082_) {
        this.printIndent(p_180082_);
        this.lastIndent = p_180082_;
    }

    private void printIndent(int p_180098_) {
        for(int i = 0; i < p_180098_ + 1; ++i) {
            this.output.write("    ");
        }

    }

    private void newLine() {
        if (this.waitingForResult) {
            this.output.println();
            this.waitingForResult = false;
        }

    }

    public void onCommand(int p_180084_, String p_180085_) {
        this.newLine();
        this.indentAndSave(p_180084_);
        this.output.print("[C] ");
        this.output.print(p_180085_);
        this.waitingForResult = true;
    }

    public void onReturn(int p_180087_, String p_180088_, int p_180089_) {
        if (this.waitingForResult) {
            this.output.print(" -> ");
            this.output.println(p_180089_);
            this.waitingForResult = false;
        } else {
            this.indentAndSave(p_180087_);
            this.output.print("[R = ");
            this.output.print(p_180089_);
            this.output.print("] ");
            this.output.println(p_180088_);
        }

    }

    public void onCall(int p_180091_, ResourceLocation p_180092_, int p_180093_) {
        this.newLine();
        this.indentAndSave(p_180091_);
        this.output.print("[F] ");
        this.output.print((Object)p_180092_);
        this.output.print(" size=");
        this.output.println(p_180093_);
    }

    public void onError(int p_180100_, String p_180101_) {
        this.newLine();
        this.indentAndSave(p_180100_ + 1);
        this.output.print("[E] ");
        this.output.print(p_180101_);
    }

    public void sendSystemMessage(Component p_214427_) {
        this.newLine();
        this.printIndent(this.lastIndent + 1);
        this.output.print("[M] ");
        this.output.println(p_214427_.getString());
    }

    public boolean acceptsSuccess() {
        return true;
    }

    public boolean acceptsFailure() {
        return true;
    }

    public boolean shouldInformAdmins() {
        return false;
    }

    public boolean alwaysAccepts() {
        return true;
    }
}