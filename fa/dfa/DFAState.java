package fa.dfa;
import fa.State;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *  Represents a single state in a Deterministic Finite Automaton.
 * @author ChandlerBlack
 * */
public class DFAState extends State {

    // A map of the transitions of a DFA
    private Map<Character, DFAState> transitions;


    /**
     * Constructor that sets the name of the state and initializes the transition map.
     * @param name The string label for this state.
     */
    public DFAState(String name) {
        super(name);
        transitions = new LinkedHashMap<>();
    }

    /**
     * Adds a transition from this state to another state based on an input symbol.
     * @param onSymb The alphabet character triggering the transition.
     * @param toState The destination DFAState.
     */
    public void addTransition(char onSymb, DFAState toState) {
        transitions.put(onSymb, toState);
    }

    /**
     * Retrieves the next state given a specific input symbol.
     * @param onSymb The alphabet character.
     * @return The destination DFAState, or null if no transition exists.
     */
    public DFAState getTransition(char onSymb) {
        return transitions.get(onSymb);
    }

}
