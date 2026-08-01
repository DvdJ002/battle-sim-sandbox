# Battle Sim

## Short Summary

A simple 2D top-down battle game. The player fights waves of enemies with different behaviors, abilities, and mechanics while completing time-limited levels.
The game is made with libGDX and uses classic OOP architecture.

Enemy textures are simple 2D sprites drawn in MS Paint. Other assets (like effect icons, sounds) are taken from free websites like icons8.com and freesound.org.   


## Player
The player is a circular "tank" with a range of abilities.

**Shoot:**
The player's primary damage dealer. Shoot bullets at enemies that deal 13 damage. There is a short cooldown between bullets so the player cannot shoot at a super high rate.

**Ultimate:**
This is an AOE force field attack that deals waning damage to enemies in range and makes the player invincible and static for the duration

**Shield:**
A defensive shield that blocks all damage. If broken, it cannot be used until it recharges. While the shield is activated, the player cannot shoot, dash, and the dash bar does not recharge.

**Dash:**
A movement ability that lets the player jump/teleport quickly in the movement direction. Implemented by giving the player a sudden movement boost.

**Stats**

HP: 100
Shield health: 100
Speed: 280
Bullet damage: 1
Force field damage: 3 continuous, waning

**Controls**

Shoot: LMB | Move: WASD | Dash: Space | Shield: RMB | Ult: Q 

## Enemies
Enemy pathfinding and movement AI is implemented with the gdx.ai library, enemies use behaviors like Evade and Pursuit.

### Shooter

**Role: Range pressure**

The shooter is a careful enemy that constantly shoots bullet in your direction every 2 seconds. 
It avoids you if you get too close, and chases you if you go far. They are most effective in numbers.
Unlike most other enemies, the player deals damage to them upon collision instead of vice versa. 

Since the shooter often cannot brake in time when chasing after the player, its momentum will carry it into the player, making 
the ultimate ability a powerful tactic against them.

**Stats:**
- HP: 100
- Damage: 20 (bullet)
- Collision: The player deals damage to shooter during collision 
- Movement speed: Medium (chase), high (chase)
- Other relevant values: Small, fast bullets

**Visual:**

Image of a shooter or two and a flying bullet
![Enemy image](path/to/image.png)

---

### Slasher

**Role: Offensive pursuer**

The slasher is an aggressive enemy that dashes at you and applies a dangerous 1 second slow effect.
It will dash when it reaches a certain detection range, and if you get hit, it deals collision damage and applies a slow effect.  

**Stats:**
- HP: 120
- Collision damage: 2 when hitting the player
- Movement speed: High (chase), very high (dash) 
- Detection range: Short

**Visual:**

![Enemy image](path/to/image.png)


---

### Kamikaze

**Role: Explosive kamikaze**

Kamikaze is a smaller enemy that will chase you directly and explode upon reaching your vicinity. 
The explosion will deal high damage and apply knockback. The KB is implemented by giving the player sudden opposite angle velocity.

**Stats:**
- HP: 80
- Explosion damage: 75
- Movement speed: High (chase)
- Detection range: Short

**Visual:**

![Enemy image](path/to/image.png)


---

### Sucker

**Role: Tanky AOE**

The Sucker is a large, slow, tanky, AOE enemy that sucks you into a force field if you get too close.

**Stats:**
- HP: 250
- Force field damage: 1 continuous, waning
- Movement speed: Slow (chase), static (force field)
- Detection range: Medium

**Visual:**

![Enemy image](path/to/image.png)


---

### Healer

**Role: Elusive assistance**

The Healer is a small enemy with a low HP pool. When it finds an enemy, it will "lock-on" to it; which entails following and continuously healing it.
It's only able to heal the enemy if within range. It "hides" from the player by trying to use the enemy it is healing between itself and the player.

If there is no enemy within range it will simply roam around randomly until one is found. 
If you kill the enemy it is actively healing, it will also die. If you kill the locked-on enemy while it is not actively healing, it will simply switch back to roam and search for a new enemy.


**Stats:**
- HP: 40
- Healing: 0.5 continuous
- Movement speed: Slow (roam), medium (healing)
- Detection range: Medium

**Visual:**

