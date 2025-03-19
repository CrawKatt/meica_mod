package com.crawkatt.meicamod.util;

public abstract class ModIronStorage {
    private final int capacity;
    protected int ironStored;

    public ModIronStorage(int capacity) {
        this.capacity = capacity;
        this.ironStored = 0;
    }

    public void consumeIron(int amount, boolean simulate) {
        int ironExtracted = Math.min(ironStored, amount);
        if (!simulate) {
            ironStored -= ironExtracted;
            if (ironExtracted > 0) {
                onIronChanged();
            }
        }
    }

    // Método para añadir hierro fundido (equivalente a receiveEnergy)
    public int addIron(int amount, boolean simulate) {
        int ironReceived = Math.min(capacity - ironStored, amount);
        if (!simulate) {
            ironStored += ironReceived;
            if (ironReceived > 0) {
                onIronChanged();
            }
        }
        return ironReceived;
    }

    public void setIron(int amount) {
        this.ironStored = Math.min(capacity, amount);
        onIronChanged();
    }

    // Método abstracto que se llamará cada vez que cambie el hierro almacenado
    public abstract void onIronChanged();

    public int getIronStored() {
        return ironStored;
    }

    public int getCapacity() {
        return capacity;
    }
}
