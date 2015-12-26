package me.vowed.api.pets.ai.pathfinding.astarmain;

/**
 * Created by JPaul on 11/24/2015.
 */
public enum PathingResult {

    SUCCESS(0),
    NO_PATH(-1);

    private final int ec;

    PathingResult(int ec){
        this.ec = ec;
    }

    public int getEndCode(){
        return this.ec;
    }

}
