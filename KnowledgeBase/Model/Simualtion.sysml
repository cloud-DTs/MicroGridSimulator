package Simulation{
    private import MetaModelDigitalTwin::*;
    public import BatterySimulation::*;
    public import PVSimulation::*;
    public import GridSimulation::*;
    public import MicroGridSimulation::*;
    public import ChargingEventSimulation::*;
    public import LoadSimulation::*;
    public import SimulationMain::*;

    #Entity def Simulation{
        #entity battery:>sbEntities:BatterySimulation[1];
        #entity pv:>sbEntities:PVSimulation[1];
        #entity grid:>sbEntities:GridSimulation[1];
        #entity microgrid:>sbEntities:MicroGridSimulation[1];
        #entity load:>sbEntities:LoadSimulation[1];
        #entity initial_values:>sbEntities:SimulationMain[1];
    }
}
