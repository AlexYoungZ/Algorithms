/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

public class BurrowsWheeler {

    // if args[0] is '-', apply Burrows-Wheeler encoding
    // if args[0] is '+', apply Burrows-Wheeler decoding
    public static void main(String[] args) {
        String par = args[0];
        if (par.equals("-")) {
            transform();
        }
        if (par.equals("+")) {
            inverseTransform();
        }
    }

    // apply Burrows-Wheeler encoding, reading
    // from standard input and writing to standard output
    public static void transform() {
        String s = BinaryStdIn.readString();
        CircularSuffixArray csa = new CircularSuffixArray(s);

        int length = csa.length();
        for (int i = 0; i < length; i++) {
            if (csa.index(i) == 0) {
                BinaryStdOut.write(i);
                break;
            }
        }
        for (int i = 0; i < length; i++) {
            BinaryStdOut.write(s.charAt((csa.index(i) + length - 1) % length));
        }
        BinaryStdOut.flush();
    }

    // apply Burrows-Wheeler decoding, reading
    // from standard input and writing to standard output
    public static void inverseTransform() {

        int start = BinaryStdIn.readInt();
        String s = BinaryStdIn.readString();

        int length = s.length();
        char[] t = new char[length];
        int[] counts = new int[256];

        //count occurrence of each character
        for (int i = 0; i < length; i++) {
            char c = s.charAt(i);
            t[i] = c;
            counts[c] += 1;
        }

        int[] offset = new int[256];
        int cum = 0;
        for (int i = 1; i < 256; i++) {
            cum = cum + counts[i - 1];
            offset[i] = cum;
        }

        int[] next = new int[length];  // ??? refer to readme
        counts = new int[256];
        for (int i = 0; i < t.length; i++) {
            char c = t[i];
            int alph = offset[c] + counts[c];
            counts[c] = counts[c] + 1;
            next[alph] = i;
        }

        int result = next[start];
        for (int i = 0; i < length - 1; i++) {
            BinaryStdOut.write(t[result]);
            result = next[result];
        }
        BinaryStdOut.flush();
        BinaryStdOut.write(t[start]);
        BinaryStdOut.flush();
    }
}
