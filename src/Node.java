import java.util.List;

public class Node {
    private List<String> terms;
    private String character;
    private Node one, zero;
    private BDD bdd;
    public Node(List<String> terms, String character, BDD bdd) {
        this.terms = terms;
        this.character = character;
        this.one = null;
        this.zero = null;
        this.bdd = bdd;
    }
    private Object condition = null;
    public Node(boolean condition) {
        this.condition = condition;
    }
    public Object getCondition() {
        return condition;
    }
    public List<String> getTerms() {
        return this.terms;
    }
    public String getCharacter() {
        return this.character;
    }
    public Node getOne() {
        return one;
    }
    public Node getZero() {
        return zero;
    }
    public void setOne(Node one) {
        this.one = one;
    }
    public void setZero(Node zero) {
        this.zero = zero;
    }
    public void upDate() {
        this.bdd.put(this);
    }
}
