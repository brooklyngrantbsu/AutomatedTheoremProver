import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayDeque;
import java.util.LinkedList;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.Vector;

public class prover {
    private Vector<String> kb;
    private long runi;
    private long huni;

    prover(LinkedList<String> linkedList) {
        this.kb = this.prepare(linkedList);
        this.runi = 0L;
        this.huni = 0L;
    }

    public long getRandunifies() {
        return this.runi;
    }

    public long getHunifies() {
        return this.huni;
    }

    private Vector<String> prepare(LinkedList<String> linkedList) {
        Vector<String> vector = new Vector<String>(linkedList.size());
        for (int i = 0; i < linkedList.size(); ++i) {
            String string = linkedList.get(i);
            if (string == null) continue;
            string = this.tagargs(string, i);
            vector.add(string);
        }
        return vector;
    }

    private String tagargs(String string, int n) {
        StringTokenizer stringTokenizer = new StringTokenizer(string, " ");
        int n2 = stringTokenizer.countTokens();
        Object object = "";
        for (int i = 0; i < n2; ++i) {
            object = (String)object + this.exprtag(stringTokenizer.nextToken(), n);
            if (i == n2 - 1) continue;
            object = (String)object + " ";
        }
        return object;
    }

    private String exprtag(String string, int n) {
        StringTokenizer stringTokenizer = new StringTokenizer(string, " ,()");
        int n2 = stringTokenizer.countTokens();
        String string2 = stringTokenizer.nextToken() + "(";
        for (int i = 1; i < n2; ++i) {
            Object object;
            if (i != 1) {
                string2 = string2 + ",";
            }
            if (this.variable((String)(object = stringTokenizer.nextToken()))) {
                object = (String)object + n;
            }
            string2 = string2 + (String)object;
        }
        string2 = string2 + ")";
        return string2;
    }

    boolean constant(String string) {
        return string.toUpperCase().equals(string);
    }

    boolean variable(String string) {
        return string.toLowerCase().equals(string);
    }

    boolean unifyable(String string, String string2) {
        StringTokenizer stringTokenizer = new StringTokenizer(string, " ,)(!");
        StringTokenizer stringTokenizer2 = new StringTokenizer(string2, " ,)(!");
        int n = stringTokenizer.countTokens();
        int n2 = stringTokenizer2.countTokens();
        return stringTokenizer.nextToken().equals(stringTokenizer2.nextToken()) && n == n2;
    }

    String apply(String string, String string2) {
        StringTokenizer stringTokenizer = new StringTokenizer(string, "- ");
        int n = stringTokenizer.countTokens();
        String string3 = string2;
        for (int i = 0; i < n / 2; ++i) {
            String string4 = stringTokenizer.nextToken();
            String string5 = stringTokenizer.nextToken();
            string3 = string3.replace(string4, string5);
        }
        return string3;
    }

    private String argsunify(String string, String string2) {
        StringTokenizer stringTokenizer = new StringTokenizer(string, " ,)(!");
        StringTokenizer stringTokenizer2 = new StringTokenizer(string2, " ,)(!");
        Object object = "";
        int n = stringTokenizer.countTokens();
        String string3 = stringTokenizer.nextToken();
        String string4 = stringTokenizer2.nextToken();
        if (this.constant(string3) && this.constant(string4)) {
            if (!string3.equals(string4)) {
                return null;
            }
        } else if (this.variable(string3)) {
            object = (String)object + string3 + "-" + string4;
        } else if (this.variable(string4)) {
            object = (String)object + string4 + "-" + string3;
        }
        String string5 = this.remaining(stringTokenizer);
        String string6 = this.remaining(stringTokenizer2);
        string5 = this.apply((String)object, string5);
        string6 = this.apply((String)object, string6);
        if (n - 1 == 0) {
            return " " + (String)object + " ";
        }
        String string7 = this.argsunify(string5, string6);
        if (string7 == null) {
            return string7;
        }
        return " " + (String)object + " " + string7 + " ";
    }

    String remaining(StringTokenizer stringTokenizer) {
        int n = stringTokenizer.countTokens();
        Object object = "";
        for (int i = 0; i < n; ++i) {
            object = (String)object + "," + stringTokenizer.nextToken();
        }
        return object;
    }

