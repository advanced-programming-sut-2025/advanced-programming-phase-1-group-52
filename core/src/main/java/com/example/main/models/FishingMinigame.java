package com.example.main.models;

import com.example.main.enums.items.FishType;
import com.example.main.enums.items.MovementPattern; // Import the separate MovementPattern enum
import java.util.Random;

public class FishingMinigame {

    private final FishType fish;
    private float fishPosition; // Vertical position, 0.0 (bottom) to 1.0 (top)
    private float fishTargetPosition;
    private float fishMoveTimer = 0f;

    private float playerBarPosition = 0.5f; // Player's green bar position
    private float playerBarSize = 0.25f; // The height of the player's bar

    private float captureProgress = 0.3f; // Starts with some progress
    private boolean isPerfect = true;
    private Random random = new Random();

    public FishingMinigame(FishType fish) {
        this.fish = fish;
        this.fishPosition = random.nextFloat() * 0.7f + 0.1f; // Start somewhere in the middle
        this.fishTargetPosition = this.fishPosition;
    }

    // Getters
    public FishType getFish() { return fish; }
    public float getFishPosition() { return fishPosition; }
    public float getPlayerBarPosition() { return playerBarPosition; }
    public float getPlayerBarSize() { return playerBarSize; }
    public float getCaptureProgress() { return captureProgress; }
    public boolean isPerfect() { return isPerfect; }

    /**
     * Updates the state of the minigame for one frame.
     * @param delta Time since the last frame.
     * @param playerHoldingUp True if the player is pressing the up key.
     * @param playerHoldingDown True if the player is pressing the down key.
     */
    public void update(float delta, boolean playerHoldingUp, boolean playerHoldingDown) {
        // --- Player Bar Movement ---
        final float BAR_SPEED = 0.6f;
        if (playerHoldingUp) {
            playerBarPosition += BAR_SPEED * delta;
        }
        if (playerHoldingDown) {
            playerBarPosition -= BAR_SPEED * delta;
        }
        // Clamp player bar position within bounds [0, 1]
        playerBarPosition = Math.max(0, Math.min(1.0f - playerBarSize, playerBarPosition));

        // --- Fish Movement ---
        fishMoveTimer += delta;
        if (fishMoveTimer >= 0.5f) { // Update fish target every half second
            fishMoveTimer = 0f;
            updateFishTarget();
        }
        // Smoothly move fish towards its target position
        fishPosition = lerp(fishPosition, fishTargetPosition, delta * 3.0f);

        // --- Capture Progress Logic ---
        float barTop = playerBarPosition + playerBarSize;
        float barBottom = playerBarPosition;

        if (fishPosition >= barBottom && fishPosition <= barTop) {
            // Fish is inside the bar, increase progress
            captureProgress += 0.33f * delta; // Fills in about 3 seconds
        } else {
            // Fish is outside, decrease progress and it's no longer a perfect catch
            isPerfect = false;
            captureProgress -= 0.25f * delta; // Empties in about 4 seconds
        }
        captureProgress = Math.max(0, Math.min(1.0f, captureProgress));
    }

    /**
     * Calculates the next target position for the fish based on its movement pattern.
     */
    private void updateFishTarget() {
        MovementPattern pattern = fish.getMovementPattern();
        float moveRange = 0.4f; // Default range (corresponds to a move of 5)

        if (pattern == MovementPattern.DARTER) {
            moveRange = 0.72f; // Corresponds to a move of 9
        }

        // Base random movement
        float randomChange = (random.nextFloat() * 2f - 1f) * moveRange;

        // Apply pattern-specific logic
        switch (pattern) {
            case SINKER:
                // Higher chance to move down
                this.fishTargetPosition -= (random.nextFloat() * 0.15f);
                break;
            case FLOATER:
                // Higher chance to move up
                this.fishTargetPosition += (random.nextFloat() * 0.15f);
                break;
            case SMOOTH:
                // Tend to continue in the same direction
                float direction = Math.signum(fishTargetPosition - fishPosition);
                // If the fish is already at its target, give it a new random direction
                if (direction == 0) direction = (random.nextBoolean() ? 1 : -1);
                this.fishTargetPosition += (direction * random.nextFloat() * 0.1f);
                break;
        }

        this.fishTargetPosition += randomChange;
        // Clamp target position to ensure it stays within the bar's bounds [0, 1]
        this.fishTargetPosition = Math.max(0, Math.min(1.0f, this.fishTargetPosition));
    }

    /**
     * Linear interpolation for smooth movement.
     */
    private float lerp(float start, float end, float t) {
        // Clamp t to a max of 1 to prevent overshooting
        t = Math.min(t, 1.0f);
        return start + t * (end - start);
    }
}
