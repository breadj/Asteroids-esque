# My Game

## Manual

### How to Run:
* Open the project in a Java IDE of your choice
* Make sure the `levels.xml` file is in the working directory of the project
* Open the `Game.java` file in the `Game` package, as this is where the `main()` method is 
* Run the file from the `main()` method

### How to Play:
#### Controls:
* Up-arrow: Move forwards
* Left-arrow: Rotate counter-clockwise
* Right-arrow: Rotate clockwise
* Space: Shoot
* Escape: Exit

Shoot the cannons until you destroy them, otherwise they'll destroy you.  
There are 5 levels in this game, each increasing in difficulty.  
If you get hit, you get -500 points. If you destroy a cannon, you get +200 points.  
You start with 3 lives. If you reach 0, that's game over.  
Every time you reach 5000 points, and you **haven't** died in that level yet, you gain an extra life.  
Projectiles, including you and the cannons, wrap around the screen. So if you go over the edge of the screen on one 
side, you'll teleport to the other edge.  
Tip: The cannons take some time turning around, so use the screen wrap-around to your advantage.


## Report

### Appraisal

Overall, the project went seemingly well. Development was fast, and though a few snags were caught along the way, most 
of the work I wanted to do had been done. I was caught up for a few hours on two specific things during development, 
however:
1. Implementing `GameTime`
2. Cannon turning and aiming

The former was due to simply how fast computers are. I tried to implement fully-unlocked frames, however that was not 
possible. This was because when I let the loop iterate without any `Thread.sleep()` holding it back, the `lapsed()` 
property of `GameTime` always showed the lapsed time as 0ms, which the occasional 1ms once the millisecond actually 
ticked over. This messed with my physics, so I tried to use a separate timer for physics, drawing, and updates. This 
also refused to work, so I decided the best way to implement `GameTime` was to have the lapsed time be accurate, but 
still let the thread sleep for `TICK_RATE` amount of time. If I could go back and program something again, I would 
figure out how to completely unlock the framerate and redo that.

The latter was and still is a mystery to me. Using the `Vector2D.angleTo()` method, I attempted to retrieve the angle 
the cannon needed to turn to aim at the ship. This, judging by how the story is going, obviously failed. I rewrote the 
`angleTo()` method multiple times, using different formulae each iteration, but it just wouldn't work. I spent hours 
inserting console prints and drawing rays to where the cannon direction was facing and all, but in the end, I fixed it 
through no intentional means. It kind of just... started to work after I changed the umpteenth thing. If I were to do 
this project again, I would be most careful and genuinely terrified of this bug.


In terms of the general project, I'm just glad it works okay. For a game - is it fun? Who knows. As a computing 
exercise, it was both fun and frustrating. I'm not particularly proud of any part of my code in this project, but 
equally not disappointed in any part of it either. I'm unsure as to whether there are any unsolved issues, but I haven't 
come across any so far, so that's my excuse.  
I used the code from the labs as a basis but heavily modified a fair few parts of it, though part of me feels it 
could've maybe been easier to start from the ground up again... For some reason. Either way, I'm glad it's over, but 
also content it happened.

### Implementation & Additional Features

**GameTime:**  
I implemented a `GameTime` class to keep the time and ticks. Doing this made my calculations more accurate as delta time was 
found by taking the current time in milliseconds and subtracting it from the previous time in milliseconds. I found it 
an important addition, as, from measuring the time myself, despite there being a 20ms sleep in the thread every game 
loop iteration, the actual time that had passed was closer to 30ms due to the computation that happened between those 
`Thread.sleep()` calls. 

**Invulnerability Flash:**  
To combat being immediately shot upon loading a level, and to remedy being hit multiple times in a short span of time 
(and therefore getting an instant game over), I added invincibility frames. Whenever you get hit, or when you start a 
new level, you gain a few seconds of invulnerability, where collision is not calculated. At the start of the level, you 
have 4 seconds of invulnerability, and if you get hit, you gain 2.5 seconds. During your invulnerable time, just to make
sure you're aware that you have invulnerable frames, and also to notify you that you've been hit, the ship flashes 
between visible and invisible, similar to how it was done in many retro games.