![Enemy image](path/to/image.png)



## Special Enemies

These are special versions of the normal enemies. They include:
- Large shooter (more health and deals more damage)
- Large slasher (more health and more damage)
- Large kamikaze (more health and stronger explosion)
- Special healer (massive healing range and fast healing)


# Levels

The game has 8 levels that challenge you on different scenarios. Levels consists of waves, and a new wave is triggered either when enough time passes, or when you kill a certain amount of enemies. There is also a level timer that pressures you to complete all the waves in time.

## Level 1: Entourage

**Duration / Time Limit:** 30 seconds

**Enemy Composition:** 
Waves of a large enemy accompanied with two normal versions of the same enemy type

**Wave Structure:**
1. Large shooter with 3 normal shooters placed in front of it
2. Large slasher with 2 normal slashers placed far on the sides
3. Large kamikaze with 2 normal kamikazes placed close on the sides


## Level 2 - War on two fronts

**Duration / Time Limit:** 25 seconds

**Enemy Composition:** 
Two waves of shooters from both sides, finishes with 2 kamikaze

**Wave Structure:**
1. 4 shooters on the left side of screen
2. 4 shooters on the right side of screen
3. 2 kamikaze from left and right upper corners

**Notes:** Using force field is very strong (arguably necessary) against the numerous shooters


## Level 3 - Spontaneous explosion

**Duration / Time Limit:** 47 seconds

**Enemy Composition:** 
Starts with a circle of kamikazes that force your ultimate (otherwise unsurvivable). Then, shooters and slashers cycle coming from each side of the arena clockwise, followed by all of the previous shooters and slashers combined. Finally it ends with a a large shooter, large slasher, and large kamikaze.

**Wave Structure:**
1. Circle of kamikazes on the edges
2. Shooter middle up
3. Slasher right middle
4. Shooter middle down
5. Slasher left middle
6. All of the previous combined
7. Large slasher, large shooter, large kamikaze in middle

**Notes:** Consider saving your ultimate for the very last wave

 
## Level 4 - Vacuum cleaned

**Duration / Time Limit:** 30 seconds

**Enemy Composition:** 
A sucker spawns in each corner and a row of shooters at the top. After 4 enemies are killed, another sucker spawns in the middle.

**Wave Structure:**
1. Suckers spawns in each corner and a row of shooters at the top
2. Sucker in the middle

 
## Level 5 - Front line

**Duration / Time Limit:** 37 seconds

**Enemy Composition:** 
A "front line" of suckers with a healer behind each. Every 2 enemies killed a kamikaze spawns, for 3 waves. 

**Wave Structure:**
1. A row of 5 healers at the top followed by a row of 5 suckers just below them
2. A kamikaze top middle
3. A kamikaze top middle
4. A kamikaze top middle

**Notes:** You cannot kill the suckers in time if you don't kill the healers first

 
## Level 6 - The Chosen One

**Duration / Time Limit:** 55 seconds

**Enemy Composition:** 
A large slasher encircled by 3 special healers. Then, the level keeps spawning healers every 1.5 seconds, alternating top and bottom.
Two times during the level, a large kamikaze will spawn in the middle of the arena.

**Wave Structure:**
1. A large slasher encircled by 3 healers, top row
2. - X. Keep spawning healers every 1.5 seconds, alternating top and bottom, with two waves including a large kamikaze

**Notes:** The large slasher (Chosen One) is practically unkillable until the end when the healers stop spawning. You have to focus the waves of healers and kill them as they spawn, other wise you will run out of time to kill the slasher at the end.


## Level 7 - Hornet's Nest

**Duration / Time Limit:** 55 seconds

**Enemy Composition:** 
First, a singular healer is spawned. As the healer is killed, a large number of shooters is spawned (every 1.5 seconds), all from the top.

**Wave Structure:**
1. A healer middle top
2. - X. - Keep spawning shooters every 1.5 seconds

**Notes:** There are two ways to "solve" this level. It is not meant to be fought normally.


## Level 8 - Well-rounded attack

**Duration / Time Limit:** 85 seconds

**Enemy Composition:**  
A combination of many enemy types, forcing the player to handle melee pressure, ranged attacks, healing support, and area control simultaneously.