    String exprunify(String string, String string2) {
        StringTokenizer stringTokenizer = new StringTokenizer(string, " ,)(!");
        StringTokenizer stringTokenizer2 = new StringTokenizer(string2, " ,)(!");
        String string3 = "";
        int n = stringTokenizer.countTokens() - 1;
        int n2 = stringTokenizer2.countTokens() - 1;
        String string4 = stringTokenizer.nextToken();
        String string5 = stringTokenizer2.nextToken();
        if (n != n2 || !string4.equals(string5)) {
            return null;
        }
        string3 = this.argsunify(this.remaining(stringTokenizer), this.remaining(stringTokenizer2));
        return string3;
    }

    Vector<String> applyV(String string, Vector<String> vector) {
        Vector<String> vector2 = new Vector<String>(3);
        for (int i = 0; i < vector.size(); ++i) {
            String string2 = vector.get(i);
            string2 = this.apply(string, string2);
            vector2.add(string2);
        }
        return vector2;
    }

    String combine(Vector<String> vector, Vector<String> vector2) {
        Object object = "";
        Vector<String> vector3 = new Vector<String>(vector);
        vector3.addAll(vector2);
        this.compress(vector3);
        for (int i = 0; i < vector3.size(); ++i) {
            if (i != 0) {
                object = (String)object + " ";
            }
            object = (String)object + vector3.get(i);
        }
        return object;
    }

    private void compress(Vector<String> vector) {
        for (int i = 0; i < vector.size(); ++i) {
            for (int j = i; j < vector.size(); ++j) {
                if (i < 0) {
                    ++i;
                }
                if (j < 0) {
                    ++j;
                }
                if (!vector.get(i).equals(vector.get(j))) {
                    String string = vector.get(i);
                    String string2 = vector.get(j);
                    if (!(string = string.replace("!", "")).equals(string2 = string2.replace("!", ""))) continue;
                    if (i > j) {
                        vector.remove(i);
                        vector.remove(j);
                        --i;
                        --j;
                        continue;
                    }
                    vector.remove(j);
                    vector.remove(i);
                    --i;
                    --j;
                    continue;
                }
                if (i == j) continue;
                vector.remove(j);
                if (i > j) {
                    --i;
                }
                --j;
            }
        }
    }

    String unify(String string, String string2) {
        int n;
        StringTokenizer stringTokenizer = new StringTokenizer(string, " ");
        StringTokenizer stringTokenizer2 = new StringTokenizer(string2, " ");
        int n2 = stringTokenizer.countTokens();
        int n3 = stringTokenizer2.countTokens();
        boolean bl = false;
        Vector<String> vector = new Vector<String>(n2);
        Vector<String> vector2 = new Vector<String>(n3);
        for (n = 0; n < n2; ++n) {
            vector.add(stringTokenizer.nextToken());
        }
        for (n = 0; n < n3; ++n) {
            vector2.add(stringTokenizer2.nextToken());
        }
        for (n = 0; n < n2; ++n) {
            for (int i = 0; i < n3; ++i) {
                String string3;
                if (!this.unifyable(vector.get(n), vector2.get(i)) || (string3 = this.exprunify(vector.get(n), vector2.get(i))) == null) continue;
                bl = true;
                vector = this.applyV(string3, vector);
                vector2 = this.applyV(string3, vector2);
            }
        }
        if (bl) {
            return this.combine(vector, vector2);
        }
        return null;
    }

    public void printkb() {
        for (int i = 0; i < this.kb.size(); ++i) {
            System.out.println(this.kb.get(i));
        }
    }

    boolean failtest(Vector<Sentence> vector) {
        boolean bl = true;
        for (int i = 0; i < vector.size(); ++i) {
            Sentence sentence = vector.get(i);
            if (sentence.scores.size() == vector.size() && !sentence.scores.contains(new Integer(0))) continue;
            bl = false;
            break;
        }
        return bl;
    }

    private void updatescores(Sentence sentence, Vector<Sentence> vector) {
        int n;
        for (int i = n = sentence.scores.size(); i < vector.size(); ++i) {
            sentence.scores.add(sentence.score(vector.get(i)));
        }
    }

