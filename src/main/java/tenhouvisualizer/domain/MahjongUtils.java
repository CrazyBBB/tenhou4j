package tenhouvisualizer.domain;

public class MahjongUtils {

    public static int computeSyanten(int[] tehai, int naki) {
        int tmp = 13;
        if (naki == 0) {
            tmp = Math.min(tmp, computeKokusiSyanten(tehai));
            tmp = Math.min(tmp, computeTiitoituSyanten(tehai));
        }
        tmp = Math.min(tmp, computeNormalSyanten(tehai, naki));

        return tmp;
    }

    public static int computeTiitoituSyanten(int[] tehai) {
        int toitu = 0;
        int syurui = 0;
        int syantenTiitoi;

        for (int i = 0; i < 34; i++) {
            if (tehai[i] >= 1) syurui++;
            if (tehai[i] >= 2) toitu++;
        }

        syantenTiitoi = 6 - toitu;

        if (syurui < 7) syantenTiitoi += 7 - syurui;
        return syantenTiitoi;
    }

    public static int computeKokusiSyanten(int[] tehai) {
        int kokusiToitu = 0;
        int syantenKokusi = 13;

        for (int i = 0; i < 34; i++) {
            if (i % 9 == 0 || i % 9 == 8 || i >= 27) {
                if (tehai[i] >= 1) syantenKokusi--;
                if (tehai[i] >= 2) kokusiToitu = 1;
            }
        }

        syantenKokusi -= kokusiToitu;
        return syantenKokusi;
    }

    private static int mentu;
    private static int toitu;
    private static int kouho;
    private static int syantenNormal;

    public static int computeNormalSyanten(int[] tehai, int naki) {
        mentu = naki;
        toitu = 0;
        kouho = 0;
        syantenNormal = 13;

        for (int i = 0; i < 34; i++) {
            if (tehai[i] >= 2) {
                toitu++;
                tehai[i] -= 2;
                mentuCut(0, tehai);
                tehai[i] += 2;
                toitu--;
            }
        }

        mentuCut(0, tehai);
        return syantenNormal;
    }

    public static void mentuCut(int i, int[] tehai) {
        while (i < 34 && tehai[i] == 0) i++;
        if (i == 34) {
            taatuCut(0, tehai);
            return;
        }

        if (tehai[i] >= 3) {
            mentu++;
            tehai[i] -= 3;
            mentuCut(i, tehai);
            tehai[i] += 3;
            mentu--;
        }

        if (i < 27 && i % 9 <= 6 && tehai[i + 1] >= 1 && tehai[i + 2] >= 1) {
            mentu++;
            tehai[i]--;
            tehai[i + 1]--;
            tehai[i + 2]--;
            mentuCut(i, tehai);
            tehai[i]++;
            tehai[i + 1]++;
            tehai[i + 2]++;
            mentu--;
        }

        mentuCut(i + 1, tehai);
    }

    public static void taatuCut(int i, int[] tehai) {
        while (i < 34 && tehai[i] == 0) i++;
        if (i == 34) {
            int tmp = 8 - mentu * 2 - kouho - toitu;
            syantenNormal = Math.min(syantenNormal, tmp);
            return;
        }

        if (mentu + kouho < 4) {
            if (tehai[i] >= 2) {
                kouho++;
                tehai[i] -= 2;
                taatuCut(i, tehai);
                tehai[i] += 2;
                kouho--;
            }

            if (i < 27 && i % 9 <= 7 && tehai[i + 1] >= 1) {
                kouho++;
                tehai[i]--;
                tehai[i + 1]--;
                taatuCut(i, tehai);
                tehai[i]++;
                tehai[i + 1]++;
                kouho--;
            }

            if (i < 27 && i % 9 <= 6 && tehai[i + 2] >= 1) {
                kouho++;
                tehai[i]--;
                tehai[i + 2]--;
                taatuCut(i, tehai);
                tehai[i]++;
                tehai[i + 2]++;
                kouho--;
            }
        }

        taatuCut(i + 1, tehai);
    }

}
