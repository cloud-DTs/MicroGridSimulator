package StandardMG {
    private import MetaModelDigitalTwin::*;
    private import ModelDefault::*;

    #Twin def StTwin{

        #entity battery    : Battery    {}
        #entity pv         : PV         {}
        #entity grid       : Grid       {}
        #entity simulation : Simulation {}
        #entity microgrid  : MicroGrid  {}
        #entity load       : Load {}

        #entity wallbox_Keba      : WallBox {}
        #entity wallbox_Versi     : WallBox {}
        #entity wallbox_SieChargeCC : WallBox {}
        #entity wallbox_SieChargeD  : WallBox {}
        #entity wallbox_SieCPC20    : WallBox {}
        #entity charging_events : ChargingEvents {}
    }
}