**Wave Structure:**
1. 3 slashers and 3 shooters spawn from opposite sides of the arena, supported by 4 healers positioned in the corners.
2. 4 suckers spawn in the corners, each supported by a healer. Additional slashers and shooters spawn alongside them
3. 2 slashers and 2 shooters spawn from the corners
4. 2 slashers and 2 shooters spawn from the corners


# Boss

## General

The boss is a large, difficult enemy, with a wide range of close and long range attacks. The fight has two phases. The boss constantly cycles between an idle and attacking. The boss appearance indicates the state it is in; two exclamation marks on its body means that it is currently attacking, which is important to distinguish certain telemarked attacks.

The idle state is a fixed duration, and when it ends, the boss chooses an attack based on the enemy's position (close, long range), and the current state of the arena. The boss by default tries to mirror the player's position in the arena, unless some attack is overriding this behavior. 
This is the most difficult level in the game. 

**Base stats**
- HP: 5650
- Idle state duration: 1.8 seconds
- Base speed: Slow, mirrors player
- Enraged speed: Medium, pursues player
- Close range radius: Medium
- Enraged threshold: 25% of HP
- Collide damage: 1.5

**Phases:**

The two phases are very different in aggression and behavior. The state transition is indicated by a change to a multi-colored texture. The idle period becomes shorter, and the boss starts pursuing the player.
- Phase 1: In this phase, the boss has a high number of attacks available. It will mirror the player's movement by default.
- Phase 2 (enraged): When the boss' health threshold reaches 25%, it will transition into an enraged mode, where it follows the player constantly and has two available attacks that complement each other.


## Attacks

### Bullets (Long range)

**Description:** The boss shoots pairs of medium-sized bullets for 5 seconds that deal 20 damage.

**Counterplay:** Dodging is relatively simple if you are not too close

---

### Slam/Force field (Close range)

**Description:** Creates a large force field with waning damage that lasts 3.5 seconds and deals 1.5 damage.

---

### Dash (Both ranges)

**Description:** Dashes at the player 4 times. The 4th dash is much stronger and faster than the others.

---

### Summon offensives (Both ranges)

**Description:** Spawns a pair of shooters in front and a slasher behind.

**Counterplay:** It's really dangerous to get slowed by the slasher so focusing it is a priority.

---

### Summon healer (Both ranges)

**Description:** Spawns a healer right behind the boss.

**Counterplay:** This is a priority. The healer can heal a lot of HP in a relatively short time. The boss might spawn multiple healers in succession.

---

### Explosion (Both ranges)

**Description:** 
This two-stage attack starts with a telemarked pause/grace period during which the boss stands completely still for 3 seconds. After that, it rushes at the player and, if it reaches a suitable range, explodes to deal immense 150 damage with knockback.  

**Counterplay:** Since the explosion deals so much damage, it's important to prioritize avoiding it or using ultimate to counter it.

---

### Cannonballs (Long range)

**Description:** Similarly to the bullets attack, it shoots large, but slow, bullets for 4 seconds. They deal 95 damage.

---

### Summon kamikazes (Enraged)
**Description:** The boss summons 4 kamikazes with a delay of 0.75 seconds.

---

### Pursuit (Enraged)
**Description:** The boss pursues the player at a very high speed, while also being invincible and dealing increased collision damage.


## Attack choosing logic
There is a few mechanics here when the boss is choosing an attack. 

The first variable is long vs close range attacks. There's some attacks that are only chosen for specific ranges, but still many attacks are in the pool for both ranges.

The next variable is the amount of currently alive enemies. If the boss detects that an enemy is present, it will not spawn offensives, but instead a healer. This limitation exists so the fight does not become too overwhelming (offensive summons two times in a row is already incredibly difficult to play against). 

Also, if there is more than 3 spawned enemies alive, it will not opt to summon anything. This is to prevent the boss spawning a large number of healers which would be too strong.  

Finally, during the enraged state, it will not perform the kamikaze attack if there is any number of kamikaze still alive. Conversely, it will not perform a pursuit if there are no kamikaze alive (since that attack by itself is pointless if not paired with kamikazes).

