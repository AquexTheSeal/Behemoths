package org.celestialworkshop.behemoths.api;

import net.minecraft.world.entity.Entity;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class ActionManager<T extends Entity> {

    private final T entity;
    private final List<ManagedAction<T>> action = new ArrayList<>();
    private final Random random = new Random();

    private @Nullable ManagedAction<T> currentAction;

    public ActionManager(T entity) {
        this.entity = entity;
    }

    @SafeVarargs
    public final void addAction(ManagedAction<T>... tasks) {
        this.action.addAll(Arrays.asList(tasks));
    }

    public void tick() {
        if (this.currentAction != null) {
            if (!this.currentAction.onTick() || !entity.isAlive()) {
                this.stopCurrentAction();
            }
        } else {
            this.tryStartNewTask();
        }
    }

    private void tryStartNewTask() {
        List<ManagedAction<T>> availableAction = new ArrayList<>();
        int totalWeight = 0;

        for (ManagedAction<T> action : this.action) {
            if (action.canStart()) {
                int weight = action.getWeight();
                if (weight > 0) {
                    availableAction.add(action);
                    totalWeight += weight;
                }
            }
        }

        if (availableAction.isEmpty()) {
            return;
        }

        int roll = this.random.nextInt(totalWeight);
        ManagedAction<T> selectedAction = null;

        for (ManagedAction<T> action : availableAction) {
            roll -= action.getWeight();
            if (roll < 0) {
                selectedAction = action;
                break;
            }
        }

        if (selectedAction == null) {
            selectedAction = availableAction.get(availableAction.size() - 1);
        }

        this.currentAction = selectedAction;
        this.currentAction.onStart();
    }

    public boolean isBusy() {
        return this.currentAction != null;
    }

    @Nullable
    public ManagedAction<T> getCurrentAction() {
        return currentAction;
    }

    public void stopCurrentAction() {
        if (this.currentAction != null) {
            this.currentAction.onStop();
            this.currentAction = null;
        }
    }
}