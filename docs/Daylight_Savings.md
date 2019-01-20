NoMoreOversleeps has built-in support for Daylight Savings Time switchovers starting with version 0.13 with improvements in version 0.14. If you are using NMO during a DST changeover, the following will happen:

**For sleep blocks which start before 01:00 AM**:  The sleep block will start at the normal time and will last the normal length of time. This means, if your sleep block is normally 00:30 AM to 03:30 AM and the clocks are going backwards, your sleep block will become 00:30 AM to 02:30 AM (still totalling the normal 3 hours, as 01:00 to 01:59 is repeated)

**For sleep blocks which start during 01:00 AM - 01:59 AM**:  For changeovers where the clock goes backwards, your sleep block will be starting at the new 01:00 AM (the old 02:00 AM), which means you will go to sleep 1 hour later. For changeovers where the clock goes forwards, your sleep block will not be moved, which means you will go to sleep as though the clock did not advance forward at the old 01:00 AM (the new 02:00 AM).

**For sleep blocks which start after 01:59 AM**:  The sleep block will start in the new timezone at the normal scheduled time.