**Cannon Recoil:**  
Every time a cannon fires a bullet at the ship, it gets pushed back slightly due to recoil. I did this as having 
completely stationary targets seemed; 1. boring, and 2. programmatically not complex enough. Also, the fact the entire 
cannon gets pushed back after firing cements further to the player that this takes place in space/0G. So now, instead, 
whenever a cannon fires, a bullet is created, but it also has its velocity increased in the opposite direction it fired. 
Drag will naturally slow it down quickly, especially since it doesn't recoil far, but the movement is certainly 
noticeable.

**Cannon Turning:**  
To make the game more balanced/fair, cannons shouldn't be able to immediately lock onto you wherever you are on the 
screen. I made sure to prevent this by implementing a `TURN_SPEED` for cannons, so if you're fast enough you can outrun 
their turning speed, or perhaps if you used the wrap-around effect of the game engine you could get a few shots in 
before they locked onto you again. I had a lot of trouble with implementing the lag-behind turn, however, especially 
with changing which way the cannon turned (clockwise/counter-clockwise) depending on which would be the shortest 
rotation to aim at the ship.

**Loading Levels from a File:**  
For loading levels, I used an XML file named `levels.xml` and stored level data in there. Levels are made up of an 
HPPool and an array of Vector2Ds that determine the position of each cannon on the screen.  
I used the built-in library of java.beans.XMLEncoder to create the xml file, and java.beans.XMLDecoder to retrieve the 
data from the XML file. The reason I used this library is that it is the only native XML/JSON serializing to Java, 
and I didn't want to bloat the game with external libraries. Unfortunately, using these java libraries made my `Level` 
class messier, as I had to program it in a way it corresponded to the JavaBeans syntax, but it was the only feasible way
to store my levels externally to the program.

**Randomly Distributing HP:**  
When a new level is loaded, all the cannons in the level are given a random amount of HP. This isn't truly random, 
however, as it wouldn't be fair if all the cannons received the highest health possible. This is why I used a method to 
randomly distribute 'X' amount of HP between N number of cannons. That, and the fact that each level had a specific, 
deterministic layout made the small random aspect makes the game more fun and thrilling to play. It actually took me a 
long time to come up with the mathematical formula and method to do this, as I had to both calculate random floats 
between 0-1 for each cannon, but also normalise the floats once their share of the X amount was given to them. Once I'd 
done this, I realised that some cannons were getting 0HP and dying immediately. So I had to modify the method so all 
cannons received at least 1HP.

**Corpse Collection:**  
When `GameObject` objects die (have their `isDead()` method return true), they shouldn't exist in the game anymore. In 
the labs and the lab code, it was encouraged for us to keep a list of all objects, and another list of all the 'alive' 
objects. I felt this to be inefficient however, as keeping copies, or even just references of all the alive objects 
could be taking up much more memory than necessary. This is why I decided to have Corpse Collection. A simple method 
that just ran through the whole `GameObject` list and removed those that were dead. This got rid of the unnecessary 
reference copying that occurred every frame to replace the whole object list with the alive object list.

**Info Displaying:**  
Just to make things more pleasant for the player, I added `drawString()` calls in `View` to display the current level, 
score, number of lives, and number of cannons left. Simply for the sake of being more open with the player and sharing 
more information that might interest or benefit them.

**Cannon Health Bar:**  
Especially due to cannons having random HP, I decided to make a health bar for the cannons that floated just below their
sprites. It uses 'current HP' and 'total HP' variables to calculate how much of the full bar should be coloured in, so 
if a cannon started with 5HP but now only has 3HP, 3/5ths of the bar would be full, giving players more feedback on 
which cannons to prioritise and how close they are to being destroyed.

**Friendly Fire:**  
In the labs, asteroid-on-asteroid collision wasn't accounted for. For good reason, too. However, in this assignment, I 
decided that one of the main way to destroy the cannons would be to get them to shoot at each other. Due to the fact 
that cannons always try to aim at the ship, if you placed your ship in front of another cannon, the cannon at the back 
would fire and hit the cannon in-between, doing damage. There are many more cannons and therefore many more bullets to 
dodge than you can fire at and weave between, so the aide received by the cannons themselves I feel would be welcomed by
the player.