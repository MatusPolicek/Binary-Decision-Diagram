import java.util.*;
import java.util.stream.Collectors;

public class BDD {
    private List<Node> nodes = new ArrayList<>();
    private String poradie;
    private Node finalTrue = new Node(true);
    private Node finalFalse = new Node(false);
    private Node root;
    public BDD(String expression, String order) {
        this.poradie = order;
        List<String> terms = Main.processFunctionString(expression, order);
        this.root = createNodes(terms, order);
    }
    private Node createNodes(List<String> terms, String order) {
        if (order.isEmpty() || terms.isEmpty()) {
            return null;
        }
        String var = Character.toString(order.charAt(0));
        boolean clearContains = false;
        boolean clearNotContains = false;
        Node node = new Node(terms, var, this);
        /*
        Pokial uz taky node v array nodes existuje tak vrati dany node z arrayu
        */
        for (Node x : nodes) {
            if (node.getTerms().equals(x.getTerms()) && node.getCharacter().equals(x.getCharacter()))
                return x;
        }
        /*
        Vytvorenie listov constains(formuly ktore dany chracter obsahuju)
        a notContains (formuly ktore dany charcter neobsahuju)
        **Negacia je riesena ze neobsahuje**
        */
        List<String> contains = new ArrayList<>();
        List<String> notContains = new ArrayList<>();
        for (String term : terms) {
            if (term.contains(var)) {
                if (term.equals(var))
                    clearContains = true;
                contains.add(term.replace(var, ""));
            } else {
                if (term.equals(var.toLowerCase()))
                    clearNotContains = true;
                if (term.contains(var.toLowerCase()))
                    term = term.replace(var.toLowerCase(), "");
                notContains.add(term);
            }
        }
        contains.removeIf(String::isEmpty);
        notContains.removeIf(String::isEmpty);
        contains.addAll(notContains);
        Set<String> unique = new HashSet<>(contains);
        contains = new ArrayList<>(unique).stream().sorted().collect(Collectors.toList());

        /*
        Ak obsahuje A aj !A oddelene "+" to znamena
        ze ak je A false alebo true tak vzdy bude vysledok
        true
        0+1 1+0 = 1
        */
        if (clearContains && clearNotContains) {
            node.setOne(finalTrue);
            node.setZero(finalTrue);
            return node;
        }
        /*
        Ak obsahuje iba A a najde sa take tak mozme
        prejst iba na stranu kde je A false
        */
        if (clearContains) {
            for (String i : contains) {
                if (!i.contains(var.toLowerCase())) {
                    if (!notContains.contains(i))
                        notContains.add(i);
                }
            }
            node.setZero(createNodes(notContains, order.substring(1)));
            if (node.getZero() == null)
                node.setZero(finalFalse);
            node.setOne(finalTrue);
            node.upDate();
            return node;
        }
        /*
        Ak obsahuje iba !A a najde take tak mozme
        prejst iba na stranu je A true
        */
        if (clearNotContains) {
            for (String i : notContains) {
                if (!i.contains(var.toLowerCase())) {
                    if (!contains.contains(i))
                        contains.add(i);
                }
            }
            node.setOne(createNodes(contains, order.substring(1)));
            if (node.getOne() == null)
                node.setOne(finalFalse);
            node.setZero(finalTrue);
            node.upDate();
            return node;
        }
        /*
        Ak sa nenachadza nikde negacia daneho charu tak assigneme false a true
        rekurzivne
        */
        if (!clearContains && !clearNotContains) {
            node.setOne(createNodes(contains, order.substring(1)));
            node.setZero(createNodes(notContains, order.substring(1)));
        }
        /*
        Po assignuti (children) true false tak ak su null
        */
        if (node.getOne() == null && !clearContains) {
            node.setOne(finalTrue);
        }
        if (node.getZero() == null && !clearNotContains) {
            node.setZero(finalFalse);
        }
        node.upDate();
        return node;
    }

    public void put(Node node) {
        nodes.add(node);
    }
    public int getNodesSize() {
        return nodes.size();
    }
    public void print() {
        for (Node node : nodes) {
            System.out.println(node.getCharacter() + node.getTerms());
        }
        printBDD(this.root);
    }
    private void printBDD(Node node) {
        if (node == null) {
            return;
        }
        if (node.getCondition() == null) {
            System.out.println(node + "\nNodes character: " + node.getCharacter() + "\nNodes terms: " + node.getTerms() + "\nNodes 0 child: " + node.getZero() + "\nNodes 1 child: " + node.getOne());
        } else {
            System.out.println(node + "\nNodes condition: " + node.getCondition());
        }
        printBDD(node.getZero());
        printBDD(node.getOne());
    }

    public String use(String vstupy) {
        return useBdd(this.root, vstupy);
    }

    private String useBdd(Node node, String vstupy) {
        if (node.getCondition() != null)
            return (Boolean) node.getCondition() ? "1" : "0";
        if (vstupy.isEmpty())
            return "";
        String value = Character.toString(vstupy.charAt(0));
        vstupy = vstupy.substring(1);
        if (value.equals("1"))
            return useBdd(node.getOne(), vstupy);
        if (value.equals("0"))
            return useBdd(node.getZero(), vstupy);

        return "";
    }
    public String getOrder() {
        return this.poradie;
    }
}