    public String hresolution() {
        Object object = "";
        long l = 0L;
        boolean bl = false;
        ArrayDeque<Sentence> arrayDeque = new ArrayDeque<Sentence>();
        Vector<Sentence> vector = new Vector<Sentence>(200);
        int n = 0;
        while (n < this.kb.size()) {
            Sentence sentence = new Sentence(this);
            sentence.sentence = this.kb.get(n);
            sentence.genflist();
            sentence.p1 = null;
            sentence.p2 = null;
            sentence.position = n++;
            vector.add(sentence);
            arrayDeque.push(sentence);
        }
        while (!bl) {
            Sentence sentence;
            n = 0;
            int n2 = 0;
            Sentence sentence2 = null;
            while (n2 == 0) {
                if (arrayDeque.size() == 0) {
                    bl = true;
                    break;
                }
                sentence2 = (Sentence)arrayDeque.pop();
                arrayDeque.push(sentence2);
                this.updatescores(sentence2, vector);
                for (int i = 0; i < sentence2.scores.size(); ++i) {
                    if (sentence2.scores.get(i) <= n2) continue;
                    n2 = sentence2.scores.get(i);
                    n = i;
                }
                if (n2 != 0) continue;
                arrayDeque.pop();
            }
            String string = null;
            if (!bl) {
                string = this.unify(sentence2.sentence, ((Sentence)vector.get((int)n)).sentence);
            }
            if (!bl && string == null) {
                sentence2.scores.remove(n);
                sentence2.scores.add(n, new Integer(0));
                if (vector.get((int)n).scores.size() >= sentence2.position) {
                    this.updatescores(vector.get(n), vector);
                }
                vector.get((int)n).scores.remove(sentence2.position);
                vector.get((int)n).scores.add(sentence2.position, new Integer(1));
                continue;
            }
            if (!bl && string.equals("")) {
                sentence = new Sentence(this);
                sentence.p1 = vector.get(sentence2.position);
                sentence.p2 = vector.get(n);
                sentence.sentence = string;
                object = (String)object + this.traceparents(sentence);
                this.huni = ++l;
                bl = true;
                continue;
            }
            if (bl) continue;
            sentence = new Sentence(this);
            sentence.sentence = string;
            sentence.p1 = vector.get(sentence2.position);
            sentence.p2 = vector.get(n);
            boolean bl2 = false;
            vector.get((int)sentence2.position).scores.remove(n);
            vector.get((int)sentence2.position).scores.add(n, new Integer(0));
            if (vector.get((int)n).scores.size() > sentence2.position) {
                vector.get((int)n).scores.remove(sentence2.position);
                vector.get((int)n).scores.add(sentence2.position, new Integer(1));
            }
            for (int i = 0; i < vector.size(); ++i) {
                if (sentence.subs(vector.get(i))) {
                    bl2 = true;
                    continue;
                }
                if (!vector.get(i).subs(sentence) || vector.get(i).equals(sentence)) continue;
                for (int j = 0; j < vector.size(); ++j) {
                    if (vector.get((int)j).scores.size() != vector.size()) {
                        vector.get(j).growscores(vector.size());
                    }
                    vector.get((int)j).scores.remove(i);
                    if (vector.get((int)j).position <= i) continue;
                    --vector.get((int)j).position;
                }
                arrayDeque.removeFirstOccurrence(vector.get(i));
                vector.remove(i);
            }
            if (!bl2) {
                sentence.position = vector.size();
                sentence.genflist();
                vector.add(sentence);
                arrayDeque.push(sentence);
            }
            ++l;
        }
        this.huni = l;
        return object;
    }

