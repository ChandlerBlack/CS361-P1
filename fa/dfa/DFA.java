package fa.dfa;
import fa.State;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * This class represents a DFA
 * @author ChandlerBlack,
 * */
public class DFA implements DFAInterface {

    // Using LinkedHashSet guarantees that the insertion order is preserved,
    private LinkedHashSet<DFAState> states;
    private LinkedHashSet<Character> sigma;
    private LinkedHashSet<DFAState> finalStates;
    private DFAState startState;


    /**
     * Constructor initializes empty sets and a null start state.
     */
    public DFA () {
        states = new LinkedHashSet<>();
        sigma = new LinkedHashSet<>();
        finalStates = new LinkedHashSet<>();
        startState = null;
    }


    @Override
    public boolean addTransition(String fromState, String toState, char onSymb) {
        DFAState from = (DFAState) getState(fromState);
        DFAState to = (DFAState) getState(toState);

        // Ensure both states exist and the symbol is part of the alphabet
        if (from == null || to == null || !sigma.contains(onSymb)) {
            return false;
        }

        from.addTransition(onSymb, to);
        return true;
    }

    @Override
    public DFA swap(char symb1, char symb2) {
        DFA swappedDFA = new DFA();

        // 1. Copy the alphabet
        for (char c : sigma) {
            swappedDFA.addSigma(c);
        }

        // 2. Copy all states
        for (DFAState s : states) {
            swappedDFA.addState(s.getName());
        }

        // 3. Set the start state
        if (startState != null) {
            swappedDFA.setStart(startState.getName());
        }

        // 4. Set the final states
        for (DFAState f : finalStates) {
            swappedDFA.setFinal(f.getName());
        }

        // 5. Copy transitions and swap labels where applicable
        for (DFAState s : states) {
            for (char c : sigma) {
                DFAState dest = s.getTransition(c);
                if (dest != null) {
                    char mappedSymb = c;
                    if (c == symb1) {
                        mappedSymb = symb2;
                    } else if (c == symb2) {
                        mappedSymb = symb1;
                    }
                    swappedDFA.addTransition(s.getName(), dest.getName(), mappedSymb);
                }
            }
        }

        return swappedDFA;
    }

    @Override
    public boolean addState(String name) {
        if (getState(name) != null) {
            return false; // State already exists
        }
        states.add(new DFAState(name));
        return true;
    }

    @Override
    public boolean setFinal(String name) {
        DFAState state = (DFAState) getState(name);
        if (state != null) {
            finalStates.add(state);
            return true;
        }
        return false;
    }

    @Override
    public boolean setStart(String name) {
        DFAState state = (DFAState) getState(name);
        if (state != null) {
            startState = state;
            return true;
        }
        return false;
    }

    @Override
    public void addSigma(char symbol) {
        sigma.add(symbol);
    }

    @Override
    public boolean accepts(String s) {
        if (startState == null) return false;

        DFAState current = startState;

        // Handle standard string traversal (and implicitly handles "" empty string)
        for (int i = 0; i < s.length(); i++) {
            // Note: If an 'e' is passed to represent empty string instead of "",
            // you may need to add an if-check here depending on exact test input formats.
            char c = s.charAt(i);
            current = current.getTransition(c);

            if (current == null) {
                return false; // Invalid transition
            }
        }

        return finalStates.contains(current);
    }

    @Override
    public Set<Character> getSigma() {
        return sigma;
    }

    @Override
    public State getState(String name) {
        for (DFAState s : states) {
            if (s.getName().equals(name)) {
                return s;
            }
        }
        return null;
    }

    @Override
    public boolean isFinal(String name) {
        DFAState state = (DFAState) getState(name);
        return state != null && finalStates.contains(state);
    }

    @Override
    public boolean isStart(String name) {
        DFAState state = (DFAState) getState(name);
        return state != null && state.equals(startState);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        // Q = { a b }
        sb.append("Q = { ");
        for (DFAState s : states) {
            sb.append(s.getName()).append(" ");
        }
        sb.append("}\n");

        // Sigma = { 0 1 }
        sb.append("Sigma = { ");
        for (char c : sigma) {
            sb.append(c).append(" ");
        }
        sb.append("}\n");

        // delta =
        //         0    1
        sb.append("delta =\n\t\t");
        for (char c : sigma) {
            sb.append(c).append("\t");
        }
        sb.append("\n");

        //     a   a    b
        for (DFAState s : states) {
            sb.append("\t").append(s.getName()).append("\t");
            for (char c : sigma) {
                DFAState dest = s.getTransition(c);
                if (dest != null) {
                    sb.append(dest.getName());
                } else {
                    sb.append("-"); // In case of incomplete DFA
                }
                sb.append("\t");
            }
            sb.append("\n");
        }

        // q0 = a
        sb.append("q0 = ");
        if (startState != null) {
            sb.append(startState.getName());
        }
        sb.append("\n");

        // F = { b }
        sb.append("F = { ");
        for (DFAState f : finalStates) {
            sb.append(f.getName()).append(" ");
        }
        sb.append("}\n");

        return sb.toString();
    }
}