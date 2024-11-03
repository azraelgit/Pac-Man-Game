/**
 * MyClara
 * 
 * Available functions (see Assignment document for explanations on what each function does):
 * treeFront, ghostWallFront,
 * getDirection, setDirection,
 * move,
 * makeScared, isScared,
 * animate, animateDead, 
 * onLeaf, removeLeaf, 
 * onMushroom, removeMushroom,
 * allLeavesEaten, 
 * isClaraDead,
 * playClaraDieSound, isClaraDieSoundStillPlaying,
 * playLeafEatenSound,
 * playPacmanIntro, isPacmanIntroStillPlaying,
 * wrapAroundWorld,
 * getCurrentLevelNumber, advanceToLevel
 */
class MyClara extends Clara {
    // Please leave this first level here,
    // until after you've completed "Part 12 -
    // Making and Adding Levels"
    public final char[][] LEVEL_1 = {
        {'#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#'},
        {'#','$','.','.','.','.','.','.','.','.','.','.','.','.','.','.','.','$','#'},
        {'#','.','#','#','.','#','.','#','#','#','#','#','.','#','.','#','#','.','#'},
        {'#','.','.','.','.','#','.','.','.','.','.','.','.','#','.','.','.','.','#'},
        {'#','#','#','#','.','#',' ','#','#','|','#','#','.','#','.','#','#','#','#'},
        {' ',' ',' ',' ','.',' ',' ','#','%','?','%','#','.',' ','.',' ',' ',' ',' '},
        {'#','#','#','#','.','#',' ','#','#','#','#','#','.','#','.','#','#','#','#'},
        {'#','.','.','.','.','.','.','.','.','#','.','.','.','.','.','.','.','.','#'},
        {'#','.','#','#','.','#','#','#','.','#','.','#','#','#','.','#','#','.','#'},
        {'#','$','.','#','.','.','.','.','.','@','.','.','.','.','.','.','.','$','#'},
        {'#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#'}
    };

    public final char[][] LEVEL_2 = {
        {'#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#'},
        {'#','$','#','#','.','.','.','.','.','.','.','.','.','.','.','.','.','.','#'},
        {'#','.','#','#','.','#','.','#','#','.','#','#','.','#','.','#','#','.','#'},
        {'#','.','.','.','.','#','.','.','.','.','.','.','.','#','.','#','$','.','#'},
        {'#','#','#','#','.','#','#','#','#','|','#','#','#','#','.','#','#','#','#'},
        {'.','.','.','.','.','.','.','#','%','?','%','#','.','.','.','.','.','.','.'},
        {'#','#','#','#','.','#','.','#',' ','%',' ','#','.','#','.','#','#','#','#'},
        {'#','.','#','#','.','#','$','#','#','#','#','#','.','.','.','.','.','.','#'},
        {'#','.','#','#','.','#','#','#','#','#','#','#','#','#','.','#','#','.','#'},
        {'#','$','.','.','.','.','.','.','.','@','.','.','.','.','.','.','.','$','#'},
        {'#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#'}
    };

    public final char[][] LEVEL_3 = {
        {'#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#'},
        {'#','.','.','.','.','#','.','.','.','.','.','.','.','.','.','.','.','.','#'},
        {'#','.','#','#','.','#','.','#','#','.','#','#','.','#','.','#','#','.','#'},
        {'#','$','#','#','.','.','.','.','.','.','.','.','.','#','.','#','$','.','#'},
        {'#','#','#','#','.','#','#','#','#','|','#','#','.','#','.','#','#','#','#'},
        {'.','.','.','.','.','#','.','#','%',' ','%','#','.','#','.','.','.','.','.'},
        {'#','.','#','#','.','#','.','|',' ',' ',' ','|','.','.','.','#','#','#','#'},
        {'#','.','#','.','.','#','.','#','%','?','%','#','.','#','.','#','.','.','#'},
        {'#','.','#','#','.','#','.','#','#','#','#','#','.','#','.','#','#','.','#'},
        {'#','.','$','#','.','.','.','.','.','@','.','.','.','.','.','.','.','$','#'},
        {'#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#'}
    };


    // Movement constants
    public final String UP = "up";    
    public final String DOWN = "down";    
    public final String LEFT = "left";    
    public final String RIGHT = "right";   
    
    // Add and initialise Clara's variables here
    private int speed = 7;
    private boolean isMovementStarted = false; 
    private boolean shouldMove = false;
    private boolean acceptInput = true;
    private int actCounter = 0;

    /**
     * Act method
     * 
     * Runs on every frame
     */
    public void act() {
        
        handleInput();

        // Play Pacman intro and prevent movement until the intro stops playing
        if (isPacmanIntroStillPlaying()) {
            // Prevents Clara from moving until the intro has stopped playing
            shouldMove = false;
            playPacmanIntro();
        } else {
            // Clara can move now
            shouldMove = isMovementStarted;
        }

        if (shouldMove) {
            
            // When Clara is alive
            animateClara();

            if (!treeFront() && !ghostWallFront()) {
                move(speed);
            }
            // Prevents Clara from leaving the game boundaries and to circle back
            wrapAroundWorld();
        }

        

        // Remove leaf and play sound
        if (onLeaf()) {
            removeLeaf();
            playLeafEatenSound();
        }

        // Check if all leaves have been eaten and advance to the next level
        if (allLeavesEaten()) {
            // Play Pacman Intro again when Clara enters new level
            playPacmanIntro();
            // Gets the current level number and is used below at the if statement
            int currentLevel = getCurrentLevelNumber();
            char[][] nextLevel = null;
    
            // Check if there is a next level available and the level number
            if (currentLevel < 3) {
                // Loads the next level based on the current level number
                switch (currentLevel) {
                    case 1:
                    nextLevel = LEVEL_2;
                    break;
                    case 2:
                    nextLevel = LEVEL_3;

                }
            }
    
            // Return to the first level if all levels are complete
            advanceToLevel(nextLevel != null ? nextLevel : LEVEL_1);
            
        }

        // Check if Clara is dead
        if (isClaraDead()) {
            if (isClaraDieSoundStillPlaying()) {
                // When Clara is dead a death sound occurs
                playClaraDieSound();
            }

            // Animates when Clara is dead
            animateDead();
            // Stop Clara's movement
            acceptInput = false;
            // Stop Clara's movement
            shouldMove = false; 
        }

        if (onMushroom()) {
            removeMushroom();
            // Ghost become scared
            makeScared();
        }
    }
    
    // Clara's movement inputs using arrow keys
    private void handleInput() {
        if (!acceptInput) {
            return;
        }

        if (Keyboard.isKeyDown(UP)) {
            setDirection(UP);
            resetMovement();
        } else if (Keyboard.isKeyDown(DOWN)) {
            setDirection(DOWN);
            resetMovement();
        } else if (Keyboard.isKeyDown(LEFT)) {
            setDirection(LEFT);
            resetMovement();
        } else if (Keyboard.isKeyDown(RIGHT)) {
            setDirection(RIGHT);
            resetMovement();
        }
    }

    //Prevent Clara from moving until an input is pressed
    private void resetMovement() {
        isMovementStarted = true;
        shouldMove = false;
    }

    private void animateClara() {
        actCounter++;

        if (actCounter % 5 == 0) {
            animate();
        }
    }
}