    public String randresolution() {
        Object object;
        long l = 0L;
        Vector<Sentence> vector = new Vector<Sentence>(200);
        boolean bl = false;
        long l2 = 0L;
        Random random = new Random();
        Object object2 = "";
        for (int i = 0; i < this.kb.size(); ++i) {
            object = new Sentence(this);
            ((Sentence)object).growscores(this.kb.size());
            ((Sentence)object).scores.remove(i);
            ((Sentence)object).scores.add(i, new Integer(1));
            ((Sentence)object).sentence = this.kb.get(i);
            ((Sentence)object).p1 = null;
            ((Sentence)object).p2 = null;
            vector.add((Sentence)object);
        }
        while (!bl) {
            int n = random.nextInt(vector.size());
            int n2 = random.nextInt(vector.size());
            Sentence sentence = (Sentence)vector.get(n);
            if (sentence.scores.size() < vector.size()) {
                sentence.growscores(vector.size());
            }
            while (n == n2 || sentence.scores.get(n2) == 1) {
                n = random.nextInt(vector.size());
                n2 = random.nextInt(vector.size());
                sentence = (Sentence)vector.get(n);
                if (sentence.scores.size() >= vector.size()) continue;
                sentence.growscores(vector.size());
            }
            if (((Sentence)vector.get((int)n2)).scores.size() < vector.size()) {
                ((Sentence)vector.get(n2)).growscores(vector.size());
            }
            if ((object = this.unify(((Sentence)vector.get((int)n)).sentence, ((Sentence)vector.get((int)n2)).sentence)) == null) {
                ((Sentence)vector.get((int)n)).scores.remove(n2);
                ((Sentence)vector.get((int)n)).scores.add(n2, new Integer(1));
                ((Sentence)vector.get((int)n2)).scores.remove(n);
                ((Sentence)vector.get((int)n2)).scores.add(n, new Integer(1));
                ++l2;
            } else if (((String)object).equals("")) {
                Sentence sentence2 = new Sentence(this);
                sentence2.p1 = (Sentence)vector.get(n);
                sentence2.p2 = (Sentence)vector.get(n2);
                sentence2.sentence = object;
                object2 = (String)object2 + this.traceparents(sentence2);
                this.runi = ++l;
                bl = true;
            } else {
                sentence = new Sentence(this);
                sentence.sentence = object;
                sentence.p1 = (Sentence)vector.get(n);
                sentence.p2 = (Sentence)vector.get(n2);
                boolean bl2 = false;
                ((Sentence)vector.get((int)n)).scores.remove(n2);
                ((Sentence)vector.get((int)n)).scores.add(n2, new Integer(1));
                ((Sentence)vector.get((int)n2)).scores.remove(n);
                ((Sentence)vector.get((int)n2)).scores.add(n, new Integer(1));
                for (int i = 0; i < vector.size(); ++i) {
                    if (sentence.subs(vector.get(i))) {
                        bl2 = true;
                        continue;
                    }
                    if (!vector.get(i).subs(sentence) || vector.get(i).equals(sentence)) continue;
                    for (int j = 0; j < vector.size(); ++j) {
                        if (vector.get((int)j).scores.size() != vector.size()) {
                            vector.get(j).growscores(vector.size());
                        }
                        vector.get((int)j).scores.remove(i);
                    }
                    vector.remove(i);
                }
                if (!bl2) {
                    sentence.growscores(vector.size() + 1);
                    sentence.scores.remove(vector.size());
                    sentence.scores.add(vector.size(), new Integer(1));
                    vector.add(sentence);
                }
                ++l;
                ++l2;
            }
            if (l2 < (long)(vector.size() * vector.size() / 2 - vector.size()) || !this.failtest(vector)) continue;
            bl = true;
        }
        this.runi = l;
        return object2;
    }

    String traceparents(Sentence sentence) {
        Object object = "";
        if (sentence.p1 != null) {
            object = (String)object + this.traceparents(sentence.p1);
        }
        if (sentence.p2 != null) {
            object = (String)object + this.traceparents(sentence.p2);
        }
        if (sentence.p1 != null) {
            object = (String)object + sentence.p1.sentence + "\n";
        }
        if (sentence.p2 != null) {
            object = (String)object + sentence.p2.sentence + "\n";
        }
        if (sentence.p1 != null && sentence.p2 != null) {
            object = (String)object + sentence.sentence + "\n\n";
        }
        return object;
    }

