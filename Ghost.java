/**
 * Ghost Class
 * 
 * Available functions (see Assignment document for explanations on what each function does):
 * treeFront, treeAbove, treeBelow, treeToLeft, treeToRight,
 * getDirection, setDirection,
 * move,
 * isScared,
 * animate, animateDead, animateScared,
 * getClara, getGhostHealer,
 * isAboveMe, isBelowMe, isToMyLeft, isToMyRight,
 * makeClaraDead,
 * playGhostEatenSound,
 * isPacmanIntroStillPlaying,
 * wrapAroundWorld
 */
class Ghost extends Character {
    // Add and initialize Ghost variables here
    private int speed = 4;
    private int animateCounter = 0;
    private boolean passedIntersection = true;
    private boolean dead = false;
    

    /**
     * Act method, runs on every frame
     */
    public void act() {
        // Wait for the Pacman intro to finish playing
        if (isPacmanIntroStillPlaying()) {
            return; 
        }

        // When the Ghost is Dead
        if (dead) {
            animateDead();

            // Check if the ghost intersects with the ghost healer
            if (intersects(getGhostHealer())) {
                dead = false; // Ghost alive
            } else {
                // Ghost moves to the ghost healer
                movetoGhostHealer();
            }

            // Ensures ghost is dead until it intersects with ghost healer
            return; 
        }

        // When the Ghost is Alive
        if (!treeFront()) {
            move(speed);
            // Prevents going outside the game boundaries
            wrapAroundWorld();
        } else {
            setDirection("");
            passedIntersection = true;
        }

        // Prioritise Ghost to go up when it touches ghost healer to leave the starting room
        if (intersects(getGhostHealer())) {
            setDirection("up");
            return;
        }

        if (passedIntersection && isIntersection()) {
            selectRandomDirection();
            passedIntersection = false;
        }

        animateGhost();

        if (intersects(getClara())) {
            if (isScared()) {
                // Ghost dies
                dead = true;
                // If Clara kills the ghost it will play the death sound once
                playGhostEatenSound();
                return;
            } else {
                // Clara dies
                makeClaraDead();
            }
        }
    }

    private void animateGhost() {
        animateCounter++;

        // Animation speed
        if (animateCounter % 5 == 0) {
            if (isScared()) {
                // Animate scared
                animateScared();
            } else {
                // Animate alive
                animate();
            }
        }
    }

    // Checks the trees around the ghost to determine if it is at an intersection
    private boolean isIntersection() {
        int intersectingPaths = 0;

        if (!treeAbove()) {
            intersectingPaths++;
        }
        if (!treeBelow()) {
            intersectingPaths++;
        }
        if (!treeToLeft()) {
            intersectingPaths++;
        }
        if (!treeToRight()) {
            intersectingPaths++;
        }

        // Ghost knows it is at an intersection when 3 trees are not near them
        return intersectingPaths >= 3;
    }

    // Selects a random direction to turn
    private void selectRandomDirection() {
        // Randomly select a direction
        int randomDirection = (int) (Math.random() * 4);

        switch (randomDirection) {
            case 0:
                if (!treeAbove()) {
                    setDirection("up");
                }
                break;
            case 1:
                if (!treeBelow()) {
                    setDirection("down");
                }
                break;
            case 2:
                if (!treeToLeft()) {
                    setDirection("left");
                }
                break;
            case 3:
                if (!treeToRight()) {
                    setDirection("right");
                }
                break;
        }
    }

    private void movetoGhostHealer() {
        // Check if the ghost intersects with the ghosthealer to be revived
        if (intersects(getGhostHealer())) {
            // Ghost becomes alive
            dead = false; 
        } else {
            // Path towards the ghosthealer 
            if (!treeAbove() && isAboveMe(getGhostHealer())) {
                setDirection("up");
            } else if (!treeToLeft() && isToMyLeft(getGhostHealer())) {
                setDirection("left");
            } else if (!treeToRight() && isToMyRight(getGhostHealer())) {
                setDirection("right");
            } else if (!treeBelow() && isBelowMe(getGhostHealer())) {
                setDirection("down");
            } 

            // If there is a tree in the way it selects a random direction so it wont get stuck
            if (treeFront()) {
                selectRandomDirection();
            }

            // Ghost moves
            if (!treeFront()) {
                // To revive faster and makes it more challenging!!
                move(8);
                wrapAroundWorld();
            }
        }
    }

    private boolean treeAbove() {
        return false;
    }

    private boolean treeBelow() {
        return false;
    }

    private boolean treeToLeft() {
        return false;
    }

    private boolean treeToRight() {
        return false;
    }

}
