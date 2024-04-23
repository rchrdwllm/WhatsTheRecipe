package com.whatstherecipe.game.classes;

import java.util.ArrayList;
import java.util.Collections;

public class Step {
    public String label;
    public int stepNumber;
    public boolean isSelected = false;

    public Step(String label, int stepNumber) {
        this.label = label;
        this.stepNumber = stepNumber;
    }

    public static void shuffle(ArrayList<Step> steps) {
        ArrayList<Step> originalSteps = new ArrayList<Step>(steps);

        do {
            Collections.shuffle(steps);
        } while (originalSteps.equals(steps));
    }

    public static ArrayList<Step> sort(ArrayList<Step> steps) {
        int i, j;
        int n = steps.size();
        Step key;

        for (i = 1; i < n; i++) {
            key = steps.get(i);
            j = i - 1;

            while (j >= 0 && steps.get(j).stepNumber > key.stepNumber) {
                steps.set(j + 1, steps.get(j));

                j = j - 1;
            }

            steps.set(j + 1, key);
        }

        return steps;
    }
}
