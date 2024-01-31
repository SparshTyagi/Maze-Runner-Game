# The Dungeon of Tyagi

Welcome to our Java based Maze Runner game - The Dungeon of Tyagi!

## Description

The Dungeon of Tyagi is a LibGDX-based 2D top-down maze runner with combat, points, lives, and mobs. The player starts at the entrance of the maze and must navigate through the maze to reach the exit while avoiding traps and defeating mobs. The player must also collect keys that are scattered throughout the maze to unlock the exit, lest the timer run out, and they be trapped forever! 

## Gameplay


### Screens

* Menu - Available on startup and through the Esc button. Allows the player to continue playing if coming from ESC, choosing a new map, or to exit.
* Map Selector - Lets the player choose from 5 dynamic and distinct level options or to import their own map using default textures.
* Game Screen - Where the player will spend the majority of their time while playing the game. Displays the actual Gameplay.
* Victory Screen - Screen that appears when the player wins the game. Displays their score and allows them to return to the Menu.
* Death Screen - Screen that appears when the player loses the game. Allows them to return to the Menu.

### Controls 

* The player can use moving the DPad arrow keys, or using W, A, S, and D to navigate through the intricacies of the maze.
* They player can press SPACE to attack a small area directly in front of where they are facing to destroy a mob for good!

### Mechanics

* The player starts any given level with a set number of lives. These lives may not be restored so the player must be careful, as certain levels may require sacrificing some lives to progress.
* The player must collect a key from the maze and reach the exit before losing all lives to win.
* Static traps and dynamically moving mobs are obstacles. On contact with any of them, the player loses one life.
* The player can view the amount of lives left and key collection status at all times on the HUD at the top of their screen.
* The player can pause the game at any time by hitting the ESC button.

## Bonus Features:

* The player has a fixed amount of time to collect the key and exit any given level. If the timer runs out, they lose.
* The mobs have a random movement pattern, unless the player is in a 2 block radius of them, in which case they chase the player and try to attack them.
* The player gains movement speed the lower their health gets, making it easier to run away from dangerous situations.
* The player has a short period of invincibility after losing a life, making it possible to survive levels with traps in the middle of the exit path and enabling tactical health loss to rush through a crowd of mobs.
* The HUD displays additional display items like the timer and the points collected. There is also a graphical display of the number of lives remaining and the key collection.
* The player has the option to attack the mobs. If attacked, the mob is killed and removed from the level permanently, adding the option for more combat based gameplay. 
* There is a point system, where the player can gain points by killing enemies and collecting the key. These points, in addition to the time and lives remaining when they win the level are taken into account in displaying the final score. 
* There are animations, music, and sounds for every part of the game. Moving, attacking, losing lives, getting points, winning, losing, etc
* There are numerous additional preloaded maps with their own texture packs for the mobs, walls, and floors delivering unique and exciting experiences.
* The player can move with W, A, S, and D in addition to the Dpad, both being simultaneously available.


### Dependencies

* Be sure to build a custom run configuration in the beginning by setting classpath as the class path ending with
```
##### .desktop.main
```


## Help

For players for systems running on macOS, be sure to edit the run configuration to add VM options. Then input the line
```
-XstartOnFirstThread
```


## Authors

[@Sparsh Tyagi](https://www.linkedin.com/in/sparsh-tyagi/)
    sparsh.tyagi@tum.de

[@Anjani Bahl](https://www.linkedin.com/in/anjani-bahl/)
    anjani.bahl@tum.de

## License

Copyright 2024 Sparsh Tyagi & Anjani Bahl

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

The Software is provided "as is", without warranty of any kind, express or implied, including but not limited to the warranties of merchantability, fitness for a particular purpose and noninfringement. In no event shall the authors or copyright holders be liable for any claim, damages or other liability, whether in an action of contract, tort or otherwise, arising from, out of or in connection with the Software or the use or other dealings in the Software.

## Acknowledgments

* [LibGDX Documentation](https://libgdx.com/)