    public static void main(String[] stringArray) throws FileNotFoundException, IOException {
        long l;
        kbread kbread2 = new kbread(stringArray[0]);
        int n = 0;
        if (stringArray.length > 1) {
            try {
                n = Integer.parseInt(stringArray[1]);
            }
            catch (NumberFormatException numberFormatException) {
                System.err.println("Error parsing mode variable... using default settings");
            }
        }
        if (n != 1 && n != 2 && n != 0) {
            n = 0;
        }
        LinkedList<String> linkedList = kbread2.read();
        prover prover2 = new prover(linkedList);
        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
        String string = null;
        if (n == 0 || n == 1) {
            System.out.println("Random Run");
        }
        long l2 = runtimeMXBean.getUptime();
        long l3 = 0L;
        long l4 = 0L;
        if (n == 0 || n == 1) {
            try {
                string = prover2.randresolution();
            }
            catch (OutOfMemoryError outOfMemoryError) {
                System.out.println("Not enough memory");
            }
            if (string.equals("")) {
                System.out.println("no solution");
            } else {
                System.out.println(string);
            }
            l = runtimeMXBean.getUptime();
            l3 = l - l2;
        }
        if (n == 0 || n == 2) {
            System.out.println("Heuristic Run");
            l2 = runtimeMXBean.getUptime();
            try {
                string = prover2.hresolution();
            }
            catch (OutOfMemoryError outOfMemoryError) {
                System.out.println("Not enough memory");
            }
            if (string.equals("")) {
                System.out.println("no solution");
            } else {
                System.out.println(string);
            }
            l = runtimeMXBean.getUptime();
            l4 = l - l2;
        }
        if (n == 1 || n == 0) {
            System.out.println("Number of random unifications done: " + prover2.getRandunifies());
        }
        if (n == 2 || n == 0) {
            System.out.println("Number of heurisitic unifications done: " + prover2.getHunifies());
        }
        if (n == 1 || n == 0) {
            System.out.println("Random time: " + l3 + " milliseconds");
        }
        if (n == 2 || n == 0) {
            System.out.println("Heurisitic time: " + l4 + " milliseconds");
        }
        if (n == 0) {
            Object object;
            BigDecimal bigDecimal = new BigDecimal((double)prover2.getHunifies() / (double)prover2.getRandunifies());
            BigDecimal bigDecimal2 = new BigDecimal((double)l4 / (double)l3);
            bigDecimal = bigDecimal.round(new MathContext(2));
            bigDecimal2 = bigDecimal2.round(new MathContext(2));
            Object object2 = "" + bigDecimal.doubleValue() * 100.0;
            if (((String)object2).length() > 6) {
                object2 = ((String)object2).substring(0, 5);
            }
            if (((String)(object = "" + bigDecimal2.doubleValue() * 100.0)).length() > 6) {
                object = ((String)object).substring(0, 5);
            }
            System.out.println("Heuristic unifications " + (String)object2 + "% of random");
            System.out.println("Heuristic time " + (String)object + "% of random");
        }
    }

    private class Sentence {
        Sentence p1;
        Sentence p2;
        int position;
        private Vector<String> flist;
        boolean subbed = false;
        private String sentence;
        private Vector<Integer> scores = new Vector();

        Sentence(prover prover2) {
            this.flist = new Vector();
        }

        public void genflist() {
            if (this.flist.size() != 0) {
                this.flist = new Vector();
            }
            StringTokenizer stringTokenizer = new StringTokenizer(this.sentence, " ");
            int n = stringTokenizer.countTokens();
            for (int i = 0; i < n; ++i) {
                String string = stringTokenizer.nextToken();
                StringTokenizer stringTokenizer2 = new StringTokenizer(string, "(");
                this.flist.add(stringTokenizer2.nextToken());
            }
        }

        public int score(Sentence sentence) {
            int n = 0;
            for (int i = 0; i < this.flist.size(); ++i) {
                for (int j = 0; j < sentence.flist.size(); ++j) {
                    String string;
                    String string2 = this.flist.get(i);
                    if (string2.equals(string = sentence.flist.get(j))) {
                        ++n;
                        continue;
                    }
                    if (!(string2 = string2.replace("!", "")).equals(string = string.replace("!", ""))) continue;
                    n += 2;
                }
            }
            if (this.sentence.equals(sentence.sentence)) {
                n = 0;
            }
            return n;
        }

        public void growscores(int n) {
            int n2;
            for (int i = n2 = this.scores.size(); i < n; ++i) {
                this.scores.add(new Integer(0));
            }
        }

        public boolean equals(Object object) {
            Sentence sentence = (Sentence)object;
            return this.sentence.equals(sentence.sentence);
        }

        private boolean subs(Sentence sentence) {
            int n;
            if (this.sentence.equals(sentence.sentence)) {
                return true;
            }
            StringTokenizer stringTokenizer = new StringTokenizer(sentence.sentence, " ");
            StringTokenizer stringTokenizer2 = new StringTokenizer(this.sentence, " ");
            Vector<String> vector = new Vector<String>();
            Vector<String> vector2 = new Vector<String>();
            int n2 = stringTokenizer.countTokens();
            int n3 = stringTokenizer2.countTokens();
            for (n = 0; n < n2; ++n) {
                vector.add(stringTokenizer.nextToken());
            }
            for (n = 0; n < n3; ++n) {
                vector2.add(stringTokenizer2.nextToken());
            }
            return vector2.containsAll(vector) && !vector.containsAll(vector2);
        }
    }
}
