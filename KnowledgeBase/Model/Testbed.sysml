package TestBed{
    private import MetaModelDigitalTwin::*;
    public import BatteryTestBed::*;
    public import PVTestBed::*;
    public import GridTestBed::*;
    public import WallBoxTestBed::*;

    #Entity def TestBed{
       #entity battery:>sbEntities:BatteryTestBed[1];
       #entity pv:>sbEntities:PVTestBed[1];
       #entity grid:>sbEntities:GridTestBed[1];
       #entity wallboxes:>sbEntities:WallBoxes[1];
    }
}
