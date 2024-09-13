# tenkaichi-blast-dataminer
A dataminer for Blast 1's & Blast 2's in Budokai Tenkaichi 3, inspired by the [DBZ League version of the same concept](https://github.com/dbzl-project11/dataminer/tree/master/src/main/java/com/dbzl/dataminer).
As one would expect, it's CLI-only, so get your Command Prompts fired up: ``java -jar blast-dataminer.jar [insert-arguments-here]``.

Here is the first message a user is greeted with:
```
Export and display a CSV file showing the damage of all Blasts in Budokai Tenkaichi 3.
Here is a list of all the arguments that can be used. Use -h or -help to print this out again.

[General Blast Arguments]
* -blid, -magic --> Display the magic value (or Blast Identifier) of every Blast.
* -dmg, -damage --> Display the damage of every Blast. Additional arguments can be used in any order, such as:
** blk, block ----> Show blocked Blast damage only
** bst, boost ----> Show boosted Blast 2 damage (10% more).
** mpm, spark ----> Show boosted damage (20% more) when the Blast 2 is used in MPM/Sparking (MAX Power Mode).
** cmb, combo ----> Show damage (70% less) when the Blast 2 is used during a Blast Combo. 
* -dur, -length --> Display the duration/length of every Blast.
* -spd, -speed ---> Display the speed of every Blast.
* -ub, -unblock --> Display every Blast that is unblockable.
[Blast 1 Arguments]
* -b1cost --------> Display the number of Blast Stocks consumed upon usage of every Blast 1.
* -b1hp ----------> Display the number of Health Bars gained upon usage of every Blast 1.
* -b1ki ----------> Display the number of Ki Bars gained upon usage of every Blast 1.
* -b1span --------> Display how long (in seconds) the effects of every Blast 1 last.
* -b1stats -------> Display the stats (Ability Modifiers) of every Blast 1.
[Blast 2 Arguments]
* -b2clash -------> Display the Clash Advantage of every Blast 2.
* -b2cost --------> Display the number of Ki Bars consumed upon usage of every Blast 2.
* -b2cuts --------> Display the number of cutscene animations for every Blast 2.
* -b2fall --------> Display the fall type identifier of every Blast 2.
* -b2type --------> Display every Blast 2's type.
* -b2giants ------> Display every Blast 2 that affects giant characters.
* -b2struggle ----> Display every Blast 2 that can perform a Beam Clash.
* -b2damageopp ---> Display every Blast 2 that can damage the opponent's costume.
* -b2damageusr ---> Display every Blast 2 that can damage the player's costume.
* -b2planetdes ---> Display every Blast 2 that can blow up the planet.
* -b2fallafter ---> Display every Blast 2 that makes the opponent fall after the Blast 2's execution.
* -b2fadeopp -----> Display every Blast 2 that makes the opponent fade if the Blast 2 has killed them.
* -b2gothrough ---> Display every Blast 2 that goes through the opponent.
* -b2kamehame ----> Display every Blast 2 that is affected by the Fierce God Z-Item (only Kamehamehas).
* -b2positair ----> Display every Blast 2 that sets the opponent's position in the air.
* -b2positmap ----> Display every Blast 2 that sets the player's position to the map center.
* -b2stepback ----> Display every Blast 2 that makes the user step back before performing it.
```
