package com.crawkatt.meicamod.util;

public abstract class ModIronStorage {
    private final int capacity;   // Capacidad máxima de hierro fundido
    protected int ironStored;     // Cantidad actual de hierro fundido almacenado

    // Constructor
    public ModIronStorage(int capacity) {
        this.capacity = capacity;
        this.ironStored = 0; // Inicialmente no hay hierro fundido
    }

    // Método para consumir hierro fundido (equivalente a extractEnergy)
    public int consumeIron(int amount, boolean simulate) {
        int ironExtracted = Math.min(ironStored, amount); // No puedes extraer más de lo que tienes
        if (!simulate) {
            ironStored -= ironExtracted;
            if (ironExtracted > 0) {
                onIronChanged(); // Evento de cambio
            }
        }
        return ironExtracted;
    }

    // Método para añadir hierro fundido (equivalente a receiveEnergy)
    public int addIron(int amount, boolean simulate) {
        int ironReceived = Math.min(capacity - ironStored, amount); // No puedes almacenar más de la capacidad
        if (!simulate) {
            ironStored += ironReceived;
            if (ironReceived > 0) {
                onIronChanged(); // Evento de cambio
            }
        }
        return ironReceived;
    }

    // Setter directo para el hierro almacenado (si es necesario)
    public int setIron(int amount) {
        this.ironStored = Math.min(capacity, amount); // Asegurarse de no superar la capacidad
        onIronChanged();
        return this.ironStored;
    }

    // Método abstracto que se llamará cada vez que cambie el hierro almacenado
    public abstract void onIronChanged();

    // Getters para obtener la cantidad almacenada y la capacidad máxima
    public int getIronStored() {
        return ironStored;
    }

    public int getCapacity() {
        return capacity;
    }

    /*
    public ModIronStorage(int capacity, int maxTransfer) {
        super(capacity, maxTransfer);
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        int extractedEnergy = super.extractEnergy(maxExtract, simulate);
        if (extractedEnergy != 0) {
            onEnergyChanged();
        }

        return extractedEnergy;
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        int receiveEnergy = super.receiveEnergy(maxReceive, simulate);
        if (receiveEnergy != 0) {
            onEnergyChanged();
        }

        return receiveEnergy;
    }

    public int setEnergy(int energy) {
        this.energy = energy;
        return energy;
    }

    public abstract void onEnergyChanged();
    */
}
