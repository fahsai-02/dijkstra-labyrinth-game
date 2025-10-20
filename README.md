# The Labyrinth - Tower of Trials

"The Labyrinth - Tower of Trials" is a 2D Adventure RPG project developed in Java Swing. Players take on the role of an adventurer who must navigate through 3 maze-like stages, each represented as a graph, to ultimately defeat a final boss.

## Features

* **Graph-Based Navigation:**
    * The game map is designed as nodes and edges, with each edge having a weight representing distance.
    * Traveling consumes MP (and HP if MP runs out) based on the distance moved, penalizing inefficient routes.

* **Turn-Based Combat:**
    * When the player moves to a monster node ('M'), the game transitions to a turn-based combat screen.
    * Players can choose "Normal Attack" or "Hard Attack," which costs MP but deals more damage.
    * Defeating monsters rewards the player with Gold.

* **Undo System:**
    * Players can undo their last move up to 5 times per stage, allowing them to replan their route if they make a mistake.

* **Shortest Path Scoring System:**
    * At the end of each stage, the game uses **Dijkstra's Algorithm** to calculate the shortest possible path from the start to the end node.
    * The player's score is calculated by comparing their actual travel distance to this optimal shortest path.

* **Shop & Upgrade System:**
    * After completing each stage, the player enters a shop.
    * Players can spend their accumulated Gold to buy items that provide permanent stat increases (ATK, DEF, Max HP, Max MP).

* **Boss Battle:**
    * After clearing all 3 stages, the player must face the final boss, "Tung Tung Tung Sahur".
    * Defeating the boss results in the 'Win' game-ending scenario.

## How to Run

1.  Ensure you have the Java Development Kit (JDK) installed.
2.  Compile all `.java` files:
    ```bash
    javac *.java
    ```
3.  Run the main class `RunGame`:
    ```bash
    java RunGame
    ```
4.  **Important:** The `assets` folder (containing all images and resources) must be in the same directory as the compiled classes.

## Core Project Structure

* **Main Controller & Entry Point**
    * `RunGame.java`: The main `JFrame` class that controls panel switching (e.g., from Menu to Game to Shop) and serves as the game's entry point.

* **Core Gameplay Panels**
    * `GamePanel.java`: Manages the graph-based map screen, player movement, resource depletion (HP/MP), and the Undo system.
    * `FightPanel.java`: Manages the turn-based combat screen against regular monsters.
    * `BossFightPanel.java`: Manages the combat screen for the final boss.
    * `ItemSelection.java`: Manages the shop screen, allowing players to purchase items with Gold.

* **Data Structures & Algorithms**
    * `MyGraph.java`: The core class for the Graph (Adjacency List) data structure. It also includes the implementation of Dijkstra's algorithm to find the shortest path.
    * `MapData.java`: A static class that stores all the data for the 3 stages, including node positions, edges, and image paths.

* **Data Models**
    * `CharacterStatus.java`: A class that stores all player stats, including HP, MP, ATK, DEF, Gold, and total Score.
    * `MonsterStatus.java`: A database class containing the stats for all monsters and the final boss.
    * `ItemList.java`: A database class containing all items, their stats, and prices for the shop.

* **Utility & UI Panels**
    * `MainMenu.java`: The main menu screen (Play, How to Play, Exit).
    * `HowToPlayPanel.java`: The "How to Play" instruction screen.
    * `EndGamePanel.java`: The game over (win/lose) screen, which displays the final total score.
    * `ScoreCalculator.java`: A utility class with static methods for calculating stage scores based on path distance.
