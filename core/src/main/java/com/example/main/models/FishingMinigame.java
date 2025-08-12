package com.example.main.models;

import com.example.main.enums.items.FishType;
import com.example.main.enums.items.MovementPattern;
import java.util.Random;

public class FishingMinigame {

    private final FishType fish;
    private float fishPosition;
    private float fishTargetPosition;
    private float fishMoveTimer = 0f;

    private float playerBarPosition = 0.5f;
    private float playerBarSize = 0.25f;

    private float captureProgress = 0.3f;
    private boolean isPerfect = true;
    private Random random = new Random();

    public FishingMinigame(FishType fish) {
        this.fish = fish;
        this.fishPosition = random.nextFloat() * 0.7f + 0.1f;
        this.fishTargetPosition = this.fishPosition;
    }


    public FishType getFish() { return fish; }
    public float getFishPosition() { return fishPosition; }
    public float getPlayerBarPosition() { return playerBarPosition; }
    public float getPlayerBarSize() { return playerBarSize; }
    public float getCaptureProgress() { return captureProgress; }
    public boolean isPerfect() { return isPerfect; }


    public void update(float delta, boolean playerHoldingUp, boolean playerHoldingDown) {

        final float BAR_SPEED = 0.6f;
        if (playerHoldingUp) {
            playerBarPosition += BAR_SPEED * delta;
        }
        if (playerHoldingDown) {
            playerBarPosition -= BAR_SPEED * delta;
        }

        playerBarPosition = Math.max(0, Math.min(1.0f - playerBarSize, playerBarPosition));


        fishMoveTimer += delta;
        if (fishMoveTimer >= 0.5f) {
            fishMoveTimer = 0f;
            updateFishTarget();
        }

        fishPosition = lerp(fishPosition, fishTargetPosition, delta * 3.0f);


        float barTop = playerBarPosition + playerBarSize;
        float barBottom = playerBarPosition;

        if (fishPosition >= barBottom && fishPosition <= barTop) {

            captureProgress += 0.33f * delta;
        } else {

            isPerfect = false;
            captureProgress -= 0.25f * delta;
        }
        captureProgress = Math.max(0, Math.min(1.0f, captureProgress));
    }


    private void updateFishTarget() {
        MovementPattern pattern = fish.getMovementPattern();
        float moveRange = 0.4f;

        if (pattern == MovementPattern.DARTER) {
            moveRange = 0.72f;
        }


        float randomChange = (random.nextFloat() * 2f - 1f) * moveRange;


        switch (pattern) {
            case SINKER:

                this.fishTargetPosition -= (random.nextFloat() * 0.15f);
                break;
            case FLOATER:

                this.fishTargetPosition += (random.nextFloat() * 0.15f);
                break;
            case SMOOTH:

                float direction = Math.signum(fishTargetPosition - fishPosition);

                if (direction == 0) direction = (random.nextBoolean() ? 1 : -1);
                this.fishTargetPosition += (direction * random.nextFloat() * 0.1f);
                break;
        }

        this.fishTargetPosition += randomChange;

        this.fishTargetPosition = Math.max(0, Math.min(1.0f, this.fishTargetPosition));
    }


    private float lerp(float start, float end, float t) {

        t = Math.min(t, 1.0f);
        return start + t * (end - start);
    }
}
