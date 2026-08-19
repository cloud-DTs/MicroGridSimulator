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

        #entity wallbox_ALPHA       : WallBox {}
        #entity wallbox_1           : WallBox {}
        #entity wallbox_2           : WallBox {}
        #entity wallbox_3           : WallBox {}
        #entity wallbox_A           : WallBox {}
        #entity wallbox_B           : WallBox {}
        #entity wallbox_C           : WallBox {}
        #entity wallbox_FASTCHARGER : WallBox {}

        #entity charging_events : ChargingEvents {}
    }
}