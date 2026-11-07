package com.niv.flow.models;

import java.time.LocalDate;

public class DailyInspiration {
    private final String[] dailyInspirationTexts;
    public DailyInspiration() {
        this.dailyInspirationTexts = new String[]{
                "One breath at a time.",
                "Progress is still progress.",
                "You can begin again today.",
                "Small steps count.",
                "Your pace is enough.",
                "Keep showing up for yourself.",
                "Let today unfold gently.",
                "Trust your next small step.",
                "You don't need to be perfect.",
                "Pause. Breathe. Continue.",
                "Your voice deserves space.",
                "One brave moment is enough.",
                "Growth takes practice.",
                "Calm begins with a breath.",
                "You've come this far.",
                "Choose progress over pressure.",
                "Take a moment for yourself.",
                "Today is a fresh start.",
                "You are allowed to slow down.",
                "Find strength in small wins.",
                "Speak with courage.",
                "Every effort matters.",
                "Your feelings can change.",
                "Be present, not perfect.",
                "Give yourself room to grow.",
                "Your next step matters.",
                "You can handle this moment.",
                "Let go of what you can't fix.",
                "Celebrate the little wins.",
                "Keep moving, gently."
        };
    }

    public String[] getDailyInspirationTexts() {
        return dailyInspirationTexts;
    }

    public String getDailyInspiration() {
        int day = (int) LocalDate.now().toEpochDay();
        return this.dailyInspirationTexts[day%30];
    }
}
