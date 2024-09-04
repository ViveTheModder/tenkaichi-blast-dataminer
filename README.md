# tenkaichi-blast-dataminer
A dataminer for Blast 1's & Blast 2's in Budokai Tenkaichi 3, inspired by the [DBZ League version of the same concept](https://github.com/dbzl-project11/dataminer/tree/master/src/main/java/com/dbzl/dataminer).
Command-line only; requires Java 8.

Here is the first message a user is greeted with:
```
Export and display a CSV file showing the damage of all Blasts in Budokai Tenkaichi 3.
The program has other methods, but as a proof of concept, only these damage-related arguments can be used:
-blk, -block -----> Show blocked Blast damage only
-bst, -boost -----> Show boosted Blast 2 damage (10% more)
-mpm, -sparking --> Show boosted damage (20% more) when the Blast 2 is used in MPM/Sparking (MAX Power Mode)
-cmb, -combo -----> Show damage (70% less) when the Blast 2 is used during a Blast Combo.
```