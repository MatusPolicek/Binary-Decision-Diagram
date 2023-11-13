import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {

        BDD bdd = BDD_create_with_best_order("A!BC!L!M + F!G!HGI + !DE!KLGM + FG!H!IJ + KL!FAB!C + !BC!ID!F + FK!DC + KA!BG + FL!CD!E + FGA!K + LB!JAC + KL!B!AC + KAB!EF + CBA!KL");
        System.out.println("Vytvoreny: " + bdd.getNodesSize() + " s poradim " + bdd.getOrder());
         //bdd.print();
        System.out.println(bdd.use("0101010110001"));
    }
    public static BDD BDD_create(String bfunkcia, String poradie) {
        return new BDD(bfunkcia, poradie);
    }
    public static BDD BDD_create_with_best_order(String bfunkcia) {
        char[] characters = getUniqueCharacters(bfunkcia);
        StringBuilder poradieBuilder = new StringBuilder();
        for (char c : characters) {
            poradieBuilder.append(c);
        }
        BDD bdd = null;
        String poradie = poradieBuilder.toString();
        List<String> permutations = generateRandomStrings(poradie, poradie.length());

        for (String p : permutations) {
            BDD temp = new BDD(bfunkcia, p);
            if (bdd == null) {
                bdd = temp;
            } else if (bdd.getNodesSize() > temp.getNodesSize()) {
                bdd = temp;
            }
        }
        return bdd;
    }

    private static List<String> generateRandomStrings(String poradie, int length) {
        List<String> strings = new ArrayList<>();
        Random random = new Random();
        char[] characters = poradie.toCharArray();

        for (int i = 0; i < length; i++) {
            for (int j = characters.length - 1; j > 0; j--) {
                int k = random.nextInt(j + 1);
                char temp = characters[j];
                characters[j] = characters[k];
                characters[k] = temp;
            }
            StringBuilder s = new StringBuilder();
            for (char c : characters)
                s.append(c);
            String string = new String(s);
            strings.add(string);
        }
        return strings;
    }

    private static char[] getUniqueCharacters(String bfunkcia) {
        List<Character> uniqueCharsList = new ArrayList<>();
        for (char c : bfunkcia.toCharArray()) {
            if ((Character.isLowerCase(c) || Character.isUpperCase(c)) && !uniqueCharsList.contains(c)) {
                uniqueCharsList.add(c);
            }
        }
        char[] uniqueCharsArray = new char[uniqueCharsList.size()];
        for (int i = 0; i < uniqueCharsList.size(); i++) {
            uniqueCharsArray[i] = uniqueCharsList.get(i);
        }
        return uniqueCharsArray;
    }

    public static String BDD_use(BDD bdd, String vstupy) {
        return bdd.use(vstupy);
    }
    public static List<String> processFunctionString(String functionString, String order) {
        String[] terms = functionString.split("\\+");
        List<String> processedTerms = new ArrayList<>();
        Pattern pattern = Pattern.compile("!(\\p{Lu})");
        for (String term : terms) {
            Matcher matcher = pattern.matcher(term);
            while (matcher.find()) {
                String match = matcher.group(1);
                term = term.replace("!" + match, match.toLowerCase());
            }
            term = term.replaceAll("!", "");
            term = term.replaceAll(" ", "");

            String[] subStrings = term.split("");
            Set<String> uniqueSubStrings = new HashSet<String>(Arrays.asList(subStrings));
            String output = String.join("", uniqueSubStrings);
            processedTerms.add(output);
        }
        while (!order.isEmpty()) {
            String chars = Character.toString(order.charAt(0));
            processedTerms.removeIf(term -> term.contains(chars) && term.contains(chars.toLowerCase()));
            order = order.substring(1);
        }
        Set<String> uniqueTerms = new HashSet<>(processedTerms);

        return new ArrayList<>(uniqueTerms).stream().sorted().collect(Collectors.toList());
    }
}