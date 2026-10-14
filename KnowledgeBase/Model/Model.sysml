package Model {
    private import MetaModelDigitalTwin::*;
    public import TestBed::*;
    public import Simulation::*;

    #Twin def ST_Twin{
        #entity testbed:>libTwinEntities:TestBed[1];
        #entity simulation:>libTwinEntities:Simulation[1];

        
    }
}
