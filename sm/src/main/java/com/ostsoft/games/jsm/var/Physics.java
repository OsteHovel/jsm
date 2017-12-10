package com.ostsoft.games.jsm.var;

import com.ostsoft.games.jsm.util.ByteStream;

import java.util.ArrayList;

public class Physics {
    private ArrayList<Variable> variables = new ArrayList<>();

    public void load(ByteStream stream) {
        variables.add(new Variable("Terminal fall velocity", Type.SHORT, 0x909110));
        variables.add(new Variable("Damage to Samus in lava", Type.SHORT, 0x909E8D));
        variables.add(new Variable("Damage to Samus in lava subunits", Type.SHORT, 0x909E8B));
        variables.add(new Variable("Damage to Samus in acid", Type.SHORT, 0x909E91));
        variables.add(new Variable("Damage to Samus in acid subunits", Type.SHORT, 0x909E8F));
        variables.add(new Variable("Slowdown in water", Type.SHORT, 0x909E93));
        variables.add(new Variable("Slowdown in lava", Type.SHORT, 0x909E95));
        variables.add(new Variable("Maximum distance from a wall (in pixels) Samus can be for walljump to work", Type.SHORT, 0x909E9F));

        for (Variable variable : variables) {
            variable.load(stream);
        }
    }


    public void save(ByteStream stream) {
        for (Variable variable : variables) {
            variable.save(stream);
        }
    }

    public void printDebug() {
        for (Variable variable : variables) {
            System.out.println(variable.getName() + ": " + variable.getValue());
        }
    }

    public ArrayList<Variable> getVariables() {
        return variables;
    }

}
