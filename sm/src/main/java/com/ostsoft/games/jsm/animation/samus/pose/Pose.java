package com.ostsoft.games.jsm.animation.samus.pose;

import com.ostsoft.games.jsm.util.BitHelper;
import com.ostsoft.games.jsm.util.ByteStream;

import java.util.ArrayList;
import java.util.List;

public class Pose {
    public static int posePropertiesTableOffset = 0x8B629;
    public static int transitionTableOffset = 0x89EE2;

    public List<Transition> transitions;
    public Properties properties;


    public Pose read(ByteStream stream, int pose) {
        // Transition table
        stream.setPosition(transitionTableOffset + (pose * 2));
        int transitionPointer = stream.readReversedUnsignedShort() | (0x91 << 16);

        stream.setPosition(BitHelper.snesToPc(transitionPointer));
        transitions = new ArrayList<>();
        Transition transition = new Transition().read(stream);
        transition.read(stream);
        while (transition.newButtons != 0xFFFF) {
            transitions.add(transition);
            transition = new Transition().read(stream);
        }

        // Pose properties
        stream.setPosition(posePropertiesTableOffset + (pose * 8));
        properties = new Properties().read(stream);

        return this;
    }

}
